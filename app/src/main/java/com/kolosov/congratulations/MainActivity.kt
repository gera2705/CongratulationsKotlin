package com.kolosov.congratulations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

   private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CongratulationsKotlin)
        setContentView(R.layout.activity_main)

        this.window.statusBarColor = getColor(R.color.main_color)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        binding.menuNavigation.setupWithNavController(navController)
    }
}
