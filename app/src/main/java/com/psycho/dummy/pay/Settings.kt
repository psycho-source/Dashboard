package com.psycho.dummy.pay

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    object prefMan {
        var themeChanged = false
    }

    lateinit var closeBtn: ImageView
    lateinit var themeSwitch: Switch
    lateinit var accdel: TextView
    lateinit var diag: Dialog
    lateinit var accentChoose: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        val sp = this.getSharedPreferences("loginRec", Context.MODE_PRIVATE)
        val sharedPreferences = this.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("darkMode", false)
        val isLog = sp.getBoolean("isLog", false)
        if (isDark) {
            setTheme(R.style.SettingsThemeDark)
        } else {
            setTheme(R.style.SettingsTheme)
        }
        val accent = sharedPreferences.getInt("accent", 1)
        when (accent) {
            0 -> {
                theme.applyStyle(R.style.OverlayCyan, true)
            }
            1 -> {
                theme.applyStyle(R.style.OverlayBlue, true)
            }
            2 -> {
                theme.applyStyle(R.style.OverlayIndigo, true)
            }
            3 -> {
                theme.applyStyle(R.style.OverlayPurple, true)
            }
            4 -> {
                theme.applyStyle(R.style.OverlayRed, true)
            }
            5 -> {
                theme.applyStyle(R.style.OverlayPink, true)
            }
            6 -> {
                theme.applyStyle(R.style.OverlayOrange, true)
            }
            7 -> {
                theme.applyStyle(R.style.OverlayYellow, true)
            }
            8 -> {
                theme.applyStyle(R.style.OverlayTeal, true)
            }
            9 -> {
                theme.applyStyle(R.style.OverlayGreen, true)
            }
            10 -> {
                theme.applyStyle(R.style.OverlayGrey, true)
            }
            else -> {
                theme.applyStyle(R.style.OverlayBlue, true)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        closeBtn = findViewById(R.id.close)
        themeSwitch = findViewById(R.id.themeSwitch)
        accdel = findViewById(R.id.delAcc)
        accentChoose = findViewById(R.id.accentSpin)

        themeSwitch.isChecked = isDark

        themeSwitch.setOnClickListener {
            with(sharedPreferences.edit()) {
                putBoolean("darkMode", themeSwitch.isChecked)
                commit()
            }
            prefMan.themeChanged = true
            recreate()
        }

        accentSpin.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setCancelable(true)
            alert.setTitle("Accent Colors")
            val colors = resources.getStringArray(R.array.accent)
            alert.setItems(colors) { dialog, which ->
                with(sharedPreferences.edit()) {
                    putInt("accent", which)
                    commit()
                }
                prefMan.themeChanged = true
                recreate()

            }
            diag = alert.create()
            diag.window?.setBackgroundDrawableResource(R.drawable.diag_back)
            diag.show()
        }

        closeBtn.setOnClickListener {
            onBackPressed()
        }

        accdel.setOnClickListener {
            confirmDel()
        }

        if (!isLog) {
            accdel.visibility = View.GONE
        }
    }

    private fun confirmDel() {
        val alert = AlertDialog.Builder(this)
        alert.setCancelable(true)
        alert.setTitle("Delete Account")
        alert.setMessage("All your account information will be permanently removed. This action cannot be reversed. Are you sure?")
        alert.setPositiveButton("Yes") { dialog, which ->
            Toast.makeText(this, "Boom! Everything's gone", Toast.LENGTH_SHORT).show()
            diag.dismiss()
            //Logout and return to login screen
        }
        alert.setNegativeButton("No") { dialog, which ->
            diag.dismiss()
        }
        diag = alert.create()
        diag.window?.setBackgroundDrawableResource(R.drawable.diag_back)
        diag.show()
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
        super.onBackPressed()
    }
}
