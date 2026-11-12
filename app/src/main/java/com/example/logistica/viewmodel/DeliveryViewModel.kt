package com.example.logistica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.logistica.data.model.Delivery
import com.example.logistica.data.repository.DeliveryRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DeliveryUiState(
    val entregas: List<Delivery> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null,
    val mensaje: String? = null
)

class DeliveryViewModel(
    private val repository: DeliveryRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(DeliveryUiState())

    val estado: StateFlow<DeliveryUiState> = _estado

    fun cargarEntregas() {

        viewModelScope.launch {

            _estado.value = _estado.value.copy(
                cargando = true,
                error = null
            )

            val resultado = repository.obtenerEntregas()

            resultado.onSuccess { entregas ->

                _estado.value = DeliveryUiState(
                    entregas = entregas,
                    cargando = false
                )

            }.onFailure { error ->

                _estado.value = _estado.value.copy(
                    cargando = false,
                    error = error.message ?: "Error desconocido"
                )
            }
        }
    }

    fun actualizarEstado(id: Int, nuevoEstado: String) {

        viewModelScope.launch {

            _estado.value = _estado.value.copy(
                cargando = true,
                error = null,
                mensaje = null
            )

            val resultado = repository.actualizarEstado(
                id,
                nuevoEstado
            )

            resultado.onSuccess { mensaje ->

                _estado.value = _estado.value.copy(
                    cargando = false,
                    mensaje = mensaje
                )

                cargarEntregas()

            }.onFailure { error ->

                _estado.value = _estado.value.copy(
                    cargando = false,
                    error = error.message
                )
            }
        }
    }
}