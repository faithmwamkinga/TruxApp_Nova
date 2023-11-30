package com.project.trux_application.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.TruckDocsData

@Database(entities = [CargoDocsData::class], version = 2)
abstract class CargoDocsDb:RoomDatabase(){
    abstract fun cargoDocsDao():CargoDocsDao

    companion object{
        private var database:CargoDocsDb? = null

        fun getDatabase(context: Context):CargoDocsDb{
            if (database== null){
                database = Room
                    .databaseBuilder(context, CargoDocsDb::class.java,"CargoDocsDb" )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database as CargoDocsDb
        }


    }
}