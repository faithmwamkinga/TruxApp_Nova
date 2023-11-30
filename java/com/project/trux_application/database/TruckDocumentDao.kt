package com.project.trux_application.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.trux_application.model.TruckDocsData

@Dao
interface TruckDocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postTruckDocuments(truckDocs: TruckDocsData)

    @Query("SELECT * FROM truckDocuments")
    fun getTruckDocuments(): LiveData<List<TruckDocsData>>
}