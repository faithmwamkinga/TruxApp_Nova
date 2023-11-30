package com.project.trux_application.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("first_name") var firstname: String,
    @SerializedName("last_name") var lastname: String,
    var username: String,
    var email: String,
    var phone: String,
    @SerializedName("license_number") var licensenumber: String,
    var password: String,


)
