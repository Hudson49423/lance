package com.octopusbeach.lance.activities

import android.app.Fragment
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.firebase.client.AuthData
import com.firebase.client.Firebase
import com.octopusbeach.lance.BaseApplication
import com.octopusbeach.lance.R
import com.octopusbeach.lance.fragments.OverviewFragment
import com.octopusbeach.lance.fragments.ProjectFragment

/**
 * Created by hudson on 2/11/16.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var ref: Firebase
    private val fragments:Array<String> = arrayOf("Overview", "Projects", "Reports", "Logout")
    private lateinit var dList: ListView
    private lateinit var dLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dList = findViewById(R.id.left_drawer) as ListView
        dLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        toolbar = findViewById(R.id.toolbar) as Toolbar
        ref = Firebase(BaseApplication.FIREBASE_ROOT)
        var auth: AuthData? = ref.auth
        if (auth == null) {
            // not logged in.
            startActivity(Intent(this, LoginActivity::class.java))
            Log.e("Auth data null", "Logging out now")
        }

        setSupportActionBar(toolbar)

        dList.adapter = ArrayAdapter<String>(this, R.layout.drawer_list_item, fragments)
        dList.setOnItemClickListener { adapterView, view, position, l -> run {
            dLayout.closeDrawer(dList)
            Handler().postDelayed({ selectItem(position) }, 200)
        } }
        toggle = object: ActionBarDrawerToggle(this, dLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(v: View) {
                super.onDrawerClosed(v)
                invalidateOptionsMenu()
            }

            override fun onDrawerOpened(v: View) {
                super.onDrawerOpened(v)
                invalidateOptionsMenu()
            }
        };
        dLayout.setDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        selectItem(OverviewFragment.ID)
    }

    private fun selectItem(position:Int) {
        if(position == 3) { // Logging out position in list
            ref.unauth()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else {
            var frag: Fragment? = null
            when(position) {
                ProjectFragment.ID -> {
                    frag = ProjectFragment()
                }
                OverviewFragment.ID -> {
                    frag = OverviewFragment()
                }
            }
            if (frag == null) return
            // switch to new fragment.
            fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit()
            // update the title.
            title = fragments[position]
        }
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }
}