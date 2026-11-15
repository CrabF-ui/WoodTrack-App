package com.example.logistica.viewmodel

import com.example.logistica.data.model.Delivery
import com.example.logistica.util.Fechas

// Filtro de periodo
enum class Periodo { HOY, SEMANA, FECHA }

// Filtro de tipo
enum class TipoFinalizada { TODAS, ENTREGADAS, INCIDENCIAS }

// Estado de la vista "Finalizadas" (separado de la lista de entregas activas)
data class FinalizadasUiState(
    val todas: List<Delivery> = emptyList(),   // todo lo que mandó la API
    val cargando: Boolean = false,
    val error: String? = null,
    val periodo: Periodo = Periodo.HOY,
    val fechaElegida: Long? = null,             // 00:00 del día elegido (México)
    val tipo: TipoFinalizada = TipoFinalizada.TODAS
) {

    // Periodo de tiempo según el filtro
    fun rango(ahora: Long = System.currentTimeMillis()): Fechas.Rango = when (periodo) {
        Periodo.HOY -> Fechas.rangoDia(ahora)
        Periodo.SEMANA -> Fechas.rangoSemana(ahora)
        Periodo.FECHA -> Fechas.rangoDia(fechaElegida ?: ahora)
    }

    // Lo que se muestra: filtrado por periodo y tipo, la más reciente primero
    fun filtradas(ahora: Long = System.currentTimeMillis()): List<Delivery> {
        val rango = rango(ahora)

        return todas
            .filter { Fechas.estaEnRango(it.estado_fecha_hora, rango) }
            .filter {
                when (tipo) {
                    TipoFinalizada.TODAS -> true
                    TipoFinalizada.ENTREGADAS -> it.estado == "entregada"
                    TipoFinalizada.INCIDENCIAS -> it.estado == "incidencia"
                }
            }
            .sortedByDescending { Fechas.aMilis(it.estado_fecha_hora) ?: 0L }
    }

    // Texto del encabezado: "Hoy · Domingo 15 de noviembre", "Semana del 9 al 15 de nov"…
    fun textoPeriodo(ahora: Long = System.currentTimeMillis()): String = when (periodo) {
        Periodo.HOY -> "Hoy · " + Fechas.textoDia(ahora)
        Periodo.SEMANA -> Fechas.textoSemana(rango(ahora))
        Periodo.FECHA -> Fechas.textoDia(fechaElegida ?: ahora)
    }

    // Mensaje cuando no hay nada con esos filtros
    fun textoVacio(): String {
        val que = when (tipo) {
            TipoFinalizada.TODAS -> "entregas finalizadas"
            TipoFinalizada.ENTREGADAS -> "entregas completadas"
            TipoFinalizada.INCIDENCIAS -> "incidencias"
        }
        val cuando = when (periodo) {
            Periodo.HOY -> "hoy"
            Periodo.SEMANA -> "esta semana"
            Periodo.FECHA -> "ese día"
        }
        return "No hay $que $cuando."
    }
}