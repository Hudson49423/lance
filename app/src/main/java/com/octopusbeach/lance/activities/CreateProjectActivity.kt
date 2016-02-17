package com.octopusbeach.lance.activities

/**
 * Created by hudson on 2/11/16.
 */
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.firebase.client.AuthData
import com.firebase.client.Firebase
import com.octopusbeach.lance.BaseApplication
import com.octopusbeach.lance.R
import com.octopusbeach.successtrack.model.Project
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*

class CreateProjectActivity :AppCompatActivity(), DatePickerDialog.OnDateSetListener  {

    val START_TAG = "start"
    val END_TAG = "end"
    val PROJECT_CHILD = "projects"
    lateinit var startBtn: Button
    lateinit var endBtn: Button
    lateinit var createBtn: Button
    lateinit var toolbar:Toolbar
    lateinit var title:EditText
    lateinit var description:EditText
    lateinit var wage:EditText
    lateinit var budget:EditText


    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar);

        title = findViewById(R.id.title) as EditText
        description = findViewById(R.id.description) as EditText
        wage = findViewById(R.id.wage) as EditText
        budget = findViewById(R.id.budget) as EditText

        startBtn = findViewById(R.id.btn_start_date) as Button
        endBtn = findViewById(R.id.btn_end_date) as Button
        createBtn = findViewById(R.id.btn_create) as Button
        startBtn.setOnClickListener { createDialog(START_TAG) }
        endBtn.setOnClickListener { createDialog(END_TAG) }
        createBtn.setOnClickListener { createProject() }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                setResult(RESULT_CANCELED)
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createDialog(tag:String) {
        val now:Calendar = Calendar.getInstance()
        val dpd:DatePickerDialog = DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(fragmentManager, tag)
    }

    override fun onDateSet(view:DatePickerDialog, y:Int, m:Int, d:Int) {
        val date = "$m-$d-$y"
        when(view.tag) {
            START_TAG -> { startBtn.text = date }
            END_TAG -> { endBtn.text = date }
        }
    }

    private fun createProject() {
        if(!validate()) return // project must have a title

        // create a loading dialog.
        val dialog: ProgressDialog = ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog)

        dialog.isIndeterminate = true
        dialog.setMessage("Creating Project..")
        dialog.show()

        val rootRef:Firebase = Firebase(BaseApplication.FIREBASE_ROOT)

        // If we're not logged in go to the login activity.
        val authData:AuthData? = rootRef.auth

        if (authData == null) {
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        val project:Project = Project(title.getString(), description.getString())
        if (startBtn.text.toString().contains("-")) {
            project.start = startBtn.text.toString()
        } else {
            val now:Calendar = Calendar.getInstance()
            val year:Int = now.get(Calendar.YEAR)
            val month:Int = now.get(Calendar.MONTH)
            val day:Int = now.get(Calendar.DAY_OF_MONTH)
            project.start = "$month-$day-$year"
        }
        if (endBtn.text.toString().contains("-")) project.end = endBtn.text.toString()
        try {
            project.budget = budget.getString().toDouble()
        } catch(e:NumberFormatException) {
            project.budget = 0.0
        }
        try {
            project.wage = wage.getString().toDouble()
        } catch(e:NumberFormatException) {
            project.wage = 0.0
        }
        // Push the new project to firebase.
        val userRef = rootRef.child(PROJECT_CHILD).child(authData?.uid)
        userRef.push().setValue(project)
        // Go back to projects activity.
        dialog.dismiss()
        setResult(RESULT_OK)
        finish()
    }

    // Simple extension
    fun EditText.getString():String = text.toString()

    private fun validate():Boolean {
        var valid = true
        val titleText = title.getString()
        if (titleText.isEmpty()) {
            valid = false
            title.error = "Please enter a title"
        } else
            title.error = null
        val descriptionText = description.getString()
        if (descriptionText.isEmpty()) {
            valid = false
            description.error = "Please enter a description"
        } else description.error = null
        return valid
    }
}
