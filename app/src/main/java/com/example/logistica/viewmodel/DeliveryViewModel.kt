package com.example.logistica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica.data.model.Delivery
import com.example.logistica.data.repository.DeliveryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DeliveryUiState(
    val entregas: List<Delivery> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null,

    // Para guardar estado / incidencia
    val guardando: Boolean = false,
    val errorAccion: String? = null,

    // Para la pantalla de "incidencia registrada"
    val ultimaIncidencia: Delivery? = null,
    val motivoIncidencia: String? = null,

    // Para la pantalla de "entrega registrada"
    val ultimaEntrega: Delivery? = null,
    val receptorUltimaEntrega: String? = null,
    val horaUltimaEntrega: String? = null
)

class DeliveryViewModel(
    private val repository: DeliveryRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(DeliveryUiState())
    val estado: StateFlow<DeliveryUiState> = _estado

    fun nombreUsuario(): String? = repository.nombreUsuario()

    fun cargarEntregas() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }

            repository.obtenerEntregas()
                .onSuccess { lista ->
                    _estado.update { it.copy(entregas = lista, cargando = false) }
                }
                .onFailure { e ->
                    _estado.update {
                        it.copy(cargando = false, error = e.message ?: "Error desconocido")
                    }
                }
        }
    }

    fun limpiarErrorAccion() {
        _estado.update { it.copy(errorAccion = null) }
    }

    // Cambia entre "pendiente" y "en_camino"
    fun actualizarEstado(id: Int, nuevoEstado: String, onExito: () -> Unit) {
        viewModelScope.launch {
            _estado.update { it.copy(guardando = true, errorAccion = null) }

            repository.actualizarEstado(id, nuevoEstado)
                .onSuccess { entregaActualizada ->
                    // Reemplaza la entrega en la lista con la que regresó la API
                    _estado.update { s ->
                        s.copy(
                            guardando = false,
                            entregas = s.entregas.map {
                                if (it.id == entregaActualizada.id) entregaActualizada else it
                            }
                        )
                    }
                    onExito()
                }
                .onFailure { e ->
                    _estado.update { it.copy(guardando = false, errorAccion = e.message) }
                }
        }
    }

    // Registra la incidencia y cierra la entrega
    fun registrarIncidencia(id: Int, motivo: String, onExito: () -> Unit) {
        viewModelScope.launch {
            _estado.update { it.copy(guardando = true, errorAccion = null) }

            repository.registrarIncidencia(id, motivo)
                .onSuccess { entregaCerrada ->
                    _estado.update { s ->
                        s.copy(
                            guardando = false,
                            ultimaIncidencia = entregaCerrada,
                            motivoIncidencia = motivo,
                            // Ya está cerrada: se quita de la lista
                            entregas = s.entregas.filter { it.id != entregaCerrada.id }
                        )
                    }
                    onExito()
                }
                .onFailure { e ->
                    _estado.update { it.copy(guardando = false, errorAccion = e.message) }
                }
        }
    }

    // Registra la entrega con fotos y firma (o motivo sin firma)
    fun entregar(
        id: Int,
        receptorNombre: String,
        fotos: List<File>,
        firma: File?,
        motivoSinFirma: String?,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            _estado.update { it.copy(guardando = true, errorAccion = null) }

            repository.entregar(id, receptorNombre, fotos, firma, motivoSinFirma)
                .onSuccess { entregaApi ->
                    // Borra las fotos temporales del celular
                    (fotos + listOfNotNull(firma)).forEach { it.delete() }

                    _estado.update { s ->
                        // Si la API no mandó la entrega (reintento "ya fue cerrada"),
                        // se usan los datos que ya estaban en la lista
                        val registrada = entregaApi ?: s.entregas.find { it.id == id }

                        // Hora real de la API; si no viene, la del celular
                        val hora = entregaApi?.estado_fecha_hora
                            ?.takeIf { it.length >= 16 }
                            ?.substring(11, 16)
                            ?: SimpleDateFormat("HH:mm", Locale("es", "MX")).format(Date())

                        s.copy(
                            guardando = false,
                            ultimaEntrega = registrada,
                            receptorUltimaEntrega = receptorNombre,
                            horaUltimaEntrega = hora,
                            // Ya está entregada: se quita de la lista
                            entregas = s.entregas.filter { it.id != id }
                        )
                    }
                    onExito()
                }
                .onFailure { e ->
                    _estado.update { it.copy(guardando = false, errorAccion = e.message) }
                }
        }
    }

    // Cierra sesión: avisa a la API, borra el token y limpia los datos
    fun cerrarSesion(onListo: () -> Unit) {
        viewModelScope.launch {
            repository.cerrarSesion()
            // Limpia las entregas del chofer anterior
            _estado.value = DeliveryUiState()
            onListo()
        }
    }
}