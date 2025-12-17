package com.app.huntersclub.ui.slideshow

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val profileName = view.findViewById<TextView>(R.id.profileName)
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)

        //Loading user data from Firebase
        viewModel.loadUserData()

        viewModel.userName.observe(viewLifecycleOwner) { name ->
            profileName.text = name
        }

        viewModel.profileImage.observe(viewLifecycleOwner) { fileName ->
            if (!fileName.isNullOrEmpty()) {
                val assetPath = "file:///android_asset/pfp/$fileName"
                Glide.with(this).load(assetPath).into(profileImage)
            }
        }

        //Logout button
        btnLogout.setOnClickListener {
            viewModel.logout()
        }
        //After logging out, returns to login screen
        viewModel.logoutResult.observe(viewLifecycleOwner) { loggedOut ->
            if (loggedOut) {
                findNavController().navigate(R.id.action_profileFragment_to_nav_slideshow)
            }
        }

        //Edit profile with dialogue box
        btnEditProfile.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
            val edNewName = dialogView.findViewById<EditText>(R.id.edNewName)

            val avatar1 = dialogView.findViewById<ImageView>(R.id.avatar1)
            val avatar2 = dialogView.findViewById<ImageView>(R.id.avatar2)
            val avatar3 = dialogView.findViewById<ImageView>(R.id.avatar3)
            val avatar4 = dialogView.findViewById<ImageView>(R.id.avatar4)
            val avatar5 = dialogView.findViewById<ImageView>(R.id.avatar5)

            //Loading avatar images from assets using Glide
            Glide.with(this).load("file:///android_asset/pfp/1.png").into(avatar1)
            Glide.with(this).load("file:///android_asset/pfp/2.png").into(avatar2)
            Glide.with(this).load("file:///android_asset/pfp/3.png").into(avatar3)
            Glide.with(this).load("file:///android_asset/pfp/4.png").into(avatar4)
            Glide.with(this).load("file:///android_asset/pfp/5.png").into(avatar5)

            var selectedAvatarKey: String? = null

            val clickListener = View.OnClickListener { v ->
                selectedAvatarKey = when (v.id) {
                    R.id.avatar1 -> "1.png"
                    R.id.avatar2 -> "2.png"
                    R.id.avatar3 -> "3.png"
                    R.id.avatar4 -> "4.png"
                    R.id.avatar5 -> "5.png"
                    else -> null
                }
                Toast.makeText(context, "Avatar seleccionado.", Toast.LENGTH_SHORT).show()
            }

            avatar1.setOnClickListener(clickListener)
            avatar2.setOnClickListener(clickListener)
            avatar3.setOnClickListener(clickListener)
            avatar4.setOnClickListener(clickListener)
            avatar5.setOnClickListener(clickListener)

            AlertDialog.Builder(requireContext())
                .setTitle("Editar perfil")
                .setView(dialogView)
                .setPositiveButton("Guardar") { _, _ ->
                    val newName = edNewName.text.toString()
                    if (selectedAvatarKey != null) {
                        viewModel.updateProfile(newName, selectedAvatarKey!!)
                    } else {
                        viewModel.updateProfile(newName, viewModel.profileImage.value ?: "")
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        //Fixed bug using SingleLiveEvent on the updater on ProfileViewModel
        viewModel.updateResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Perfil actualizado.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "Error al actualizar el perfil.", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
