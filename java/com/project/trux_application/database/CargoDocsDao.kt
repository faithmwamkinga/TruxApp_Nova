package com.project.trux_application.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.SavedDocuments

@Dao
interface CargoDocsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postCargoDocuments(cargoDocument: CargoDocsData)

    @Query("SELECT * FROM cargoDocuments")
    fun getCargoDocuments(): LiveData<List<CargoDocsData>>

}