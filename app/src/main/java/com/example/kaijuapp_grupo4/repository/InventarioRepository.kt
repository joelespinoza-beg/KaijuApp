package com.example.kaijuapp_grupo4.repository

import android.util.Log
import com.example.kaijuapp_grupo4.data.MovimientoDao
import com.example.kaijuapp_grupo4.data.ProductoDao
import com.example.kaijuapp_grupo4.data.remote.BackendApiService
import com.example.kaijuapp_grupo4.data.remote.RetrofitClient
import com.example.kaijuapp_grupo4.model.Movimiento
import com.example.kaijuapp_grupo4.model.Producto
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class InventarioRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao,
    private val backendApi: BackendApiService = RetrofitClient.backendService
) {
    val productos: Flow<List<Producto>> = productoDao.observarTodos()

    suspend fun agregarProducto(producto: Producto) {
        productoDao.insertar(producto)
        movimientoDao.insertar(crearMovimiento(producto.id, "ENTRADA", producto.cantidadDisponible))
        try {
            val response = backendApi.crearProducto(producto)
            if (!response.isSuccessful) {
                Log.w("InventarioRepository", "Error backend al crear producto: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("InventarioRepository", "Excepción creando producto en backend: ${e.message}", e)
        }
    }

    suspend fun insertarLocal(producto: Producto) {
        productoDao.insertar(producto)
    }

    suspend fun obtenerTodosLocales(): List<Producto> {
        return productoDao.obtenerTodosDirecto()
    }

    suspend fun registrarVenta(productoId: String, cantidad: Int): Boolean {
        val filasModificadas = productoDao.descontarStock(productoId, cantidad)
        if (filasModificadas == 0) return false
        movimientoDao.insertar(crearMovimiento(productoId, "VENTA", cantidad))
        return true
    }

    private fun crearMovimiento(productoId: String, tipo: String, cantidad: Int): Movimiento {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-CL")).format(Date())
        return Movimiento(
            id = UUID.randomUUID().toString(),
            productoId = productoId,
            tipo = tipo,
            cantidad = cantidad,
            fecha = fecha
        )
    }
}

