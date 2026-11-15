package com.example.logistica

import com.example.logistica.data.model.Delivery
import com.example.logistica.data.model.Pedido
import com.example.logistica.util.Fechas
import com.example.logistica.viewmodel.FinalizadasUiState
import com.example.logistica.viewmodel.Periodo
import com.example.logistica.viewmodel.TipoFinalizada
import org.junit.Assert.assertEquals
import org.junit.Test

class FinalizadasFiltroTest {

    // "Ahora" fijo: domingo 15 de noviembre de 2026, 12:00 en México
    private val ahora = Fechas.aMilis("2026-11-15T12:00:00-06:00")!!

    private fun entrega(id: Int, estado: String, finalizada: String) = Delivery(
        id = id,
        fecha_asignada = "2026-11-15",
        orden = 1,
        estado = estado,
        estado_fecha_hora = finalizada,
        notas = null,
        pedido = Pedido(id, "Cliente $id", null, "Calle $id", null, emptyList())
    )

    private val lista = listOf(
        entrega(1, "entregada", "2026-11-15T09:00:00-06:00"),   // hoy
        entrega(2, "incidencia", "2026-11-15T10:00:00-06:00"),  // hoy
        entrega(3, "entregada", "2026-11-14T10:00:00-06:00"),   // ayer (misma semana)
        entrega(4, "entregada", "2026-11-02T10:00:00-06:00")    // otra semana
    )

    private fun ids(estado: FinalizadasUiState) = estado.filtradas(ahora).map { it.id }

    @Test
    fun hoy_todas_laMasRecientePrimero() {
        val estado = FinalizadasUiState(todas = lista)
        assertEquals(listOf(2, 1), ids(estado))
    }

    @Test
    fun hoy_soloIncidencias() {
        val estado = FinalizadasUiState(todas = lista, tipo = TipoFinalizada.INCIDENCIAS)
        assertEquals(listOf(2), ids(estado))
    }

    @Test
    fun semana_soloEntregadas() {
        val estado = FinalizadasUiState(
            todas = lista,
            periodo = Periodo.SEMANA,
            tipo = TipoFinalizada.ENTREGADAS
        )
        assertEquals(listOf(1, 3), ids(estado))
    }

    @Test
    fun fechaElegida_ayer() {
        val ayer = Fechas.aMilis("2026-11-14T00:00:00-06:00")!!
        val estado = FinalizadasUiState(
            todas = lista,
            periodo = Periodo.FECHA,
            fechaElegida = ayer
        )
        assertEquals(listOf(3), ids(estado))
    }

    @Test
    fun sinResultados_textoVacio() {
        val estado = FinalizadasUiState(periodo = Periodo.SEMANA, tipo = TipoFinalizada.INCIDENCIAS)
        assertEquals("No hay incidencias esta semana.", estado.textoVacio())
    }
}