package com.unimech.UVIOT

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.unimech.UVIOT.QrActivity.Companion.PERMISSION_REQUEST_CODE


class LauncherScreen : AppCompatActivity() {
    private val TIME_OUT = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher_screen)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        if (!checkPermission()) {
            // permission not granted
            requestPermission();
        } else {
            var check = checkWifiOnAndConnected()
            if (check) {
                Toast.makeText(
                    this@LauncherScreen,
                    "Device is connected to wifi",
                    Toast.LENGTH_SHORT
                ).show()

                Handler().postDelayed(
                    {
                        startActivity(Intent(this@LauncherScreen, QrActivity::class.java))
                        finish()
                    }, TIME_OUT.toLong()
                )


            } else {
                Toast.makeText(
                    this@LauncherScreen,
                    "Device is not connected to wifi",
                    Toast.LENGTH_SHORT
                ).show()
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("Please connect to Device WiFi, and try again.")
                alertDialog.setPositiveButton("Ok, Understood") { _, _ ->
                    finish()
                }
                val createDialog: AlertDialog = alertDialog.create()
                // Set other dialog properties
                createDialog.setCancelable(false)
                createDialog.show()
            }
        }
    }

    private fun checkWifiOnAndConnected(): Boolean {
        val wifiMgr = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        return if (wifiMgr.isWifiEnabled) { // Wi-Fi adapter is ON
            val wifiInfo = wifiMgr.connectionInfo
            if (wifiInfo.networkId == -1) {
                false // Not connected to an access point
            } else{
                true
            }
            // Connected to an access point
        } else {
            false // Wi-Fi adapter is OFF
        }
    }

    // This is a function to check the permission for the camera, and its visibility is private
    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false
        }
        return true
    }
    // This is the function to perform tasks furing request for permissions and its visibility is private
    private fun requestPermission(): Unit {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT)
                    .show()

                // main logic
                if (!checkPermission()) {
                    // permission not granted
                    requestPermission();
                } else {
                    var check = checkWifiOnAndConnected()
                    if (check) {
                        Toast.makeText(
                            this@LauncherScreen,
                            "Device is connected to wifi",
                            Toast.LENGTH_SHORT
                        ).show()

                        Handler().postDelayed(
                            {
                                startActivity(Intent(this@LauncherScreen, QrActivity::class.java))
                                finish()
                            }, TIME_OUT.toLong()
                        )


                    } else {
                        Toast.makeText(
                            this@LauncherScreen,
                            "Device is not connected to wifi",
                            Toast.LENGTH_SHORT
                        ).show()
                        val alertDialog = AlertDialog.Builder(this)
                        alertDialog.setMessage("Please connect to Device WiFi, and try again.")
                        alertDialog.setPositiveButton("Ok, Understood") { _, _ ->
                            finish()
                        }
                        val createDialog: AlertDialog = alertDialog.create()
                        // Set other dialog properties
                        createDialog.setCancelable(false)
                        createDialog.show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel("You need to allow access permissions",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermission()
                                }
                            })
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        android.app.AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}