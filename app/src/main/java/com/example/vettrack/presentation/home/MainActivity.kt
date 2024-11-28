package com.example.vettrack.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.MainActivityLayoutBinding
import com.example.vettrack.presentation.authentication.login.LoginActivity
import com.example.vettrack.presentation.visits.register.RegisterVisitActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("MainActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)

        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNotificationPermission()

        setUpDrawer()
        initObservers()
        initViews()
    }

    private fun setUpDrawer() {
        Timber.d("MainActivity_TAG: setUpDrawer: ")
        setSupportActionBar(binding.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_visits,
                R.id.nav_schedule_visit,
                R.id.nav_pets
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_visits -> {
                    binding.fab.apply {
                        visibility = View.VISIBLE

                        setOnClickListener {
                            val intent =
                                Intent(this@MainActivity, RegisterVisitActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

                R.id.nav_schedule_visit -> {
                    binding.fab.apply {
                        visibility = View.GONE
                    }
                }

                R.id.nav_pets -> {
                    binding.fab.apply {
                        visibility = View.VISIBLE

                        setOnClickListener {
                            Toast.makeText(
                                this@MainActivity,
                                "NAVIGATE TO REGISTER PET",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }
    }

    //region TOOLBAR MENU
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                displayLogoutDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayLogoutDialog() {
        Timber.d("MainActivity_TAG: displayLogoutDialog: ")
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.txt_logout))
            .setMessage(getString(R.string.txt_logout_message))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.txt_accept)) { _, _ ->
                viewModel.signOut()
            }
            .setNegativeButton(getString(R.string.txt_cancel)) { _, _ -> }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(resources.getColor(R.color.md_theme_primary, null))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                ?.setTextColor(resources.getColor(R.color.md_theme_primary, null))
        }

        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    //endregion

    private fun initObservers() {
        Timber.d("MainActivity_TAG: initObservers: ")
        viewModel.signOutSuccess.observe(this) { signOutSuccess ->
            if (signOutSuccess) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initViews() {
        Timber.d("MainActivity_TAG: initViews: ")
        try {
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.textView).let {
                it.text = Session.user?.email
            }
        } catch (e: Exception) {
            Timber.d("MainActivity_TAG: initViews: set email error in lateral menu: ${e.message}")
        }
    }

    //region NOTIFICATION PERMISSIONS
    private fun checkNotificationPermission() {
        Timber.d("MainActivity_TAG: checkNotificationPermission: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("MainActivity_TAG: askNotificationPermission: PERMISSION GRANTED")
            } else {
                Timber.d("MainActivity_TAG: askNotificationPermission: PERMISSION DENIED")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            val unwrappedDrawable =
                AppCompatResources.getDrawable(this, android.R.drawable.ic_dialog_alert)
            val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
            if (wrappedDrawable != null)
                DrawableCompat.setTint(
                    wrappedDrawable,
                    resources.getColor(R.color.md_theme_primary, null)
                )

            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.txt_notification_permission))
                .setMessage(getString(R.string.txt_message_ask_notification_permission))
                .setIcon(wrappedDrawable)
                .setPositiveButton(getString(R.string.txt_go_to_settings)) { _, _ ->
                    navigateSettings()
                }.show()
        }
    }

    private fun navigateSettings() {
        Timber.d("MainActivity_TAG: navigateSettings: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val settingsIntent: Intent =
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(settingsIntent)
        }
    }
    //endregion
}