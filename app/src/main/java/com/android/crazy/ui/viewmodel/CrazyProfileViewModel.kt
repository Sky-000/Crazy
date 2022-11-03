package com.android.crazy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.crazy.common.network.result.NetworkResult
import com.android.crazy.data.model.LoginForm
import com.android.crazy.data.model.User
import com.android.crazy.data.repository.UserRepository
import com.android.crazy.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrazyProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CrazyProfileUiState(
            loginForm = LoginForm(
                email = "15252114322@163.com",
                password = "wcnm741741.."
            )
        )
    )
    val uiState: StateFlow<CrazyProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            userRepository.getAllUserLocal().collect {
                if (it.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(user = it.firstOrNull())
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            userRepository.login(uiState.value.loginForm).collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(loading = true)
                    }
                    is NetworkResult.Success -> {
                        delay(2000L)
                        it.data.let { user ->
                            _uiState.value =
                                _uiState.value.copy(
                                    user = user,
                                    loading = false,
                                )
                            userRepository.insertUserLocal(user)
                        }
                        navigateToProfile()
                    }
                    is NetworkResult.Error -> {
                        _uiState.value =
                            _uiState.value.copy(
                                error = "Login failed please try again",
                                loading = false
                            )
                    }
                }
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            loginForm = _uiState.value.loginForm.copy(email = email),
            error = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            loginForm = _uiState.value.loginForm.copy(password = password),
            error = null
        )
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true
            )
            delay(1000L)
            _uiState.value.user?.let {
                userRepository.deleteUserLocal(it)
                _uiState.value = _uiState.value.copy(
                    user = null,
                    loading = false
                )
            }
            navigateToProfile()
        }
    }

    fun navigateToProfile() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.PROFILE)
    }

    fun navigateToUserSettings() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.USER_SETTINGS)
    }

    fun navigateToLogin() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.LOGIN)
    }

    fun navigateToAbout() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.ABOUT)
    }

    fun navigateToSettings() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.SETTINGS)
    }


    fun modifyUser(modifyType: ModifyType) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                page = CrazyProfilePage.MODIFY,
                modifyState = ModifyState(
                    title = if (modifyType == ModifyType.NAME) "修改昵称" else if (modifyType == ModifyType.PHONE) "修改手机号" else "修改邮箱",
                    onBackPressed = { navigateToUserSettings() },
                    onDone = { value ->
                        updateUser(
                            _uiState.value.user!!.copy(
                                name = if (modifyType == ModifyType.NAME) value else _uiState.value.user!!.name,
                                phone = if (modifyType == ModifyType.PHONE) value else _uiState.value.user!!.phone,
                                email = if (modifyType == ModifyType.EMAIL) value else _uiState.value.user!!.email,
                            )
                        )
                    },
                    value = if (modifyType == ModifyType.NAME) _uiState.value.user!!.name else if (modifyType == ModifyType.PHONE) _uiState.value.user!!.phone else _uiState.value.user!!.email,
                )
            )
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUserRemote(user).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(loading = true)
                    }
                    is NetworkResult.Success -> {
                        delay(1000L)
                        userRepository.updateUserLocal(user)
                        _uiState.value = _uiState.value.copy(loading = false)
                        navigateToUserSettings()
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = _uiState.value.copy(loading = false)
                        Logger.e(msg = "Update user failed")
                    }
                }
            }
        }
    }

}

data class CrazyProfileUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val loginForm: LoginForm = LoginForm(),
    val modifyState: ModifyState = ModifyState(),
    val page: CrazyProfilePage = CrazyProfilePage.PROFILE
)

data class ModifyState(
    val title: String = "",
    val onBackPressed: () -> Unit = {},
    val onDone: (String) -> Unit = {},
    val value: String = "",
)

enum class CrazyProfilePage {
    PROFILE, USER_SETTINGS, LOGIN, ABOUT, SETTINGS, MODIFY
}

enum class ModifyType {
    NAME, PHONE, EMAIL
}
