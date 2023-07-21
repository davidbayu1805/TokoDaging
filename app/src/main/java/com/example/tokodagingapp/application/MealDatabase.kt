package com.example.tokodagingapp.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tokodagingapp.dao.MealDao
import com.example.tokodagingapp.model.Meal

@Database(entities = [Meal::class], version = 2, exportSchema = false)
abstract class MealDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object{
        private var INSTANCE: MealDatabase? = null
        private val migration1To2: Migration = object: Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE meal_table ADD COLUMN latitude Double DEFAULT 0.0")
                database.execSQL("ALTER TABLE meal_table ADD COLUMN longitude Double DEFAULT 0.0")
            }

        }

        fun getDatabase(context: Context):MealDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database"
                )
                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE=instance
                instance
            }
        }
    }
}