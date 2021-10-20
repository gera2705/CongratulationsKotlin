package com.kolosov.congratulations.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kolosov.congratulations.data.entitys.Congratulation
import com.kolosov.congratulations.data.entitys.Favorites
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single


@Dao
interface CongratulationDao {

    @Query("SELECT name FROM congratulation")
    fun getAllNames() : List<String>

    @Query("SELECT * FROM congratulation WHERE name = :arg0")
    fun getCongratulation(arg0: String?): Congratulation

    @Query("SELECT * FROM congratulation WHERE date= :arg0")
    fun getAllCongratulationByDate(arg0: String?): List<Congratulation>

    @Query("SELECT name FROM congratulation WHERE date= :arg0")
    fun getAllCongratulationNameByDate(arg0: String?): List<String>

    @Insert
    fun insertCongratulation(vararg congratulations: Congratulation?)

    @Insert
    fun insertFavorites(text: Favorites)

    @Query("SELECT * FROM Favorites")
    fun getAllFavorites(): List<String>

    @Delete
    fun deleteFavorites(key: Favorites)

}