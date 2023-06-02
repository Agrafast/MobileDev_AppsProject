package com.agrafast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.agrafast.data.local.entity.PlantEntity

@Dao
interface PlantDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertToMyPlant(plants : PlantEntity)
}