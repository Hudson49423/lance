package com.octopusbeach.lance.model

import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hudson on 2/6/16.
 */
data class TimeEntry(val start: Date, val end:Date, var dayOfYear:Int) {
    init {
        val now = Calendar.getInstance()
        now.time = end
        dayOfYear = now.get(Calendar.DAY_OF_YEAR)
    }

    fun getTotal():Int = TimeUnit.MILLISECONDS.toMinutes(end.time - start.time).toInt()

    fun getStringTotal():String {
        val intTotal = getTotal()
        val hours = intTotal / 60
        val mins = intTotal % 60
        return "$hours : $mins"
    }
}