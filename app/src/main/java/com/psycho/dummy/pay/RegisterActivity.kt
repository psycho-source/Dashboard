package com.psycho.dummy.pay

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mikhaellopez.circularimageview.CircularImageView
import java.math.BigInteger
import java.security.MessageDigest

//TODO: Convert all hardcoded strings to resources

class RegisterActivity : AppCompatActivity() {

    lateinit var avatar: CircularImageView
    lateinit var nameLayout: TextInputLayout
    lateinit var name: EditText
    lateinit var dobLayout: TextInputLayout
    lateinit var dob: EditText
    lateinit var gender: RadioGroup
    lateinit var emailLayout: TextInputLayout
    lateinit var email: EditText
    lateinit var passwordLayout: TextInputLayout
    lateinit var password: EditText
    lateinit var uidLayout: TextInputLayout
    lateinit var uid: EditText
    lateinit var cancel: CardView
    lateinit var register: MaterialButton
    var selGender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        avatar = findViewById(R.id.userImage)
        nameLayout = findViewById(R.id.nameLayout)
        name = findViewById(R.id.name)
        dobLayout = findViewById(R.id.dateLayout)
        dob = findViewById(R.id.dob)
        gender = findViewById(R.id.genderSel)
        emailLayout = findViewById(R.id.emailLayout)
        email = findViewById(R.id.userName)
        passwordLayout = findViewById(R.id.passLayout)
        password = findViewById(R.id.userPass)
        uidLayout = findViewById(R.id.uidLayout)
        uid = findViewById(R.id.userUid)
        cancel = findViewById(R.id.cancel)
        register = findViewById(R.id.create)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)

        cancel.setOnClickListener {
            onBackPressed()
        }

        dob.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    dob.setText("$dayOfMonth/" + (monthOfYear + 1).toString() + "/$year")
                }, year, month, date)
            datePickerDialog.show()
        }

        register.setOnClickListener {
            if (checkContents()) {
                register()
            }
        }

    }

    private fun checkContents(): Boolean {
        var err = 0
        nameLayout.isErrorEnabled = true
        dobLayout.isErrorEnabled = true
        emailLayout.isErrorEnabled = true
        passwordLayout.isErrorEnabled = true
        uidLayout.isErrorEnabled = true

        if (name.text.toString().isEmpty()) {
            nameLayout.error = "This Field cannot be empty"
            err++
        } else {
            nameLayout.isErrorEnabled = false
        }

        if (dob.text.toString().isEmpty()) {
            dobLayout.error = "This Field cannot be empty"
            err++
        } else {
            dobLayout.isErrorEnabled = false
        }

        if (email.text.toString().isEmpty()) {
            emailLayout.error = "This Field cannot be empty"
            err++
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            emailLayout.error = "Incorrect Email used"
            err++
        } else {
            emailLayout.isErrorEnabled = false
        }

        if (password.text.toString().isEmpty()) {
            passwordLayout.error = "This Field cannot be empty"
            err++
        } else {
            passwordLayout.isErrorEnabled = false
        }

        if (uid.text.toString().isEmpty()) {
            uidLayout.error = "This Field cannot be empty"
            err++
        } else if (uid.text.toString().length != 12) {
            uidLayout.error = "Incorrect UID number"
            err++
        } else {
            uidLayout.isErrorEnabled = false
        }

        val selectedId = gender.checkedRadioButtonId

        if (err == 0) {
            if (selectedId == -1) {
                Toast.makeText(this, "Please select your gender", Toast.LENGTH_LONG).show()
            } else {
                val gen = gender.findViewById<RadioButton>(selectedId)
                selGender = gen.text.toString()
                return true
            }
        }

        return false
    }

    fun register() {
        val queue = Volley.newRequestQueue(this)
        val url = " " //TODO: Your URL to registeration page
        val stringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            when (response) {
                "1" -> {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                "0" -> {
                    Toast.makeText(this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show()
                    emailLayout.isErrorEnabled = false
                    uidLayout.isErrorEnabled = false
                }
                "-41" -> {
                    emailLayout.isErrorEnabled = true
                    emailLayout.error = "Email already in use"
                }
                "-69" -> {
                    uidLayout.isErrorEnabled = true
                    uidLayout.error = "UID already registered"
                }
            }
        }, Response.ErrorListener {
            Log.e("Volley", it.message)
        }) {
            override fun getParams(): Map<String, String> {
                val myData = HashMap<String, String>()
                myData["name"] = name.text.toString()
                myData["uid"] = uid.text.toString()
                myData["dob"] = dob.text.toString()
                myData["gender"] = selGender
                myData["user"] = email.text.toString()
                myData["password"] = password.text.toString().sha256()
                return myData
            }
        }
        queue.add(stringRequest)
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

    override fun onBackPressed() {
        supportFinishAfterTransition()
        super.onBackPressed()
    }

}
