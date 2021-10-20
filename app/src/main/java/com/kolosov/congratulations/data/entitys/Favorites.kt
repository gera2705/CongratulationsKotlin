package com.kolosov.congratulations.data.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorites(
    @PrimaryKey
    var text: String
)
