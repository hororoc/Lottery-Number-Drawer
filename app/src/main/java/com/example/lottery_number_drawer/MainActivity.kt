package com.example.lottery_number_drawer

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }
    private val clearButton: Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }
    private val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }
    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }
    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.textView1),
            findViewById<TextView>(R.id.textView2),
            findViewById<TextView>(R.id.textView3),
            findViewById<TextView>(R.id.textView4),
            findViewById<TextView>(R.id.textView5),
            findViewById<TextView>(R.id.textView6),
        )
    }
    private var didRun: Boolean = false
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initAddButton()
        initClearButton()
        initRunButton()
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.text = numberPicker.value.toString()
            textView.isVisible = true
            textView.background = getBackgroundByNumber(numberPicker.value)
            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.text = null
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            didRun = true
            val list = getRandomNumber()

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                textView.background = getBackgroundByNumber(number)
            }

            Log.d("MainActivity", list.toString())
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) continue
                    this.add(i)
                }
            }
        numberList.shuffle()

        val newList: List<Int> =
            pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
        return newList.sorted()
    }

    private fun getBackgroundByNumber(number: Int): Drawable {
        return when (number) {
            in 1..9 -> ContextCompat.getDrawable(this, R.drawable.circle_yellow)!!
            in 10..19 -> ContextCompat.getDrawable(this, R.drawable.circle_blue)!!
            in 20..29 -> ContextCompat.getDrawable(this, R.drawable.circle_red)!!
            in 30..39 -> ContextCompat.getDrawable(this, R.drawable.circle_gray)!!
            else -> ContextCompat.getDrawable(this, R.drawable.circle_green)!!
        }
    }
}