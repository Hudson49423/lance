package com.octopusbeach.lance.activities

/**
 * Created by hudson on 2/11/16.
 */
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.firebase.client.AuthData
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.octopusbeach.lance.BaseApplication
import com.octopusbeach.lance.R

class LoginActivity: AppCompatActivity() {

    val TAG:String = LoginActivity::getLocalClassName.name

    private lateinit var login_button:Button
    private lateinit var emailField:EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_button =  findViewById(R.id.btn_login) as Button
        emailField =  findViewById(R.id.email_address) as EditText
        passwordField =  findViewById(R.id.password) as EditText

        login_button.setOnClickListener { authenticate() }
        findViewById(R.id.create_account)?.setOnClickListener { createAccount() }
    }


    fun authenticate() {
        Log.d(TAG, "Logging in...")

        if (!validate()) return

        login_button.isEnabled = false

        val dialog: ProgressDialog = ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog)

        dialog.isIndeterminate = true
        dialog.setMessage("Logging in...")
        dialog.show()

        val ref: Firebase = Firebase(BaseApplication.FIREBASE_ROOT)
        val handler: Firebase.AuthResultHandler = object:Firebase.AuthResultHandler {
            override fun onAuthenticated(authData:AuthData) {
                val map = mutableMapOf("provider" to authData.provider)
                ref.child("users").child(authData.uid).setValue(map)
                dialog.dismiss()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }

            override fun onAuthenticationError(firebaseError:FirebaseError) {
                dialog.dismiss()
                failure(firebaseError.message)
            }
        }

        ref.authWithPassword(emailField.text.toString(), passwordField.text.toString(), handler)
    }


    fun createAccount() {
        val i = Intent(this@LoginActivity, CreateAccountActivity::class.java)
        startActivityForResult(i, 0)
    }

    private fun failure(msg:String) {
        Snackbar.make(findViewById(R.id.linear), msg, Snackbar.LENGTH_SHORT).show()
        // Really should update the view with the real error.
        login_button.isEnabled = true
    }

    private fun validate():Boolean {
        val pass = passwordField.text.toString()
        val email = emailField.text.toString()
        var valid = true

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // email not correct.
            //emailField.error = "enter a valid email"
            emailField.error = "enter a valid email"
            valid = false
        } else emailField.error = null // got cleared up.

        if(pass.isEmpty() || pass.length < 5 || pass.length > 12) {
            passwordField.error = "must be between 5 and 12 characters"
            valid = false
        } else passwordField.error = null // got cleared up.

        return valid
    }



    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
