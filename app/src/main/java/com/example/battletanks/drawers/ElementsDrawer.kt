package com.example.battletanks.drawers

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.battletanks.CELL_SIZE
import com.example.battletanks.R
import com.example.battletanks.enums.Material
import com.example.battletanks.models.Coordinate
import com.example.battletanks.models.Element

// класс, который запоминает выбранный материал и размещает его по координатам
class ElementsDrawer (val container: FrameLayout){
    var currentMaterial = Material.EMPTY
    private val elementsOnContainer = mutableListOf<Element>() // храним все добавленные на поле элементы

    private fun drawView(coordinate: Coordinate){
        val view = ImageView(container.context)
        val layoutParams = FrameLayout.LayoutParams(CELL_SIZE, CELL_SIZE)
        when (currentMaterial) {
            Material.EMPTY -> {

            }
            Material.BRICK -> view.setImageResource(R.drawable.brick)
            Material.CONCRETE -> view.setImageResource(R.drawable.concrete)
            Material.GRASS -> view.setImageResource(R.drawable.grass)
        }
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        val viewId = View.generateViewId()
        view.id = viewId
        view.layoutParams = layoutParams
        container.addView(view)
        elementsOnContainer.add(Element(viewId, currentMaterial, coordinate))
    }
}