package com.project.trux_application.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.model.TruckDocsData

@Database(entities = [SavedDocuments::class], version = 2)
abstract class DocumentsDb: RoomDatabase(){
    abstract fun documentsDao():DocumentsDao

    companion object{
        private var database:DocumentsDb? = null

        fun getDatabase(context: Context):DocumentsDb{
            if (database== null){
                database = Room
                    .databaseBuilder(context, DocumentsDb::class.java,"DocumentsDb" )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database as DocumentsDb
        }


    }

}