package com.kolosov.congratulations.data

import java.util.ArrayList

class CongratulationJson {
    var holidays: ArrayList<CongratulationModel>? = null
}

data class CongratulationModel(
    var name: String,
    var date: String,
    var description: String,
    var text: String
)
