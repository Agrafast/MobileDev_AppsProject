package com.agrafast.data.local

import androidx.room.RoomDatabase

//@Database(entities = [], version =  1 )
abstract class AppDatabase : RoomDatabase() {
  abstract fun plantDao(): PlantDao
}