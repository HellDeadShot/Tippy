package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat


private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var enteredAmount: EditText
    private lateinit var PercentageBar: SeekBar
    private lateinit var TipPercentage: TextView
    private lateinit var TipAmountShow: TextView
    private lateinit var TotalAmountShow: TextView
    private lateinit var tvColor: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enteredAmount = findViewById(R.id.enteredAmount)
        PercentageBar = findViewById(R.id.PercentageBar)
        TipPercentage = findViewById(R.id.TipPercentage)
        TipAmountShow = findViewById(R.id.TipAmountShow)
        TotalAmountShow = findViewById(R.id.TotalAmountShow)
        tvColor = findViewById(R.id.tvColor)

        PercentageBar.progress = INITIAL_TIP_PERCENT
        TipPercentage.text = "$INITIAL_TIP_PERCENT%"
        updateColorOfTip(INITIAL_TIP_PERCENT)
        PercentageBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChange $progress")
                TipPercentage.text = "$progress%"
                computeTipAndTotal()
                updateColorOfTip(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        enteredAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged ${s.toString()}")
                computeTipAndTotal()
            }

        })

    }

    private fun updateColorOfTip(tipPercent: Int) {
        val tipcolor = when (tipPercent){
            in 0..9 -> "Poor"
            in 10..14->"Acceptable"
            in 15..19->"Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvColor.text= tipcolor
        //color
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/PercentageBar.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        )as Int
        tvColor.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        if (enteredAmount.text.isEmpty()) {
            TipAmountShow.text = ""
            TotalAmountShow.text = ""
            return
        }
        //1. get value
        val amount = enteredAmount.text.toString().toDouble()
        val tippercentag = PercentageBar.progress
        //2. compute the tip and total
        val gTipAmountShow = amount * tippercentag / 100
        val gTotalAmountShow = gTipAmountShow + amount
        //3. update the UI
        TipAmountShow.text = "%.2f".format(gTipAmountShow)
        TotalAmountShow.text = "%.2f".format(gTotalAmountShow)
    }
}