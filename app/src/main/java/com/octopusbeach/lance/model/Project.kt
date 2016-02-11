package com.octopusbeach.successtrack.model

/**
 * Created by hudson on 2/6/16.
 */

data class Project(val title:String, val description:String) {
    var budget = 0.0
    var start:String = "" // default to now
    var end:String = ""// end date of project
    var wage = 0.0 // wage of user working on project
}
