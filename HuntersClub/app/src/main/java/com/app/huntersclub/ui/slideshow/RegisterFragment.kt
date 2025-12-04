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


class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val edName = view.findViewById<EditText>(R.id.edName)
        val edEmail = view.findViewById<EditText>(R.id.edEmail)
        val edPassword = view.findViewById<EditText>(R.id.edPassword)
        val edConfirmPassword = view.findViewById<EditText>(R.id.edConfirmPassword)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val backToLogin = view.findViewById<Button>(R.id.backToLogin)

        btnRegister.setOnClickListener {
            viewModel.register(
                edName.text.toString(),
                edEmail.text.toString(),
                edPassword.text.toString(),
                edConfirmPassword.text.toString()
            )
        }
        //Register result
        viewModel.registerResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Registro con éxito.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_nav_slideshow)
            } else {
                Toast.makeText(context, "Error en el registro.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        //Once user is registered, returns to login screen
        backToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_nav_slideshow)
        }
    }
}

