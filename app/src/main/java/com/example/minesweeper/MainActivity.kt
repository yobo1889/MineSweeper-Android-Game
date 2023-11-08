package com.example.minesweeper


import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var locateMines : List<IntArray>
    private lateinit var nonMines : List<IntArray>
    private lateinit var mine1: Button
    private lateinit var mine2: Button
    private lateinit var mine3: Button
    private lateinit var board: TextView
    private var flaggedMine: Int = 0
    private var isFlag : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        board = findViewById(R.id.board)
        locateMines = randomizeMines()
        nonMines = noMines()
        setUpMine()
        startGame()
    }
    private fun startGame(){
        for (field in nonMines) {
            val button = getField(field[0], field[1])
            button.setOnClickListener {
                if(isFlag){
                    lost()
                    return@setOnClickListener
                }
                if (countMines(field[0], field[1]) == 0) {
                    activateNeighbours(field[0], field[1])
                }
                else{
                    showCount(field[0],field[1])
                }
            }


        }
        val flag = findViewById<Switch>(R.id.flagStatus)
        flag.setOnCheckedChangeListener { _, isChecked ->
            isFlag = isChecked
        }
        val resetBut = findViewById<Button>(R.id.reset)
        resetBut.setOnClickListener{
            restartGame()
        }


    }
    private fun restartGame(){
        setContentView(R.layout.activity_main)
        board = findViewById(R.id.board)
        isFlag = false
        locateMines = randomizeMines()
        nonMines = noMines()
        setUpMine()
        startGame()
    }
    private fun activateNeighbours(x: Int, y: Int){
        val mineCount = countMines(x, y)
        val neighbors = getNeighboringButtons(x,y)
        if(mineCount==0){
            showCount(x,y)
            for (n in neighbors) {
                showCount(n[0],n[1])
            }
        }
        else{
            showCount(x, y)
        }
    }
    private fun showCount(x: Int, y: Int){
        val field= getField(x, y)
        val mineCount = countMines(x, y)
        field.text = mineCount.toString()

    }
    private fun countMines(x: Int, y: Int) : Int{
        var count = 0
        var mess = ""
        val neighbors = getNeighboringButtons(x,y)
        for (n in neighbors){
            for (m in locateMines){
                mess += "[" + n.joinToString(",") + "], "
                if(n.joinToString(",")==m.joinToString(",")) {
                    count++
                }
            }
        }
        return count

    }

    private fun noMines(): List<IntArray> {
        val matrix : List<IntArray> = locateMines
        val allCoordinates = (0 until 5).flatMap { row ->
            (0 until 5).map { col -> intArrayOf(row, col) }
        }

        return allCoordinates.filterNot { coord ->
            matrix.any { it.contentEquals(coord) }
        }
    }

    private fun setUpMine(){

        mine1.setOnClickListener {
            if(!isFlag){lost()}
            checkWin()
            mine1.text = getString(R.string.mine)
            flaggedMine++
        }
        mine2.setOnClickListener {
            if(!isFlag){lost()}
            checkWin()
            mine2.text = getString(R.string.mine)
            flaggedMine++

        }
        mine3.setOnClickListener {
            if(!isFlag){lost()}
            checkWin()
            mine3.text = getString(R.string.mine)
            flaggedMine++
        }
    }
    private fun explodeMines(){
        mine1.text = getString(R.string.mine)
        mine2.text = getString(R.string.mine)
        mine3.text = getString(R.string.mine)
    }
    private fun checkWin(){
        if(flaggedMine==2){
            board.textSize = 25f
            board.setTypeface(null, Typeface.BOLD)
            board.text = getString(R.string.won)
            flaggedMine = 0
        }
    }
    private fun lost(){
        board.textSize = 25f
        board.setTypeface(null, Typeface.BOLD)
        board.text = getString(R.string.lost)
        explodeMines()
    }
    private fun randomizeMines(): List<IntArray>{
        val mineC1 = getRandomCoordinates()
        val mineC2 = getRandomCoordinates()
        val mineC3 = getRandomCoordinates()
        mine1 = getField(mineC1[0],mineC1[1])
        mine2 = getField(mineC2[0], mineC2[1])
        mine3 = getField(mineC3[0], mineC3[1])
        val mineLocation = mutableListOf<IntArray>()
        mineLocation.add(mineC1)
        mineLocation.add(mineC2)
        mineLocation.add(mineC3)
        return mineLocation
    }
    private fun getNeighboringButtons(row: Int, col: Int): List<IntArray> {
         val neighbors = mutableListOf<IntArray>()

        for (i in row - 1..row + 1) {
            for (j in col - 1..col + 1) {
                // Skip the center cell (the button itself)
                if (i == row && j == col) continue

                // Check if the neighboring cell is within the grid bounds (0-4)
                if (i in 0..4 && j in 0..4) {
                    neighbors.add(intArrayOf(i,j))
                }
            }
        }

        return neighbors
    }
    private fun getField(x: Int, y: Int): Button {
        return when (x to y) {
            0 to 0 -> findViewById(R.id.button_00)
            0 to 1 -> findViewById(R.id.button_01)
            0 to 2 -> findViewById(R.id.button_02)
            0 to 3 -> findViewById(R.id.button_03)
            0 to 4 -> findViewById(R.id.button_04)

            1 to 0 -> findViewById(R.id.button_10)
            1 to 1 -> findViewById(R.id.button_11)
            1 to 2 -> findViewById(R.id.button_12)
            1 to 3 -> findViewById(R.id.button_13)
            1 to 4 -> findViewById(R.id.button_14)

            2 to 0 -> findViewById(R.id.button_20)
            2 to 1 -> findViewById(R.id.button_21)
            2 to 2 -> findViewById(R.id.button_22)
            2 to 3 -> findViewById(R.id.button_23)
            2 to 4 -> findViewById(R.id.button_24)

            3 to 0 -> findViewById(R.id.button_30)
            3 to 1 -> findViewById(R.id.button_31)
            3 to 2 -> findViewById(R.id.button_32)
            3 to 3 -> findViewById(R.id.button_33)
            3 to 4 -> findViewById(R.id.button_34)

            4 to 0 -> findViewById(R.id.button_40)
            4 to 1 -> findViewById(R.id.button_41)
            4 to 2 -> findViewById(R.id.button_42)
            4 to 3 -> findViewById(R.id.button_43)
            4 to 4 -> findViewById(R.id.button_44)

            else -> Button(applicationContext) // Return a default button if the coordinates don't match
        }
    }
    private fun getRandomCoordinates(): IntArray {
        val row = Random.nextInt(5) // Random number between 0 and 4 (inclusive)
        val col = Random.nextInt(5) // Random number between 0 and 4 (inclusive)
        return intArrayOf(row, col)
    }

}

