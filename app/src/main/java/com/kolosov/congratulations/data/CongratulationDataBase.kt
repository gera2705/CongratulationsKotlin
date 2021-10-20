package com.kolosov.congratulations.data

import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.kolosov.congratulations.data.dao.CongratulationDao
import com.kolosov.congratulations.data.entitys.Congratulation
import com.kolosov.congratulations.data.entitys.Favorites
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.concurrent.Executors

@Database(entities = [Congratulation::class , Favorites::class], version = 1, exportSchema = false)
abstract class CongratulationDataBase : RoomDatabase() {
    abstract fun congratulationDao(): CongratulationDao

    companion object {
        private var INSTANCE: CongratulationDataBase? = null
        fun getDbInstance(context: Context): CongratulationDataBase? {
            if (INSTANCE == null) {
                Log.d("DBcreate", "create")
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CongratulationDataBase::class.java, "CONGRATULATIONS_BD"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                var json = ""
                                try {
                                    val `is` =
                                        context.assets.open("testCong.json")
                                    val size = `is`.available()
                                    val buffer = ByteArray(size)
                                    `is`.read(buffer)
                                    `is`.close()
                                    json = String(buffer, StandardCharsets.UTF_8)
                                } catch (ex: IOException) {
                                    ex.printStackTrace()
                                }
                                Log.d("STASTTIME1", "1")
                                val gson = Gson()
                                val congratulationJson: CongratulationJson = gson.fromJson(
                                    json,
                                    CongratulationJson::class.java
                                )
                                val congratulations: ArrayList<CongratulationModel>? =
                                    congratulationJson.holidays
                                Log.d("ENDTTIME1", "2")
                                Log.d("LOG", congratulations.toString())



                                if (congratulations != null) {
                                    for (c in congratulations) {
                                        try {
                                            getDbInstance(context)!!
                                                .congratulationDao().insertCongratulation(
                                                    Congratulation(
                                                        c.name,
                                                        c.date,
                                                        c.description,
                                                        c.text
                                                    )
                                                )
                                        } catch (e: SQLiteException) {
                                            Log.d("DBex", c.name)
                                        }
                                    }
                                }
                                Log.d("ENDTTIME2", "2")
                            }
                        }
                    })
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}