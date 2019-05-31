package com.psycho.dummy.pay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
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
    lateinit var mainnView: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        avatar = findViewById(R.id.userImage)
        name = findViewById(R.id.userName)
        settingIc = findViewById(R.id.settIcon)
        refBal = findViewById(R.id.refreshBalance)
        balance = findViewById(R.id.availBal)
        logout = findViewById(R.id.logoutButton)
        mainnView = findViewById(R.id.MainView)

        name.text = resources.getString(R.string.defUser, user.name.split(" ").first())

        balance.text = resources.getString(R.string.currBal, user.balance)

        logout.setOnClickListener {
            val sharedPreferences = this.getSharedPreferences("loginRec", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("em", " ")
                putString("cookie", " ")
                commit()
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        settingIc.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        refBal.setOnClickListener {
            //TODO: Wallet management
            Toast.makeText(this, "Manage Wallet(Add/remove)", Toast.LENGTH_SHORT).show()
        }

    }
}
