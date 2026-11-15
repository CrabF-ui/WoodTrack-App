package com.example.logistica.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

// Funciones de fechas para la vista "Finalizadas".
// Se usa Calendar/SimpleDateFormat (y no java.time) porque el SDK mínimo es 24
// y java.time solo existe desde Android 8 (API 26).
object Fechas {

    val ZONA_MEXICO: TimeZone = TimeZone.getTimeZone("America/Mexico_City")
    private val ES_MX = Locale("es", "MX")

    // Periodo de tiempo: desde [inicio] (incluido) hasta [fin] (sin incluir), en milisegundos
    data class Rango(val inicio: Long, val fin: Long)

    // "2026-11-08T11:30:00-06:00" → milisegundos. null si viene vacío o no se puede leer.
    fun aMilis(fechaHora: String?): Long? {
        if (fechaHora.isNullOrBlank()) return null

        // Quita las fracciones de segundo (".000000"), que SimpleDateFormat no lee bien
        val limpia = fechaHora.trim().replace(Regex("\\.\\d+"), "")

        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
                .apply { isLenient = false }
                .parse(limpia)
                ?.time
        } catch (e: ParseException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    // El mismo momento pero a las 00:00 de ese día, en la zona dada
    private fun inicioDelDia(milis: Long, zona: TimeZone): Calendar =
        Calendar.getInstance(zona).apply {
            timeInMillis = milis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

    // El día completo que contiene ese momento
    fun rangoDia(milis: Long, zona: TimeZone = ZONA_MEXICO): Rango {
        val cal = inicioDelDia(milis, zona)
        val inicio = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 1)
        return Rango(inicio, cal.timeInMillis)
    }

    // La semana de lunes a domingo que contiene ese momento
    fun rangoSemana(milis: Long, zona: TimeZone = ZONA_MEXICO): Rango {
        val cal = inicioDelDia(milis, zona)

        // DAY_OF_WEEK: domingo = 1, lunes = 2 … sábado = 7
        val diasDesdeLunes = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
        cal.add(Calendar.DAY_OF_MONTH, -diasDesdeLunes)

        val inicio = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 7)
        return Rango(inicio, cal.timeInMillis)
    }

    fun estaEnRango(fechaHora: String?, rango: Rango): Boolean {
        val milis = aMilis(fechaHora) ?: return false
        return milis >= rango.inicio && milis < rango.fin
    }

    fun esDeHoy(fechaHora: String?, ahora: Long, zona: TimeZone = ZONA_MEXICO): Boolean =
        estaEnRango(fechaHora, rangoDia(ahora, zona))

    // El calendario (DatePicker) de Material 3 regresa el día elegido a las 00:00 UTC.
    // Esto lo convierte a las 00:00 de ese mismo día en México.
    fun diaDesdeDatePicker(utcMilis: Long, zona: TimeZone = ZONA_MEXICO): Long {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = utcMilis
        }
        return Calendar.getInstance(zona).apply {
            clear()
            set(
                utc.get(Calendar.YEAR),
                utc.get(Calendar.MONTH),
                utc.get(Calendar.DAY_OF_MONTH),
                0, 0, 0
            )
        }.timeInMillis
    }

    // Al revés: para que el calendario abra en el día que ya estaba elegido
    fun aDatePicker(milis: Long, zona: TimeZone = ZONA_MEXICO): Long {
        val local = Calendar.getInstance(zona).apply { timeInMillis = milis }
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(
                local.get(Calendar.YEAR),
                local.get(Calendar.MONTH),
                local.get(Calendar.DAY_OF_MONTH),
                0, 0, 0
            )
        }.timeInMillis
    }

    // "Lun 9 nov · 11:30"
    fun formatoDiaHora(fechaHora: String?, zona: TimeZone = ZONA_MEXICO): String {
        val milis = aMilis(fechaHora) ?: return ""
        val texto = SimpleDateFormat("EEE d MMM · HH:mm", ES_MX)
            .apply { timeZone = zona }
            .format(milis)
        return texto.replace(".", "").replaceFirstChar { it.uppercase() }
    }

    // "Lunes 9 de noviembre"
    fun textoDia(milis: Long, zona: TimeZone = ZONA_MEXICO): String =
        SimpleDateFormat("EEEE d 'de' MMMM", ES_MX)
            .apply { timeZone = zona }
            .format(milis)
            .replaceFirstChar { it.uppercase() }

    // "Semana del 9 al 15 de nov" o "Semana del 30 nov al 6 dic"
    fun textoSemana(rango: Rango, zona: TimeZone = ZONA_MEXICO): String {
        val dia = SimpleDateFormat("d", ES_MX).apply { timeZone = zona }
        val mes = SimpleDateFormat("MMM", ES_MX).apply { timeZone = zona }
        val ultimo = rango.fin - 1

        val mesInicio = mes.format(rango.inicio).replace(".", "")
        val mesFin = mes.format(ultimo).replace(".", "")

        return if (mesInicio == mesFin) {
            "Semana del ${dia.format(rango.inicio)} al ${dia.format(ultimo)} de $mesFin"
        } else {
            "Semana del ${dia.format(rango.inicio)} $mesInicio al ${dia.format(ultimo)} $mesFin"
        }
    }
}