package com.psycho.dummy.pay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.mikhaellopez.circularimageview.CircularImageView

class Dashboard : AppCompatActivity() {

    lateinit var avatar: CircularImageView
    lateinit var name: TextView
    lateinit var settingIc: ImageView
    lateinit var refBal: CardView
    lateinit var balance: TextView
    lateinit var logout: CardView
    lateinit var mainView: RelativeLayout
    lateinit var backCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = this.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("darkMode", false)
        if (isDark) {
            setTheme(R.style.DashThemeDark)
        } else {
            setTheme(R.style.DashTheme)
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
        setContentView(R.layout.activity_dashboard)

        avatar = findViewById(R.id.userImage)
        name = findViewById(R.id.userName)
        settingIc = findViewById(R.id.settIcon)
        refBal = findViewById(R.id.refreshBalance)
        balance = findViewById(R.id.availBal)
        logout = findViewById(R.id.logoutButton)
        mainView = findViewById(R.id.MainView)
        backCard = findViewById(R.id.contentView)

        name.text = resources.getString(R.string.defUser, user.name.split(" ").first())

        balance.text = resources.getString(R.string.currBal, user.balance)

        logout.setOnClickListener {
            val sharedPreferences = this.getSharedPreferences("loginRec", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("em", " ")
                putString("cookie", " ")
                putBoolean("isLog", false)
                commit()
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        settingIc.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            val p1: Pair<View, String> = Pair.create(backCard, "bgCard")
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1)
            startActivity(intent, optionsCompat.toBundle())
        }

        refBal.setOnClickListener {
            //TODO: Wallet management
            Toast.makeText(this, "Manage Wallet(Add/remove)", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        if (Settings.prefMan.themeChanged) {
            Settings.prefMan.themeChanged = false
            recreate()
        }
        super.onResume()
    }
}
