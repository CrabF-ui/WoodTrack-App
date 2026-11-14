package com.example.logistica.data.model

// Respuesta de GET /api/entregas
data class EntregasResponse(
    val data: List<Delivery>
)

// Respuesta de PATCH estado, incidencia y entregar
data class EntregaResponse(
    val data: Delivery
)

data class Delivery(
    val id: Int,
    val fecha_asignada: String,
    val orden: Int?,
    val estado: String,          // pendiente | en_camino | entregada | incidencia
    val estado_fecha_hora: String?,
    val notas: String?,
    val pedido: Pedido
)

data class Pedido(
    val id: Int,
    val cliente: String,
    val telefono: String?,
    val direccion: String,
    val fecha_entrega: String?,
    val productos: List<ProductoPedido>
)

data class ProductoPedido(
    val producto: String,
    val cantidad: Int
)