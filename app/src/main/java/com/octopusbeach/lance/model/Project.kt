package com.octopusbeach.successtrack.model

import com.octopusbeach.lance.model.TimeEntry
import java.util.*

/**
 * Created by hudson on 2/6/16.
 */

data class Project(val title:String, val description:String) {
    var budget = 0.0
    var start:String = "" // default to now
    var end:String = ""// end date of project
    var wage = 0.0 // wage of user working on project
    var hours: ArrayList<TimeEntry> = arrayListOf()

    constructor(title:String, description: String, start:String, end:String, wage:Double, budget:Double):
    this(title, description){
        this.budget = budget
        this.wage = wage
        this.start = start
        this.end = end
    }

    fun weeklyHours():Int {
        val now = Calendar.getInstance()
        var total = 0
        for (date in hours)
            if (date.dayOfYear < now.get(Calendar.DAY_OF_YEAR)) total += date.getTotal()
        return total
    }
}
