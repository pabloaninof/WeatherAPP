package com.example.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val emailInput: String = "",
    val passwordInput: String = "",
    val usernameInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val loggedInUserId: Int? = null,
    val loggedInUsername: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(emailInput = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(passwordInput = password, errorMessage = null) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(usernameInput = username, errorMessage = null) }
    }

    fun login() {
        val state = _uiState.value
        if (state.emailInput.isBlank() || state.passwordInput.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.login(state.emailInput, state.passwordInput)
            result.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        loggedInUserId = user.id,
                        loggedInUsername = user.username,
                        passwordInput = ""
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error al iniciar sesión"
                    )
                }
            }
        }
    }

    fun register() {
        val state = _uiState.value
        if (state.usernameInput.isBlank() || state.emailInput.isBlank() || state.passwordInput.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.register(
                username = state.usernameInput,
                email = state.emailInput,
                passwordHash = state.passwordInput
            )
            result.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        loggedInUserId = user.id,
                        loggedInUsername = user.username,
                        passwordInput = ""
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error al registrar usuario"
                    )
                }
            }
        }
    }

    fun logout() {
        _uiState.update {
            AuthUiState()
        }
    }
}
