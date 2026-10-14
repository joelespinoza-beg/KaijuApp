package com.example.kaijuapp_grupo4.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kaijuapp_grupo4.model.Producto
import com.example.kaijuapp_grupo4.model.ProductoErrores
import com.example.kaijuapp_grupo4.model.ProductoFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class InventarioViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(
        listOf(
            Producto("1", "Polera Kaiju Classic", "Ropa", 15990.0, 12, 5),
            Producto("2", "Figura Godzilla 15cm", "Coleccionables", 29990.0, 3, 5),
            Producto("3", "Taza Logo Kaiju", "Accesorios", 7990.0, 20, 10)
        )
    )
    val productos: StateFlow<List<Producto>> = _productos

    private val _formState = MutableStateFlow(ProductoFormState())
    val formState: StateFlow<ProductoFormState> = _formState

    fun onNombreChange(valor: String) {
        _formState.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCategoriaChange(valor: String) {
        _formState.update { it.copy(categoria = valor) }
    }

    fun onPrecioChange(valor: String) {
        _formState.update { it.copy(precio = valor, errores = it.errores.copy(precio = null)) }
    }

    fun onCantidadChange(valor: String) {
        _formState.update { it.copy(cantidad = valor, errores = it.errores.copy(cantidad = null)) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _formState.value
        val errores = ProductoErrores(
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es obligatorio" else null,
            precio = if (estadoActual.precio.isBlank() || estadoActual.precio.toDoubleOrNull() == null) "Ingrese un precio válido" else null,
            cantidad = if (estadoActual.cantidad.isBlank() || estadoActual.cantidad.toIntOrNull() == null) "Ingrese una cantidad válida" else null
        )

        val hayErrores = listOfNotNull(errores.nombre, errores.precio, errores.cantidad).isNotEmpty()
        _formState.update { it.copy(errores = errores) }

        if (!hayErrores) {
            val nuevoProducto = Producto(
                id = (System.currentTimeMillis()).toString(),
                nombre = estadoActual.nombre,
                categoria = estadoActual.categoria.ifBlank { "General" },
                precio = estadoActual.precio.toDoubleOrNull() ?: 0.0,
                cantidadDisponible = estadoActual.cantidad.toIntOrNull() ?: 0,
                stockMinimo = estadoActual.stockMinimo.toIntOrNull() ?: 5
            )
            _productos.update { list -> list + nuevoProducto }
            _formState.value = ProductoFormState()
        }

        return !hayErrores
    }

    fun registrarVenta(productoId: String, cantidadVendida: Int): Boolean {
        val listaActual = _productos.value.toMutableList()
        val index = listaActual.indexOfFirst { it.id == productoId }

        if (index != -1) {
            val producto = listaActual[index]
            if (producto.cantidadDisponible >= cantidadVendida) {
                listaActual[index] = producto.copy(
                    cantidadDisponible = producto.cantidadDisponible - cantidadVendida
                )
                _productos.value = listaActual
                return true
            }
        }
        return false
    }

}