package com.app.app_demo.models

data class ContactInfo(
    val name: String,
    val number: Set<String>,
    val photoUrl:String,
    var lastAct: String,
    var userType: Int
)