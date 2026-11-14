package com.example.logistica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.logistica.data.repository.DeliveryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val sesionIniciada: Boolean = false
)

class LoginViewModel(
    private val repository: DeliveryRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    fun iniciarSesion(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _estado.value = LoginUiState(error = "Completa todos los campos.")
            return
        }

        viewModelScope.launch {
            _estado.value = LoginUiState(cargando = true)

            repository.iniciarSesion(email.trim(), password)
                .onSuccess {
                    _estado.value = LoginUiState(sesionIniciada = true)
                }
                .onFailure { error ->
                    _estado.value = LoginUiState(
                        error = error.message ?: "Error desconocido"
                    )
                }
        }
    }

    // Se llama después de navegar para que no vuelva a navegar
    fun sesionNavegada() {
        _estado.value = LoginUiState()
    }
}

// Crea los ViewModels pasándoles el repository
fun appViewModelFactory(repository: DeliveryRepository) = viewModelFactory {
    initializer { LoginViewModel(repository) }
    initializer { DeliveryViewModel(repository) }
}