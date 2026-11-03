package com.example.kaijuapp_grupo4.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "preferencias_kaiju")

class EstadoDataStore(private val context: Context) {

    private val claveAlertas = booleanPreferencesKey("notificaciones_activadas")
    private val claveFotoPerfil = stringPreferencesKey("foto_perfil")

    suspend fun guardarAlertas(activo: Boolean) {
        context.dataStore.edit { prefs -> prefs[claveAlertas] = activo }
    }

    fun obtenerAlertas(): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[claveAlertas] ?: false }

    suspend fun guardarFoto(uri: String) {
        context.dataStore.edit { prefs -> prefs[claveFotoPerfil] = uri }
    }

    fun obtenerFoto(): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[claveFotoPerfil] }
}