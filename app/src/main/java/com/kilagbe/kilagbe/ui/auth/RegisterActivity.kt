package com.kilagbe.kilagbe.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.User
import com.kilagbe.kilagbe.ui.CustomerHome
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        login_text.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        register_button.setOnClickListener {
            if (name_text.text.toString().isNotEmpty() && email_text.text.toString().isNotEmpty() && phone_text.text.toString().isNotEmpty() && password_text.text.toString().isNotEmpty() && confirm_password_text.text.toString() == password_text.text.toString()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email_text.text.toString(),
                    password_text.text.toString()
                )
                    .addOnSuccessListener {
                        val user = User(
                            name_text.text.toString(),
                            email_text.text.toString(),
                            phone_text.text.toString()
                        )
                        user.uid = it.user!!.uid
                        val dbref = FirebaseFirestore.getInstance().collection("customer")
                            .document("${user.uid.toString()}")
                        dbref.set(user)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "User registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, CustomerHome::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.and(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "${it.message.toString()}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "${it.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Please enter all details correctly and ensure passwords match",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
