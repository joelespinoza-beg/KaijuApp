package com.example.kaijuapp_grupo4.model


data class ProductoFormState(
    val nombre: String = "",
    val categoria: String = "",
    val precio: String = "",
    val cantidad: String = "",
    val stockMinimo: String = "5",
    val errores: ProductoErrores = ProductoErrores()
)


data class ProductoErrores(
    val nombre: String? = null,
    val precio: String? = null,
    val cantidad: String? = null
)