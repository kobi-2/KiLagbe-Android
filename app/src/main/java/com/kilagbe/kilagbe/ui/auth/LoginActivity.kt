package com.kilagbe.kilagbe.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kilagbe.kilagbe.BottomNavigationActivity
import com.kilagbe.kilagbe.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register_text.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            if ( email_text.text.toString() != null && password_text.text.toString() != null )
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email_text.text.toString(), password_text.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, BottomNavigationActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.and(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
