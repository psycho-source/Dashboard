package com.psycho.dummy.pay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mikhaellopez.circularimageview.CircularImageView
import java.math.BigInteger
import java.security.MessageDigest

//TODO: Convert all hardcoded strings to resources

object user {
    var name = " "
    var uid = " "
    var dob = " "
    var gender = " "
    var balance = 0.00
}

class LoginActivity : AppCompatActivity() {

    lateinit var avatar: CircularImageView
    lateinit var changeAvatar: ImageView
    lateinit var emailLayout: TextInputLayout
    lateinit var passLayout: TextInputLayout
    lateinit var emailField: EditText
    lateinit var passField: EditText
    lateinit var logIn: MaterialButton
    lateinit var signUp: CardView
    lateinit var bgCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefer = this.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val isDark = sharedPrefer.getBoolean("darkMode", false)
        if (isDark) {
            setTheme(R.style.AppThemeDark)
        } else {
            setTheme(R.style.AppTheme)
        }
        val accent = sharedPrefer.getInt("accent", 1)
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
        val sharedPreferences = this.getSharedPreferences("loginRec", Context.MODE_PRIVATE)
        val em = sharedPreferences.getString("em", " ") ?: " "
        val cookie = sharedPreferences.getString("cookie", " ") ?: " "
        login(em, cookie, true)
        setContentView(R.layout.activity_login)

        avatar = findViewById(R.id.userImage)
        changeAvatar = findViewById(R.id.changeAvatar)
        emailLayout = findViewById(R.id.nameLayout)
        passLayout = findViewById(R.id.passLayout)
        emailField = findViewById(R.id.userName)
        passField = findViewById(R.id.userPass)
        logIn = findViewById(R.id.loginButton)
        signUp = findViewById(R.id.register)
        bgCard = findViewById(R.id.cardBG)

        logIn.setOnClickListener {
            val email: String? = emailField.text.toString()
            var pass = passField.text.toString()
            if (checkFields(email, pass)) {
                pass = pass.sha256()
                if (email != null)
                    login(email, pass)
            }
        }

        signUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            val p1: Pair<View, String> = Pair.create(bgCard, "bgCard")
            val p2: Pair<View, String> = Pair.create(avatar, "image")
            val p3: Pair<View, String> = Pair.create(logIn, "register")
            val p4: Pair<View, String> = Pair.create(signUp, "cancel")
            val p5: Pair<View, String> = Pair.create(emailLayout, "username")
            val p6: Pair<View, String> = Pair.create(passLayout, "password")
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3, p4, p5, p6)
            startActivity(intent, optionsCompat.toBundle())
        }

        changeAvatar.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            val p1: Pair<View, String> = Pair.create(bgCard, "bgCard")
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1)
            startActivity(intent, optionsCompat.toBundle())
        }

    }

    private fun String.sha256(): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(this.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hash = no.toString(16)
            while (hash.length < 32) {
                hash = "0$hash"
            }
            hash
        } catch (e: Exception) {
            throw Exception("Failed to encrypt the user password")
        }
    }

    private fun checkFields(email: String?, pswd: String?): Boolean {
        var err = 0
        emailLayout.isErrorEnabled = true
        passLayout.isErrorEnabled = true

        if (email.isNullOrEmpty()) {
            emailLayout.error = "Email Field Cannot be blank"
            err++
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Incorrect Email used"
            err++
        } else {
            emailLayout.isErrorEnabled = false
        }

        if (pswd.isNullOrEmpty()) {
            passLayout.error = "Password field cannot be empty"
            err++
        } else {
            passLayout.isErrorEnabled = false
        }

        return (err == 0)
    }

    fun login(email: String, pswd: String, isFirst: Boolean = false) {
        val queue = Volley.newRequestQueue(this)
        val url = "" //TODO: Your URL to registeration page
        val stringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            if (response != "0") {
                val sharedPreferences = this.getSharedPreferences("loginRec", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("em", email)
                    putString("cookie", pswd)
                    putBoolean("isLog", true)
                    commit()
                }
                val res = response.split("^")
                user.name = res[0]
                user.uid = res[1]
                user.dob = res[2]
                user.gender = res[3]
                user.balance = res[4].toDouble()
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            } else {
                if (!isFirst)
                    Toast.makeText(this, "Wrong Email/Password", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {
            Log.e("Volley", it.message)
        }) {
            override fun getParams(): Map<String, String> {
                val myData = HashMap<String, String>()
                myData["user"] = email
                myData["password"] = pswd
                return myData
            }
        }
        queue.add(stringRequest)
    }

    override fun onResume() {
        if (Settings.prefMan.themeChanged) {
            Settings.prefMan.themeChanged = false
            recreate()
        }
        super.onResume()
    }

}
