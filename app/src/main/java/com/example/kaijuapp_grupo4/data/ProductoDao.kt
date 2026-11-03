package com.example.kaijuapp_grupo4.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kaijuapp_grupo4.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun observarTodos(): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto)

    // Devuelve cuantas filas modifico: 1 = venta realizada, 0 = stock insuficiente o producto inexistente
    @Query(
        "UPDATE productos SET cantidadDisponible = cantidadDisponible - :cantidad " +
                "WHERE id = :id AND cantidadDisponible >= :cantidad"
    )
    suspend fun descontarStock(id: String, cantidad: Int): Int
}
