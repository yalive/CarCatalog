package com.capgemini.carcatalog.data.model

import com.google.gson.annotations.SerializedName

data class CarListRS(
    @SerializedName("Models")
    val models: List<CarRS>?
)


data class CarRS(
    @SerializedName("model_name")
    val name: String?,
    @SerializedName("model_make_id")
    val description: String?
)