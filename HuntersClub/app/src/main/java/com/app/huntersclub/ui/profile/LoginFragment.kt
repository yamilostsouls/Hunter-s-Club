package com.app.huntersclub.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val edUserName = view.findViewById<EditText>(R.id.edUserName)
        val edPasswd = view.findViewById<EditText>(R.id.edPasswd)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val forgotPassword = view.findViewById<TextView>(R.id.forgotPassword)

        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_passwordFragment)
        }

        btnLogin.setOnClickListener {
            val email = edUserName.text.toString().trim()
            val password = edPasswd.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Introduce correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                viewModel.preloadUserData()
            } else {
                Toast.makeText(context, "Login fallido", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.userDataLoaded.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        }
    }
}

