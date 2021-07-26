package com.dmstechsolution.chatme

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {

   // private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment)
        val intent=Intent()
        val desti= intent.getStringExtra("destination")?.toInt()
        if (desti==R.id.userChat){
            findNavController(R.id.nav_host_fragment).navigate(desti)
        }
        //val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
       // val navView: NavigationView = findViewById(R.id.nav_view)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       // appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        //setupActionBarWithNavController(navController, appBarConfiguration)
       // navView.setupWithNavController(navController)

    }
/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
*/
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
    override fun onBackPressed() {
        val navcontrol=findNavController(R.id.nav_host_fragment)

        navcontrol.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id==R.id.splash){
                finishAffinity()
            }
            if (destination.id==R.id.login){
                finishAffinity()
            }
            if (destination.id==R.id.signup){
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_signup_to_login)
            }
            if (destination.id==R.id.onbording){
                finishAffinity()
            }
            if (destination.id==R.id.userChat){
                finishAffinity()
            }
            if (destination.id==R.id.profile2){
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_profile2_to_userChat)
            }
            if(destination.id==R.id.chat){
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_chat_to_userChat)
            }

            return@addOnDestinationChangedListener
        }
    }
}

