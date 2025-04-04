package com.example.myapplication.drawers

import android.widget.FrameLayout
import com.example.myapplication.CELL_SIZE
import com.example.myapplication.binding
import com.example.myapplication.enums.CELLS_TANKS_SIZE
import com.example.myapplication.enums.Direction
import com.example.myapplication.enums.Material
import com.example.myapplication.models.Coordinate
import com.example.myapplication.models.Element
import com.example.myapplication.models.Tank
import com.example.myapplication.utils.drawElement

private  const val MAX_ENEMY_AMOUNT = 20
class EnemyDrawer (
    private val container: FrameLayout,
    private  val elements: MutableList<Element>
){

    private val respawnList: List<Coordinate>
    private var enemyAmount = 0
    private var currentCoordinate:Coordinate
    private val tanks = mutableListOf<Tank>()

    init {
        respawnList = getRespawnList()
        currentCoordinate = respawnList[0]
    }

    private fun getRespawnList(): List<Coordinate>{
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0,0))
        respawnList.add(
            Coordinate(0,
                ((container.width - container.width % CELL_SIZE) / CELL_SIZE -
                        (container.width - container.width % CELL_SIZE) / CELL_SIZE % 2) *
                        CELL_SIZE / 2 - CELL_SIZE * CELLS_TANKS_SIZE
            )
        )
        respawnList.add(
            Coordinate(
                0,
                (container.width - container.width % CELL_SIZE) - CELL_SIZE * CELLS_TANKS_SIZE
            )
        )
        return respawnList
    }

    private fun drawEnemy() {
        var index = respawnList.indexOf(currentCoordinate) + 1
        if(index == respawnList.size){
            index = 0
        }
        currentCoordinate = respawnList[index]
        val enemyTank = Tank(
            Element(
                material = Material.ENEMY_TANK,
                coordinate = currentCoordinate,
                width = Material.ENEMY_TANK.width,
                height = Material.ENEMY_TANK.height
            ), Direction.DOWN
        )
        enemyTank.element.drawElement(container)
        elements.add(enemyTank.element)
        tanks.add(enemyTank)
    }

    fun moveEnemyTank(){
        Thread(Runnable {
            while(true){
                removeInConsistentTanks()
                tanks.forEach{
                    it.move(it.direction, container, elements)
                }
                Thread.sleep(400)
            }
        }).start()
    }

    fun startEnemyCreation(){
        Thread(Runnable {
            while(enemyAmount < MAX_ENEMY_AMOUNT) {
                drawEnemy()
                enemyAmount++
                Thread.sleep(3000)
            }
        }).start()
    }

    private fun removeInConsistentTanks(){
        tanks.removeAll(getInConsistentTanks())

    }

    private  fun getInConsistentTanks() : List<Tank>{
        val removingTanks = mutableListOf<Tank>()
        val allTanksElements = elements.filter {it.material == Material.ENEMY_TANK}
        tanks.forEach{
            if(!allTanksElements.contains(it.element)){
                removingTanks.add(it)
            }
        }
        return  removingTanks
    }
}