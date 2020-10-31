package com.example.vkcloud.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vkcloud.R
import com.example.vkcloud.extensions.selectDestination
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController

    private val tokenExpirationHandler = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            navController.navigate(R.id.action_auth)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_documents,
            R.id.nav_albums,
            R.id.nav_profile
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupWithNavController(nav_view, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            nav_view.selectDestination(destination)
        }

        VK.removeTokenExpiredHandler(tokenExpirationHandler)
        VK.addTokenExpiredHandler(tokenExpirationHandler)

        if (savedInstanceState == null) {
            if (VK.isLoggedIn()) {
                navController.navigate(R.id.action_fragment_auth_to_nav_documents)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                navController.navigate(R.id.action_fragment_auth_to_nav_documents)
            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(this@MainActivity, R.string.auth_error, Toast.LENGTH_SHORT).show()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}