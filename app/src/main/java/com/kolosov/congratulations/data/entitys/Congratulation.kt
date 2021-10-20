package com.kolosov.congratulations.data.entitys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Congratulation(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "congratulationText")
    var congratulationText: String
)
