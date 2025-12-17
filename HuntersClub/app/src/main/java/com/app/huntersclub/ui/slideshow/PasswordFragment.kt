package com.app.huntersclub.ui.slideshow

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R
import com.google.firebase.auth.FirebaseAuth

class PasswordFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var btnResetPwd: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_password, container, false)

        emailInput = view.findViewById(R.id.edUserName)
        btnResetPwd = view.findViewById(R.id.btnResetPwd)

        btnResetPwd.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                emailInput.error = "Introduce tu correo"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Correo no válido"
                return@setOnClickListener
            }

            sendPasswordReset(email)
        }

        return view
    }

    private fun sendPasswordReset(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Toast.makeText(
                        requireContext(),
                        "Correo enviado. Revisa tu bandeja de entrada o de spam.",
                        Toast.LENGTH_LONG
                    ).show()

                    //Returns to login
                    findNavController().navigate(
                        R.id.action_passwordFragment_to_nav_slideshow
                    )

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}
