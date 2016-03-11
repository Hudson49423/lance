package com.octopusbeach.lance

import android.app.Application
import com.firebase.client.Firebase
import com.squareup.leakcanary.LeakCanary

/**
 * Created by hudson on 2/11/16.
 */
class BaseApplication : Application() {

    companion object {
        val FIREBASE_ROOT = "https://lance.firebaseio.com"
    }
    override fun onCreate() {
        super.onCreate()
        Firebase.setAndroidContext(this)
        Firebase.getDefaultConfig().isPersistenceEnabled = true
        LeakCanary.install(this)
    }
}
