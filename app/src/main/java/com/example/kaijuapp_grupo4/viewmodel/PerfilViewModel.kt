package com.example.kaijuapp_grupo4.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaijuapp_grupo4.repository.EstadoDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val estadoDataStore = EstadoDataStore(application)

    val alertasActivas: StateFlow<Boolean> = estadoDataStore.obtenerAlertas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val fotoPerfil: StateFlow<Uri?> = estadoDataStore.obtenerFoto()
        .map { ruta -> ruta?.let { Uri.parse(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun alternarAlertas() {
        viewModelScope.launch {
            estadoDataStore.guardarAlertas(!alertasActivas.value)
        }
    }

    fun guardarFoto(uri: Uri) {
        viewModelScope.launch {
            estadoDataStore.guardarFoto(uri.toString())
        }
    }
}
