package com.project.trux_application.database


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.model.TruckDocsData


@Dao
interface DocumentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postPersonalDocuments(document: SavedDocuments)

    @Query("SELECT * FROM documents")
    fun getPersonalDocuments(): LiveData<List<SavedDocuments>>



}
