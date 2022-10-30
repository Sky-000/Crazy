package com.android.crazy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.crazy.common.network.result.NetworkResult
import com.android.crazy.data.model.User
import com.android.crazy.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrazyProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CrazyProfileUIState())
    val uiState: StateFlow<CrazyProfileUIState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = CrazyProfileUIState()
            userRepository.login(email, password).collect {
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
}

data class CrazyProfileUIState(
    val loading: Boolean = false,
    val error: String? = null,
    val isLogin: Boolean = false,
    val user: User? = null
)
