package com.example.kaijuapp_grupo4.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey val id: String = "",
    val productoId: String = "",
    val tipo: String = "",
    val cantidad: Int = 0,
    val fecha: String = ""
)