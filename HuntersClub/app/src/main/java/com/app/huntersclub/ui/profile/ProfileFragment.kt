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
import com.app.huntersclub.utils.ImagePath
import com.app.huntersclub.utils.UserSession
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val profileName = view.findViewById<TextView>(R.id.profileName)
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val btnSets = view.findViewById<Button>(R.id.btnSets)

        // Load cached data instantly
        profileName.text = UserSession.userName

        val avatarId = UserSession.profileImage.toIntOrNull()
        if (avatarId != null) {
            val assetPath = ImagePath.getAssetPath("profile", id = avatarId)
            Glide.with(this)
                .load(assetPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage)
        }

        btnLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.logoutResult.observe(viewLifecycleOwner) { loggedOut ->
            if (loggedOut) {
                findNavController().navigate(R.id.action_profileFragment_to_nav_slideshow)
            }
        }

        btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        viewModel.updateResult.observe(viewLifecycleOwner) { success ->
            Toast.makeText(
                context,
                if (success) "Perfil actualizado." else "Error al actualizar el perfil.",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnSets.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_profileSetsFragment)
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
        val edNewName = dialogView.findViewById<EditText>(R.id.edNewName)

        val avatarViews = listOf(
            R.id.avatar1,
            R.id.avatar2,
            R.id.avatar3,
            R.id.avatar4,
            R.id.avatar5
        ).map { dialogView.findViewById<ImageView>(it) }


        var selectedAvatarKey: Int? = null

        ImagePath.profileAvatars.forEachIndexed { index, avatarId ->
            val img = avatarViews[index]
            val avatarPath = ImagePath.getAssetPath("profile", id = avatarId)

            Glide.with(this)
                .load(avatarPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)

            img.setOnClickListener {
                selectedAvatarKey = avatarId
                Toast.makeText(context, "Avatar seleccionado.", Toast.LENGTH_SHORT).show()
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Editar perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = edNewName.text.toString()
                val avatar = selectedAvatarKey?.toString() ?: UserSession.profileImage
                viewModel.updateProfile(newName, avatar)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
