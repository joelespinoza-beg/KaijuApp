package com.example.kaijuapp_grupo4.repository

import com.example.kaijuapp_grupo4.data.MovimientoDao
import com.example.kaijuapp_grupo4.data.ProductoDao
import com.example.kaijuapp_grupo4.model.Movimiento
import com.example.kaijuapp_grupo4.model.Producto
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class InventarioRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao
) {
    val productos: Flow<List<Producto>> = productoDao.observarTodos()

    suspend fun agregarProducto(producto: Producto) {
        productoDao.insertar(producto)
        movimientoDao.insertar(crearMovimiento(producto.id, "ENTRADA", producto.cantidadDisponible))
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

