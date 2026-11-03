package com.example.kaijuapp_grupo4.data

import androidx.room.Dao
import androidx.room.Insert
import com.example.kaijuapp_grupo4.model.Movimiento

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertar(movimiento: Movimiento)
}
