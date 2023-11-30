package com.project.trux_application.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.TruckDocsData

@Database(entities = [TruckDocsData::class], version = 2)
abstract class TruckDocumentDb:RoomDatabase() {
    abstract fun truckDocsDao():TruckDocumentDao

    companion object{
        private var database:TruckDocumentDb? = null

        fun getDatabase(context: Context):TruckDocumentDb{
            if (database== null){
                database = Room
                    .databaseBuilder(context, TruckDocumentDb::class.java,"TruckDocumentdb" )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database as TruckDocumentDb
        }


    }
}