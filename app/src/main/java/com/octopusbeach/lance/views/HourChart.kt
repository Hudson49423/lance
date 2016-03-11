package com.octopusbeach.lance.views

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.octopusbeach.lance.R

/**
 * Created by hudson on 2/17/16.
 */

class HourChart: HorizontalBarChart {
    val labels = arrayOf("")  // never show labels

    internal constructor(ctx: Context): super(ctx) {
        setDefaults()
    }

    internal constructor(ctx:Context, attrs:AttributeSet): super(ctx, attrs) {
        setDefaults()
    }

    internal constructor(ctx:Context, attrs:AttributeSet, defStyle:Int): super(ctx, attrs, defStyle){
        setDefaults()
    }

    private fun setDefaults() {
        setTouchEnabled(false)
        legend.isEnabled = false
        axisRight.axisMaxValue = 60f
        axisLeft.axisMaxValue = 60f
        axisLeft.setDrawGridLines(false)
        axisLeft.setDrawLabels(false)
        setDescription("")
    }

    fun setHours(value:Float, color:Int) {
        val entry = listOf(BarEntry(value, 0))
        val set = BarDataSet(entry, "")
        set.color = color
        set.setDrawValues(false)
        val data = BarData(labels, set)
        this.data = data
        animateY(1500)
        notifyDataSetChanged()
    }
}
