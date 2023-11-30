package com.project.trux_application.model

import com.google.gson.annotations.SerializedName

data class User(
    var phone: String,
    @SerializedName("first_name") var firstName:  String,
    @SerializedName("last_name") var lastName : String,
    var userName: String,
    var email:String,
    @SerializedName("license_number")var licensenumber: String,
    var password : String,

)
