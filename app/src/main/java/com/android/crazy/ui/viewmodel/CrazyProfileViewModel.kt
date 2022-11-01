package com.android.crazy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.crazy.common.network.result.NetworkResult
import com.android.crazy.data.model.LoginForm
import com.android.crazy.data.model.User
import com.android.crazy.data.repository.UserRepository
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
        CrazyProfileUIState(
            loginForm = LoginForm(
                email = "15252114322@163.com",
                password = "wcnm741741.."
            )
        )
    )
    val uiState: StateFlow<CrazyProfileUIState> = _uiState

    fun login() {
        viewModelScope.launch {
            userRepository.login(uiState.value.loginForm).collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(loading = true)
                    }
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            _uiState.value =
                                _uiState.value.copy(
                                    user = user,
                                    loading = false,
                                    isLogin = true
                                )

                        }
                        navigateToProfile()
                    }
                    is NetworkResult.Failure -> {
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
            _uiState.value = CrazyProfileUIState(
                loginForm = LoginForm(
                    email = "15252114322@163.com",
                    password = "wcnm741741.."
                )
            )
        }
    }

    fun navigateToLogin() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.LOGIN)
    }

    fun navigateToSetting() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.SETTING)
    }

    fun navigateToProfile() {
        _uiState.value = _uiState.value.copy(page = CrazyProfilePage.PROFILE)
    }
}

data class CrazyProfileUIState(
    val loading: Boolean = false,
    val error: String? = null,
    val isLogin: Boolean = false,
    val user: User? = null,
    val loginForm: LoginForm = LoginForm(),
    val page: CrazyProfilePage = CrazyProfilePage.PROFILE
)

enum class CrazyProfilePage {
    PROFILE, SETTING, LOGIN
}
