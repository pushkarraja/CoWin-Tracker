package com.pushkarraja.vaccinetracker

data class CenterRVModal (
    val centerName: String,
    val centerAddress : String,
    val centerFromTime : String,
    val centerToTime : String,
    val fee_type : String,
    val ageLimit : Int,
    val vaccineName : String,
    val slots1 : Int,
    val slots2: Int
)