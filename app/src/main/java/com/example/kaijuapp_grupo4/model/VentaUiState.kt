package com.example.kaijuapp_grupo4.model

data class VentaUiState(
    val productoId: String? = null,
    val cantidad: String = "1",
    val error: String? = null,
    val exito: Boolean = false
)