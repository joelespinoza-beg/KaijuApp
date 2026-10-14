package com.example.kaijuapp_grupo4.model

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val precio: Double = 0.0,
    val cantidadDisponible: Int = 0,
    val stockMinimo: Int = 5,
    val imagenUri: String? = null
)