package com.example.logistica

import com.example.logistica.util.Fechas
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class FechasTest {

    // "Ahora" fijo: domingo 15 de noviembre de 2026, 12:00 en México
    private val ahora = Fechas.aMilis("2026-11-15T12:00:00-06:00")!!

    // ---------- esDeHoy ----------

    @Test
    fun hoy_esDeHoy() {
        assertTrue(Fechas.esDeHoy("2026-11-15T09:30:00-06:00", ahora))
    }

    @Test
    fun ayer_noEsDeHoy() {
        assertFalse(Fechas.esDeHoy("2026-11-14T18:00:00-06:00", ahora))
    }

    @Test
    fun medianoche_inicioDelDia_esDeHoy() {
        assertTrue(Fechas.esDeHoy("2026-11-15T00:00:00-06:00", ahora))
    }

    @Test
    fun unSegundoAntesDeMedianoche_noEsDeHoy() {
        assertFalse(Fechas.esDeHoy("2026-11-14T23:59:59-06:00", ahora))
    }

    @Test
    fun ultimoSegundoDelDia_esDeHoy() {
        assertTrue(Fechas.esDeHoy("2026-11-15T23:59:59-06:00", ahora))
    }

    @Test
    fun zonaUtc_queEnMexicoEsAyer_noEsDeHoy() {
        // 05:30 UTC del 15 = 23:30 del 14 en México
        assertFalse(Fechas.esDeHoy("2026-11-15T05:30:00Z", ahora))
    }

    @Test
    fun zonaUtc_queEnMexicoEsHoy_esDeHoy() {
        // 07:00 UTC del 15 = 01:00 del 15 en México
        assertTrue(Fechas.esDeHoy("2026-11-15T07:00:00+00:00", ahora))
    }

    @Test
    fun conFraccionesDeSegundo_seLeeBien() {
        assertTrue(Fechas.esDeHoy("2026-11-15T09:30:00.000000-06:00", ahora))
    }

    @Test
    fun nuloVacioOInvalido_noEsDeHoy() {
        assertFalse(Fechas.esDeHoy(null, ahora))
        assertFalse(Fechas.esDeHoy("", ahora))
        assertFalse(Fechas.esDeHoy("hola", ahora))
        assertFalse(Fechas.esDeHoy("2026-13-40T99:00:00-06:00", ahora))
        assertNull(Fechas.aMilis("no es fecha"))
    }

    // ---------- Semana (lunes 9 a domingo 15) ----------

    @Test
    fun semana_lunesALas00_estaDentro() {
        val semana = Fechas.rangoSemana(ahora)
        assertTrue(Fechas.estaEnRango("2026-11-09T00:00:00-06:00", semana))
    }

    @Test
    fun semana_domingoAnterior_estaFuera() {
        val semana = Fechas.rangoSemana(ahora)
        assertFalse(Fechas.estaEnRango("2026-11-08T23:59:59-06:00", semana))
    }

    @Test
    fun semana_siguienteLunes_estaFuera() {
        val semana = Fechas.rangoSemana(ahora)
        assertFalse(Fechas.estaEnRango("2026-11-16T00:00:00-06:00", semana))
    }

    @Test
    fun semana_texto() {
        assertEquals("Semana del 9 al 15 de nov", Fechas.textoSemana(Fechas.rangoSemana(ahora)))
    }

    // ---------- Fecha elegida en el calendario ----------

    @Test
    fun fechaElegida_10DeNoviembre() {
        // El DatePicker regresa el 10 de nov a las 00:00 UTC
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(2026, Calendar.NOVEMBER, 10, 0, 0, 0)
        }.timeInMillis

        val dia = Fechas.rangoDia(Fechas.diaDesdeDatePicker(utc))

        assertTrue(Fechas.estaEnRango("2026-11-10T20:00:00-06:00", dia))
        assertFalse(Fechas.estaEnRango("2026-11-11T08:00:00-06:00", dia))
        assertFalse(Fechas.estaEnRango("2026-11-09T23:00:00-06:00", dia))
    }
}