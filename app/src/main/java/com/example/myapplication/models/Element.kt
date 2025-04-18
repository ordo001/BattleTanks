package com.example.myapplication.models

import android.view.View
import com.example.myapplication.enums.Material

data class Element(
    val viewId: Int = View.generateViewId(),
    val material: Material,
    var coordinate: Coordinate,
    val width: Int = material.width,
    val height: Int = material.height
)
