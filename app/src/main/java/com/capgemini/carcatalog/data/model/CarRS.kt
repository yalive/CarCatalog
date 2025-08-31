package com.capgemini.carcatalog.data.model

import com.capgemini.carcatalog.ui.cars.CarUiModel
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

fun CarRS.toCarUiModel(): CarUiModel {
    return CarUiModel(
        name = name.orEmpty(),
        description = description.orEmpty(),
        imageUrl = FAKE_IMAGES.random()
    )
}


private val FAKE_IMAGES = listOf(
    "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=300&q=80",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHiHb2u5jF10sEyjbFQcBU04HjBeYh77yDsQ&s",
    "https://uploads.vw-mms.de/system/production/images/vwn/037/486/images/9e9c8036e67cbf4bd5e6ae16596321d47e1a40dc/DB2021AU00889_web_1600.jpg?1649157860",
)