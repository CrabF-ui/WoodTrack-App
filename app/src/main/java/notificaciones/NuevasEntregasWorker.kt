package com.example.logistica.notificaciones

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.logistica.data.remote.RetrofitClient
import com.example.logistica.data.remote.TokenManager
import com.example.logistica.data.repository.DeliveryRepository
import java.util.concurrent.TimeUnit

// Revisa en segundo plano si al chofer le asignaron entregas nuevas
class NuevasEntregasWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val tokenManager = TokenManager(applicationContext)

        // Sin sesión no hay nada que revisar
        if (tokenManager.obtenerToken().isNullOrBlank()) return Result.success()

        // Las que el chofer ya había visto ANTES de esta revisión
        val vistasAntes = tokenManager.obtenerIdsVistos()

        val repository = DeliveryRepository(
            RetrofitClient.crearApi(tokenManager),
            tokenManager
        )

        // Pide la lista (y la marca como vista para la próxima revisión)
        val entregas = repository.obtenerEntregas().getOrElse {
            return Result.retry()   // sin internet: se intenta más tarde
        }

        // La primera vez no se notifica nada, solo se guardan
        if (vistasAntes != null) {
            val nuevas = entregas.filter { it.id !in vistasAntes }
            NotificationHelper(applicationContext).showNuevasEntregasNotification(nuevas)
        }

        return Result.success()
    }

    companion object {
        private const val NOMBRE = "revisar_entregas_nuevas"
        private const val NOMBRE_PRUEBA = "revisar_entregas_prueba"

        private val conInternet = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Revisa cada 15 minutos (el mínimo que permite Android)
        fun programar(context: Context) {
            val solicitud = PeriodicWorkRequestBuilder<NuevasEntregasWorker>(15, TimeUnit.MINUTES)
                .setConstraints(conInternet)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                NOMBRE,
                ExistingPeriodicWorkPolicy.KEEP,
                solicitud
            )

            // Solo al correr desde Android Studio: una revisión extra a los 2 minutos
            // para poder probar sin esperar 15. En la app final no se ejecuta.
            val esDebug = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
            if (esDebug) {
                val prueba = OneTimeWorkRequestBuilder<NuevasEntregasWorker>()
                    .setInitialDelay(2, TimeUnit.MINUTES)
                    .setConstraints(conInternet)
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    NOMBRE_PRUEBA,
                    ExistingWorkPolicy.REPLACE,
                    prueba
                )
            }
        }

        // Al cerrar sesión
        fun cancelar(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(NOMBRE)
            WorkManager.getInstance(context).cancelUniqueWork(NOMBRE_PRUEBA)
        }
    }
}