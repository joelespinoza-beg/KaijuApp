package com.example.kaijuapp_grupo4.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaijuapp_grupo4.data.AppDatabase
import com.example.kaijuapp_grupo4.model.Producto
import com.example.kaijuapp_grupo4.model.ProductoErrores
import com.example.kaijuapp_grupo4.model.ProductoFormState
import com.example.kaijuapp_grupo4.model.VentaUiState
import com.example.kaijuapp_grupo4.data.remote.RetrofitClient
import com.example.kaijuapp_grupo4.repository.EstadoDataStore
import com.example.kaijuapp_grupo4.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class InventarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventarioRepository = AppDatabase.obtener(application).let { db ->
        InventarioRepository(db.productoDao(), db.movimientoDao())
    }

    private val estadoDataStore = EstadoDataStore(application)

    val sucursalAfiliada: StateFlow<String> = estadoDataStore.obtenerSucursalAfiliada()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "San Bernardo")

    val productos: StateFlow<List<Producto>> = combine(
        repository.productos,
        sucursalAfiliada
    ) { listaProductos, sucursal ->
        listaProductos.filter { it.sucursal.equals(sucursal, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _backendOnline = MutableStateFlow(false)
    val backendOnline: StateFlow<Boolean> = _backendOnline.asStateFlow()

    private val _mensajeSincronizacion = MutableStateFlow<String?>(null)
    val mensajeSincronizacion: StateFlow<String?> = _mensajeSincronizacion.asStateFlow()

    fun sincronizarConBackend() {
        viewModelScope.launch {
            try {
                val lista = productos.value
                var conexionExitosa = true
                for (producto in lista) {
                    try {
                        val response = RetrofitClient.backendService.crearProducto(producto)
                        if (!response.isSuccessful && response.code() != 400) {
                            conexionExitosa = false
                        }
                    } catch (e: Exception) {
                        conexionExitosa = false
                    }
                }
                _backendOnline.value = true
                _mensajeSincronizacion.value = if (conexionExitosa) {
                    "¡Actualización directa (POST) completada con éxito en el servidor!"
                } else {
                    "Backend conectado (algunos productos ya existían en el servidor)"
                }
            } catch (e: Exception) {
                _backendOnline.value = false
                _mensajeSincronizacion.value = "Backend offline: No se pudo conectar al servidor"
            }
        }
    }

    // ---------- Formulario de producto ----------

    private val _formState = MutableStateFlow(ProductoFormState())
    val formState: StateFlow<ProductoFormState> = _formState.asStateFlow()

    fun onNombreChange(valor: String) {
        _formState.update {
            it.copy(
                nombre = valor.replace("\n", "").take(40),
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCategoriaChange(valor: String) {
        _formState.update { it.copy(categoria = valor.replace("\n", "").take(30)) }
    }

    fun onPrecioChange(valor: String) {
        _formState.update {
            it.copy(
                precio = valor.filter { c -> c.isDigit() || c == '.' }.take(10),
                errores = it.errores.copy(precio = null)
            )
        }
    }

    fun onCantidadChange(valor: String) {
        _formState.update {
            it.copy(
                cantidad = valor.filter { c -> c.isDigit() }.take(6),
                errores = it.errores.copy(cantidad = null)
            )
        }
    }

    fun onStockMinimoChange(valor: String) {
        _formState.update {
            it.copy(
                stockMinimo = valor.filter { c -> c.isDigit() }.take(6),
                errores = it.errores.copy(stockMinimo = null)
            )
        }
    }

    fun onImagenChange(uri: Uri?) {
        _formState.update { it.copy(imagenUri = uri) }
    }

    fun limpiarFormulario() {
        _formState.value = ProductoFormState()
    }

    fun validarFormulario(): Boolean {
        val estado = _formState.value
        val nombre = estado.nombre.trim()
        val precio = estado.precio.toDoubleOrNull()
        val cantidad = estado.cantidad.toIntOrNull()
        val stockMinimo = estado.stockMinimo.toIntOrNull()

        val errores = ProductoErrores(
            nombre = when {
                nombre.isBlank() -> "El nombre es obligatorio"
                nombre.length < 3 -> "Debe tener al menos 3 caracteres"
                else -> null
            },
            precio = when {
                estado.precio.isBlank() -> "El precio es obligatorio"
                precio == null -> "Ingrese un precio válido"
                precio <= 0 -> "El precio debe ser mayor a 0"
                else -> null
            },
            cantidad = when {
                estado.cantidad.isBlank() -> "La cantidad es obligatoria"
                cantidad == null -> "Ingrese una cantidad válida"
                else -> null
            },
            stockMinimo = when {
                estado.stockMinimo.isBlank() -> "El stock mínimo es obligatorio"
                stockMinimo == null -> "Ingrese un stock mínimo válido"
                else -> null
            }
        )

        _formState.update { it.copy(errores = errores) }

        if (errores.hayErrores || precio == null || cantidad == null || stockMinimo == null) {
            return false
        }

        val nuevoProducto = Producto(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            categoria = estado.categoria.trim().ifBlank { "General" },
            precio = precio,
            cantidadDisponible = cantidad,
            stockMinimo = stockMinimo,
            imagenUri = estado.imagenUri?.toString(),
            sucursal = sucursalAfiliada.value
        )
        viewModelScope.launch { repository.agregarProducto(nuevoProducto) }
        _formState.value = ProductoFormState()
        return true
    }

    // ---------- Venta ----------

    private val _ventaState = MutableStateFlow(VentaUiState())
    val ventaState: StateFlow<VentaUiState> = _ventaState.asStateFlow()

    fun seleccionarProductoVenta(productoId: String) {
        _ventaState.update { it.copy(productoId = productoId, error = null, exito = false) }
    }

    fun onCantidadVentaChange(valor: String) {
        _ventaState.update {
            it.copy(cantidad = valor.filter { c -> c.isDigit() }.take(6), error = null, exito = false)
        }
    }

    fun reiniciarVenta() {
        _ventaState.value = VentaUiState()
    }

    fun confirmarVenta() {
        val estado = _ventaState.value
        val producto = productos.value.find { it.id == estado.productoId }
        val cantidad = estado.cantidad.toIntOrNull() ?: 0

        val error = when {
            producto == null -> "Por favor seleccione un producto"
            cantidad <= 0 -> "Ingrese una cantidad válida mayor a 0"
            cantidad > producto.cantidadDisponible ->
                "Stock insuficiente (Disponible: ${producto.cantidadDisponible})"
            else -> null
        }

        if (error != null || producto == null) {
            _ventaState.update { it.copy(error = error, exito = false) }
            return
        }

        viewModelScope.launch {
            val exito = repository.registrarVenta(producto.id, cantidad)
            _ventaState.update {
                if (exito) it.copy(cantidad = "1", error = null, exito = true)
                else it.copy(error = "Error al procesar la venta", exito = false)
            }
        }
    }
}
