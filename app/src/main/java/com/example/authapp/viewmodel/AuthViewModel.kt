package com.example.authapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.authapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    init {
        checkAuth() // Check authentication status when the ViewModel is created
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.register(email, password)
            _authState.value = if (result.isSuccess) AuthState.Authenticated else
                AuthState.Error(result.exceptionOrNull()?.message ?: "Erro ao registrar")
        }
    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            _authState.value = if (result.isSuccess) AuthState.Authenticated else
                AuthState.Error(result.exceptionOrNull()?.message ?: "Usuário ou senha inválidos")
        }
    }
    fun checkAuth() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.me()
            _authState.value = if (result.isSuccess) AuthState.Authenticated else AuthState.Unauthenticated
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Unauthenticated
        }
    }

    // Factory for AuthViewModel
    class Factory(private val repository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String): AuthState()
}