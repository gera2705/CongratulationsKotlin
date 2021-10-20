package com.kolosov.congratulations.data

import java.util.ArrayList


class CongratulationJson {
    var holidays: ArrayList<CongratulationModel>? = null

    fun setDictionaryWords(holidays: ArrayList<CongratulationModel>?) {
        this.holidays = holidays
    }
}


data class CongratulationModel(
    var name: String,
    var date: String,
    var description: String,
    var text: String
)
