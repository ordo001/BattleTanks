package com.example.myapplication.drawers

import android.widget.FrameLayout
import com.example.myapplication.GameCore
import com.example.myapplication.activities.CELL_SIZE
import com.example.myapplication.sounds.MainSoundPlayer
//import com.example.myapplication.binding
import com.example.myapplication.enums.CELLS_TANKS_SIZE
import com.example.myapplication.enums.Direction
import com.example.myapplication.enums.Material.ENEMY_TANK
import com.example.myapplication.models.Coordinate
import com.example.myapplication.models.Element
import com.example.myapplication.models.Tank
import com.example.myapplication.sounds.GameSound
import com.example.myapplication.utils.checkIfChanceBiggerThanRandom
import com.example.myapplication.utils.drawElement

private const val MAX_ENEMY_AMOUNT = 20

class EnemyDrawer(
    private val container: FrameLayout,
    private val elements: MutableList<Element>,
    private val soundManager: MainSoundPlayer,
    private val gameCore: GameCore
) {
    private val respawnList: List<Coordinate>
    private var enemyAmount = 0
    private var currentCoordinate: Coordinate
    val tanks = mutableListOf<Tank>()
    lateinit var bulletDrawer: BulletDrawer
    private var gameStarted = false
    private var enemyMurders = 0

    init {
        respawnList = getRespawnList()
        currentCoordinate = respawnList[0]
    }

    private fun getRespawnList(): List<Coordinate> {
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0, 0))
        respawnList.add(
            Coordinate(
                0,
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
        if (index == respawnList.size) {
            index = 0
        }
        currentCoordinate = respawnList[index]
        val enemyTank = Tank(
            Element(
            material = ENEMY_TANK,
            coordinate = currentCoordinate
            ), Direction.DOWN,
            this
        )
        enemyTank.element.drawElement(container)
        tanks.add(enemyTank)
    }

    private fun moveEnemyTanks() {
        Thread(Runnable {
            while (true) {
                if (!gameCore.isPlaying())
                    continue
                goThroughAllTanks()
                Thread.sleep(400)
            }
        }).start()
    }

    private fun goThroughAllTanks() {
        if (tanks.isNotEmpty()) {
            soundManager.tankMove()
        } else {
            soundManager.tankStop()
        }

        tanks.toList().forEach {
            it.move(it.direction, container, elements)
            if (checkIfChanceBiggerThanRandom(10))
                bulletDrawer.addNewBulletForTank(it)
        }
    }

    fun startEnemyCreation() {
        if (gameStarted){
            return
        }
        gameStarted=true
        Thread(Runnable {
            while (enemyAmount < MAX_ENEMY_AMOUNT) {
                if (!gameCore.isPlaying())
                    continue
                drawEnemy()
                enemyAmount++
                Thread.sleep(3000)
            }
        }).start()
        moveEnemyTanks()
    }

    fun isAllTanksDestroyed():Boolean{
        return enemyMurders == MAX_ENEMY_AMOUNT
    }

    fun getPlayerScore() = enemyMurders * 100

    fun removeTank(tankIndex: Int) {
        tanks.removeAt(tankIndex)
        enemyMurders++
        if(isAllTanksDestroyed()){
            gameCore.playerWon(getPlayerScore())
        }
    }
}
