package com.kilagbe.kilagbe.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.data.Customer
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
            if (name_text.text.toString() != null && email_text.text.toString() != null && phone_text.text.toString() != null && password_text.text.toString() != null && confirm_password_text.text.toString() == password_text.text.toString()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email_text.text.toString(),
                    password_text.text.toString()
                )
                    .addOnSuccessListener {
                        val user = Customer(
                            name_text.text.toString(),
                            email_text.text.toString(),
                            phone_text.text.toString()
                        )
                        user.uid = it.user!!.uid
                        Log.d(
                            "Auth",
                            "Name = ${user.name}, email = ${user.email}, password = ${password_text.text.toString()}, phone = ${user.phone}, uid = ${user.uid}"
                        )
                        val dbref = FirebaseFirestore.getInstance().collection("customer")
                            .document("${user.uid.toString()}")
                        dbref.set(user)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "User registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
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
