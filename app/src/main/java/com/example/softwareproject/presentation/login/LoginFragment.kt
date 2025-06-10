package com.example.softwareproject.com.example.softwareproject.presentation.login

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.softwareproject.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.fragment.app.viewModels
import com.example.softwareproject.presentation.login.LoginViewModel

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login) {

    private val vm: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<View>(R.id.btn_login)

        loginButton.setOnClickListener {
            vm.startLogin()
        }
        vm.authUrl
            .onEach { url ->
                if (url.isNotBlank()) {
                    CustomTabsIntent.Builder().build()
                        .launchUrl(requireContext(), Uri.parse(url))
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        //vm.startLogin()
    }

    override fun onResume() {
        super.onResume()
        // redirect 처리
        val data = requireActivity().intent?.data
        if (data?.scheme == "myapp" && data.host == "auth-callback") {
            val code  = data.getQueryParameter("code")
            val state = data.getQueryParameter("state")
            vm.handleRedirect(code, state)
            // 인텐트 소모
            requireActivity().intent.data = null
        }
    }
}