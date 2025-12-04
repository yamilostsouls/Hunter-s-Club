package com.app.huntersclub.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R
import com.google.firebase.auth.FirebaseAuth


class SlideshowFragment : Fragment() {

    private lateinit var viewModel: SlideshowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_slideshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SlideshowViewModel::class.java]
        //We check if user is logged in to skip log in
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_nav_slideshow_to_profileFragment)
            return
        }

        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val edUserName = view.findViewById<EditText>(R.id.edUserName)
        val edPasswd = view.findViewById<EditText>(R.id.edPasswd)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        //Button for the user to register in case it doesn't have an account
        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_nav_slideshow_to_registerFragment)
        }
        //Login check handled in SlideshowViewModel
        btnLogin.setOnClickListener {
            val email = edUserName.text.toString().trim()
            val password = edPasswd.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Introduce correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        //Login result
        viewModel.loginResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_nav_slideshow_to_profileFragment)
            } else {
                Toast.makeText(context, "Login fallido", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

