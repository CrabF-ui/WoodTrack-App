package com.example.logistica.notificaciones

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.logistica.R
import com.example.logistica.com.example.logistica.MainActivity
import com.example.logistica.data.model.Delivery

// Igual que en la Práctica 07: canal, construcción y lanzamiento de notificaciones
class NotificationHelper(private val context: Context) {

    // Información general de notificaciones
    companion object {
        const val CHANNEL_ENTREGAS_ID = "Canal_entregas"
        const val NOTIFICATION_ENTREGAS_ID = 300
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        // Los canales existen desde Android 8 (nuestro SDK mínimo es 24)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canalEntregas = NotificationChannel(
                CHANNEL_ENTREGAS_ID,
                "Entregas asignadas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisos cuando te asignan una entrega nueva"
            }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(canalEntregas)
        }
    }

    // Notificación de toque + botón de acción (como en la guía)
    @SuppressLint("MissingPermission")
    fun showNuevasEntregasNotification(nuevas: List<Delivery>) {
        if (nuevas.isEmpty()) return

        // Si el chofer negó el permiso, no hacemos nada
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return

        // 1. Intent que abre la app (cae en la lista porque ya hay sesión)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // 2. PendingIntent (FLAG_IMMUTABLE obligatorio en Android 12+)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Textos
        val titulo = if (nuevas.size == 1) {
            "Nueva entrega asignada"
        } else {
            "Tienes ${nuevas.size} entregas nuevas"
        }

        val texto = if (nuevas.size == 1) {
            "${nuevas[0].pedido.cliente} · ${nuevas[0].pedido.direccion}"
        } else {
            nuevas.joinToString { it.pedido.cliente }
        }

        val detalle = nuevas.joinToString("\n") {
            "• Pedido #${it.pedido.id} · ${it.pedido.cliente} · ${it.pedido.direccion}"
        }

        // 4. Construir la notificación
        val builder = NotificationCompat.Builder(context, CHANNEL_ENTREGAS_ID)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(detalle))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)   // al tocarla abre la app
            .setAutoCancel(true)               // se quita al tocarla
            .addAction(R.drawable.ic_notificacion, "Ver entregas", pendingIntent)

        // 5. Disparar la notificación
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ENTREGAS_ID, builder.build())
    }

    fun cancelNotification(id: Int) {
        NotificationManagerCompat.from(context).cancel(id)
    }
}