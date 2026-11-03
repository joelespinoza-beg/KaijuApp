package com.example.kaijuapp_grupo4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kaijuapp_grupo4.navigation.AppNavigation
import com.example.kaijuapp_grupo4.ui.theme.KaijuApp_Grupo4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaijuApp_Grupo4Theme {
                AppNavigation()
            }
        }
    }
}