package com.example.battletanks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.example.battletanks.enums.Direction.UP
import com.example.battletanks.enums.Direction.LEFT
import com.example.battletanks.enums.Direction.RIGHT
import com.example.battletanks.enums.Direction.DOWN
import com.example.battletanks.databinding.ActivityMainBinding
import com.example.battletanks.drawers.ElementsDrawer
import com.example.battletanks.drawers.GridDrawer
import com.example.battletanks.enums.Direction
import com.example.battletanks.enums.Material
import com.example.battletanks.models.Coordinate

const val CELL_SIZE = 50

lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var editMode = false // переменная, для показа в каком мы режиме находимся: редакта или игры
    private val gridDrawer by lazy {
        GridDrawer(binding.container)
    }

    private val elementsDrawer by lazy {
        ElementsDrawer(binding.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Menu"

        binding.editorClear.setOnClickListener { elementsDrawer.currentMaterial = Material.EMPTY }
        binding.editorBrick.setOnClickListener { elementsDrawer.currentMaterial = Material.BRICK }
        binding.editorConcrete.setOnClickListener {
            elementsDrawer.currentMaterial = Material.CONCRETE
        }
        binding.editorGrass.setOnClickListener { elementsDrawer.currentMaterial = Material.GRASS}
        binding.container.setOnTouchListener{ _, event ->
            elementsDrawer.onTouchContainer(event.x,event.y)
            return@setOnTouchListener true
        }
    }

    // метод, переключения режима, и удаления, либо рисования сетки
    private fun switchEditMode() {
        if (editMode) {
            gridDrawer.removeGrid()
            binding.materialsContainer.visibility = INVISIBLE
        } else {
            gridDrawer.drawGrid()
            binding.materialsContainer.visibility = VISIBLE
        }
        editMode = !editMode
    }

    //Наполнение меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    //Отслеживание какое меню выбрали
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_settings ->{
                gridDrawer.drawGrid()
                switchEditMode()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KEYCODE_DPAD_UP-> elementsDrawer.move(binding.myTank,UP)
            KEYCODE_DPAD_DOWN-> elementsDrawer.move(binding.myTank,DOWN)
            KEYCODE_DPAD_LEFT-> elementsDrawer.move(binding.myTank,LEFT)
            KEYCODE_DPAD_RIGHT-> elementsDrawer.move(binding.myTank,RIGHT)
        }
        return super.onKeyDown(keyCode, event)
    }


}