package com.example.kaijuapp_grupo4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kaijuapp_grupo4.model.Movimiento
import com.example.kaijuapp_grupo4.model.Producto

@Database(entities = [Producto::class, Movimiento::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaiju_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL("INSERT INTO productos (id, nombre, categoria, precio, cantidadDisponible, stockMinimo, imagenUri) VALUES ('1', 'Polera Kaiju Classic', 'Ropa', 15990.0, 12, 5, NULL)")
                            db.execSQL("INSERT INTO productos (id, nombre, categoria, precio, cantidadDisponible, stockMinimo, imagenUri) VALUES ('2', 'Figura Godzilla 15cm', 'Coleccionables', 29990.0, 3, 5, NULL)")
                            db.execSQL("INSERT INTO productos (id, nombre, categoria, precio, cantidadDisponible, stockMinimo, imagenUri) VALUES ('3', 'Taza Logo Kaiju', 'Accesorios', 7990.0, 20, 10, NULL)")
                        }
                    })
                    .build()
                    .also { instancia = it }
            }
        }
    }
}