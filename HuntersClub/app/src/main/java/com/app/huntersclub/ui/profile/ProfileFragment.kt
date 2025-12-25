package com.app.huntersclub.ui.profile

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
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val profileName = view.findViewById<TextView>(R.id.profileName)
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)

        // Observers
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            profileName.text = name
        }

        viewModel.profileImage.observe(viewLifecycleOwner) { fileName ->
            if (!fileName.isNullOrEmpty()) {
                val assetPath = "file:///android_asset/pfp/$fileName"
                Glide.with(this)
                    .load(assetPath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImage)
            }
        }

        //Logout
        btnLogout.setOnClickListener {
            viewModel.logout()
        }

        //After logging out, returns to login screen
        viewModel.logoutResult.observe(viewLifecycleOwner) { loggedOut ->
            if (loggedOut) {
                findNavController().navigate(R.id.action_profileFragment_to_nav_slideshow)
            }
        }

        //Edit profile
        btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }
        //Fixed bug using SingleLiveEvent on the updater on ProfileViewModel
        viewModel.updateResult.observe(viewLifecycleOwner) { success ->
            Toast.makeText(
                context,
                if (success) "Perfil actualizado." else "Error al actualizar el perfil.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Refactored to increase performance (still with dialog box)
    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
        val edNewName = dialogView.findViewById<EditText>(R.id.edNewName)

        val avatarIds = listOf(
            R.id.avatar1 to "1.png",
            R.id.avatar2 to "2.png",
            R.id.avatar3 to "3.png",
            R.id.avatar4 to "4.png",
            R.id.avatar5 to "5.png"
        )

        var selectedAvatarKey: String? = null

        avatarIds.forEach { (viewId, fileName) ->
            val img = dialogView.findViewById<ImageView>(viewId)
            Glide.with(this)
                .load("file:///android_asset/pfp/$fileName")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)

            img.setOnClickListener {
                selectedAvatarKey = fileName
                Toast.makeText(context, "Avatar seleccionado.", Toast.LENGTH_SHORT).show()
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Editar perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = edNewName.text.toString()
                val avatar = selectedAvatarKey ?: viewModel.profileImage.value ?: ""
                viewModel.updateProfile(newName, avatar)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}