package com.example.kaijuapp_grupo4.ui.components

import java.text.NumberFormat
import java.util.Locale

fun formatearPrecio(valor: Double): String =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL")).format(valor)
