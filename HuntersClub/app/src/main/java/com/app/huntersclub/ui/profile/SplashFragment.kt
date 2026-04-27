package com.app.huntersclub.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.R
import com.bumptech.glide.Glide

class SplashFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gifView = view.findViewById<ImageView>(R.id.splashGif)

        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(gifView)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val app = requireActivity().application as HuntersClubApp
        val auth = app.auth

        if (auth.currentUser != null) {
            viewModel.preloadUserData()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }

        viewModel.userDataLoaded.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_splashFragment_to_profileFragment)
        }
    }
}
