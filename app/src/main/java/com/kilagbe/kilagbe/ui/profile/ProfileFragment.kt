package com.kilagbe.kilagbe.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.kilagbe.kilagbe.R
import com.kilagbe.kilagbe.ui.auth.LoginActivity

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val username_text = root.findViewById<TextView>(R.id.username)
        val useremail_text = root.findViewById<TextView>(R.id.useremail)
        val userphone_text = root.findViewById<TextView>(R.id.userphone)

        val logout_button = root.findViewById<Button>(R.id.logout_button)
        val username_edit_button = root.findViewById<Button>(R.id.username_edit)
        val useremail_edit_button = root.findViewById<Button>(R.id.useremail_edit)
        val userpass_edit_button = root.findViewById<Button>(R.id.userpass_edit)
        val userphone_edit_button = root.findViewById<Button>(R.id.userphone_edit)

        val user = FirebaseAuth.getInstance().currentUser

        logout_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.and(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        FirebaseFirestore.getInstance().collection("customer").document("${user!!.uid}").get()
            .addOnSuccessListener {
                if ( it.exists() )
                    userphone_text.text = it.get("phone").toString()
                else
                    userphone_text.text = "Not found"
            }
            .addOnFailureListener {
                Log.d("LOL", "${it.message}")
            }

        username_text.text = user!!.displayName
        useremail_text.text = user!!.email


        username_edit_button.setOnClickListener {
            val dialog = AlertDialog.Builder(activity).create()
            val dialogview = inflater.inflate(R.layout.edit_dialog, null)
            dialogview.findViewById<Button>(R.id.confirm_button).setOnClickListener {
                if ( dialogview.findViewById<EditText>(R.id.edit).text.toString() == dialogview.findViewById<EditText>(R.id.edit_confirm).text.toString() && !dialogview.findViewById<EditText>(R.id.edit).text.toString().isEmpty())
                {
                    user.reauthenticate(EmailAuthProvider.getCredential(dialogview.findViewById<EditText>(R.id.edit_email).text.toString(), dialogview.findViewById<EditText>(R.id.edit_password).text.toString()))
                        .addOnSuccessListener {
                            changeProfileName(dialogview.findViewById<EditText>(R.id.edit).text.toString(), user)
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else
                {
                    Toast.makeText(activity, "Not matching", Toast.LENGTH_SHORT).show()
                }
            }
            dialogview.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                dialog.dismiss()
            }
            dialog.setView(dialogview)
            dialog.setCancelable(true)
            dialog.show()
        }

        userpass_edit_button.setOnClickListener {
            val dialog = AlertDialog.Builder(activity).create()
            val dialogview = inflater.inflate(R.layout.edit_dialog, null)
            dialogview.findViewById<Button>(R.id.confirm_button).setOnClickListener {
                if ( dialogview.findViewById<EditText>(R.id.edit).text.toString() == dialogview.findViewById<EditText>(R.id.edit_confirm).text.toString() && !dialogview.findViewById<EditText>(R.id.edit).text.toString().isEmpty() )
                {
                    user.reauthenticate(EmailAuthProvider.getCredential(dialogview.findViewById<EditText>(R.id.edit_email).text.toString(), dialogview.findViewById<EditText>(R.id.edit_password).text.toString()))
                        .addOnSuccessListener {
                            changeProfilePassword(dialogview.findViewById<EditText>(R.id.edit).text.toString(), user)
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else
                {
                    Toast.makeText(activity, "Not matching", Toast.LENGTH_SHORT).show()
                }
            }
            dialogview.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                dialog.dismiss()
            }
            dialog.setView(dialogview)
            dialog.setCancelable(true)
            dialog.show()
        }

        useremail_edit_button.setOnClickListener {
            val dialog = AlertDialog.Builder(activity).create()
            val dialogview = inflater.inflate(R.layout.edit_dialog, null)
            dialogview.findViewById<Button>(R.id.confirm_button).setOnClickListener {
                if ( dialogview.findViewById<EditText>(R.id.edit).text.toString() == dialogview.findViewById<EditText>(R.id.edit_confirm).text.toString() && !dialogview.findViewById<EditText>(R.id.edit).text.toString().isEmpty() )
                {
                    user.reauthenticate(EmailAuthProvider.getCredential(dialogview.findViewById<EditText>(R.id.edit_email).text.toString(), dialogview.findViewById<EditText>(R.id.edit_password).text.toString()))
                        .addOnSuccessListener {
                            changeProfileEmail(dialogview.findViewById<EditText>(R.id.edit).text.toString(), user)
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else
                {
                    Toast.makeText(activity, "Not matching", Toast.LENGTH_SHORT).show()
                }
            }
            dialogview.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                dialog.dismiss()
            }
            dialog.setView(dialogview)
            dialog.setCancelable(true)
            dialog.show()
        }

        userphone_edit_button.setOnClickListener {
            val dialog = AlertDialog.Builder(activity).create()
            val dialogview = inflater.inflate(R.layout.edit_dialog, null)
            dialogview.findViewById<Button>(R.id.confirm_button).setOnClickListener {
                if ( dialogview.findViewById<EditText>(R.id.edit).text.toString() == dialogview.findViewById<EditText>(R.id.edit_confirm).text.toString() && !dialogview.findViewById<EditText>(R.id.edit).text.toString().isEmpty() )
                {
                    user.reauthenticate(EmailAuthProvider.getCredential(dialogview.findViewById<EditText>(R.id.edit_email).text.toString(), dialogview.findViewById<EditText>(R.id.edit_password).text.toString()))
                        .addOnSuccessListener {
                            changeProfilePhone(dialogview.findViewById<EditText>(R.id.edit).text.toString(), user)
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else
                {
                    Toast.makeText(activity, "Not matching", Toast.LENGTH_SHORT).show()
                }
            }
            dialogview.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                dialog.dismiss()
            }
            dialog.setView(dialogview)
            dialog.setCancelable(true)
            dialog.show()
        }
        return root
    }

    fun changeProfileEmail(email: String, user: FirebaseUser) {
        user!!.updateEmail(email)
            .addOnSuccessListener {
                Toast.makeText(activity, "Email changed successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun changeProfileName(username: String, user: FirebaseUser?)
    {
        val upds = UserProfileChangeRequest.Builder().setDisplayName(username).build()
        user!!.updateProfile(upds)
            .addOnSuccessListener {
                Toast.makeText(activity, "Username changed successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun changeProfilePassword(password: String, user: FirebaseUser?)
    {
        user!!.updatePassword(password)
            .addOnSuccessListener {
                Toast.makeText(activity, "Password changed successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun changeProfilePhone(phone: String, user: FirebaseUser?)
    {
        FirebaseFirestore.getInstance().collection("customer").document("${user!!.uid}").update("phone", phone)
            .addOnSuccessListener {
                Toast.makeText(activity, "Phone number changed successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
