/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.crazy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.crazy.common.network.result.NetworkResult
import com.android.crazy.common.worker.WorkerManager
import com.android.crazy.data.model.Email
import com.android.crazy.data.model.User
import com.android.crazy.data.repository.EmailsRepository
import com.android.crazy.data.repository.EmailsRepositoryImpl
import com.android.crazy.data.repository.UserRepository
import com.android.crazy.ui.utils.CrazyContentType
import com.android.crazy.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrazyHomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) :
    ViewModel() {

    private val emailsRepository: EmailsRepository = EmailsRepositoryImpl()

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(CrazyHomeUIState(loading = true))
    val uiState: StateFlow<CrazyHomeUIState> = _uiState

    init {
        WorkerManager.instance.createWorker()
        observeEmails()
        //testUser()
    }

    private fun observeEmails() {
        viewModelScope.launch {
            emailsRepository.getAllEmails()
                .catch { ex ->
                    _uiState.value = CrazyHomeUIState(error = ex.message)
                }
                .collect { emails ->
                    /**
                     * We set first email selected by default for first App launch in large-screens
                     */
                    _uiState.value = CrazyHomeUIState(
                        emails = emails,
                        selectedEmail = emails.first()
                    )
                }
        }
    }

    fun setSelectedEmail(emailId: Long, contentType: CrazyContentType) {
        /**
         * We only set isDetailOnlyOpen to true when it's only single pane layout
         */
        val email = uiState.value.emails.find { it.id == emailId }
        _uiState.value = _uiState.value.copy(
            selectedEmail = email,
            isDetailOnlyOpen = contentType == CrazyContentType.SINGLE_PANE
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                selectedEmail = _uiState.value.emails.first()
            )
    }

    fun testUser() {
        viewModelScope.launch {
            userRepository.getAllUserRemote().collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        Logger.e(msg = "Loading")
                    }
                    is NetworkResult.Success -> {
                        Logger.e(msg = "Success")
                        it.data?.let { userList -> userRepository.insertUserBatchLocal(userList) }
                    }
                    is NetworkResult.Failure -> {
                        Logger.e(msg = "Failure ${it.errorMessage}")
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        viewModelScope.launch {
            if (query.toIntOrNull() != null) {
                userRepository.getUserRemote(query.toInt()).collect {
                    when (it) {
                        is NetworkResult.Loading -> {
                            Logger.e(msg = "Loading")
                        }
                        is NetworkResult.Success -> {
                            it.data?.let { user -> Logger.e(msg = "Success $user") }
                        }
                        is NetworkResult.Failure -> {
                            Logger.e(msg = "Failure ${it.errorMessage}")
                        }
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(crazyProfileUIState = CrazyProfileUIState())
            userRepository.login(email, password).collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        _uiState.value =
                            _uiState.value.copy(crazyProfileUIState = CrazyProfileUIState(loading = true))
                    }
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            _uiState.value =
                                _uiState.value.copy(
                                    crazyProfileUIState = CrazyProfileUIState(
                                        user = user,
                                        loading = false,
                                        isLogin = true
                                    )
                                )
                        }
                    }
                    is NetworkResult.Failure -> {
                        _uiState.value =
                            _uiState.value.copy(
                                crazyProfileUIState = CrazyProfileUIState(
                                    error = "Login failed please try again",
                                    loading = false
                                )
                            )
                    }
                }
            }
        }
    }
}

data class CrazyHomeUIState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val crazyProfileUIState: CrazyProfileUIState = CrazyProfileUIState()
)

data class CrazyProfileUIState(
    val loading: Boolean = false,
    val error: String? = null,
    val isLogin: Boolean = false,
    val user: User? = null
)
