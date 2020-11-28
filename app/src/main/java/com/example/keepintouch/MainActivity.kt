package com.example.keepintouch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        auth.signInAnonymously()
        setContentView(R.layout.activity_main)
        bottom_navigation.setupWithNavController(Navigation.findNavController(this, R.id.navHostFragment))
        bottom_navigation.setOnNavigationItemSelectedListener {
            item -> onNavDestinationSelected(item, Navigation.findNavController(this, R.id.navHostFragment))
        }
    }
}

