package com.octopusbeach.lance.model

import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hudson on 2/6/16.
 */
data class TimeEntry(val start: Date, val end:Date) {

    fun getTotal() = TimeUnit.MILLISECONDS.toMinutes(end.time - start.time)

    fun getStringTotal():String {
        val intTotal = getTotal()
        val hours = intTotal / 60
        val mins = intTotal % 60
        return "$hours : $mins"
    }
}