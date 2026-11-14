package com.example.logistica.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

// Crea un archivo vacío donde la cámara guardará la foto
fun crearArchivoFoto(context: Context): File {
    val carpeta = File(context.cacheDir, "fotos").apply { mkdirs() }
    return File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
}

// Dirección que se le pasa a la cámara para que escriba en ese archivo
fun uriParaCamara(context: Context, archivo: File): Uri =
    FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )

// Reduce la foto a máximo 1600 px y la endereza si viene de lado.
// Así queda muy por debajo del límite de 5 MB de la API.
fun comprimirFoto(ruta: String) {
    val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(ruta, limites)
    if (limites.outWidth <= 0) return

    var muestra = 1
    while (limites.outWidth / muestra > 1600 || limites.outHeight / muestra > 1600) {
        muestra *= 2
    }

    val original = BitmapFactory.decodeFile(
        ruta,
        BitmapFactory.Options().apply { inSampleSize = muestra }
    ) ?: return

    val grados = when (
        ExifInterface(ruta).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
    ) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    val bitmap = if (grados != 0f) {
        Bitmap.createBitmap(
            original, 0, 0, original.width, original.height,
            Matrix().apply { postRotate(grados) }, true
        )
    } else {
        original
    }

    FileOutputStream(ruta).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, it)
    }
}

// Miniatura ligera para mostrar en pantalla
fun miniaturaFoto(ruta: String): Bitmap? =
    BitmapFactory.decodeFile(
        ruta,
        BitmapFactory.Options().apply { inSampleSize = 8 }
    )

// Dibuja los trazos de la firma en una imagen PNG con fondo blanco
fun guardarFirma(
    context: Context,
    trazos: List<List<Offset>>,
    ancho: Int,
    alto: Int
): File {
    val bitmap = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)

    val pincel = Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 6f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }

    trazos.forEach { trazo ->
        if (trazo.size == 1) {
            canvas.drawPoint(trazo[0].x, trazo[0].y, pincel)
        }
        for (i in 1 until trazo.size) {
            canvas.drawLine(
                trazo[i - 1].x, trazo[i - 1].y,
                trazo[i].x, trazo[i].y,
                pincel
            )
        }
    }

    val archivo = File(context.cacheDir, "firma_${System.currentTimeMillis()}.png")
    FileOutputStream(archivo).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    return archivo
}