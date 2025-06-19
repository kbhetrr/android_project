package com.example.softwareproject.com.example.softwareproject.presentation.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.softwareproject.MainActivity
import com.example.softwareproject.R
import com.example.softwareproject.databinding.LoginBinding
import com.example.softwareproject.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login) {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            viewModel.loginWithGitHub(requireActivity())
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val exists = viewModel.checkUserExist(user.uid)
                    if (!exists) {
                        Log.d("Login", "신규 유저 생성 완료")
                    }
                    Log.d("Login", "로그인 성공: ${user.email}")

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            } else {
                Log.e("Login", "로그인 실패 또는 취소")
            }
        }

        viewModel.githubUser.observe(viewLifecycleOwner) { githubUser ->
            githubUser?.let {
                val firebaseUser = viewModel.loginResult.value
                if (firebaseUser != null) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val exists = viewModel.checkUserExist(firebaseUser.uid)
                        if (!exists) {
                            viewModel.createUser(it, firebaseUser)
                            Log.d("Login", "신규 유저 생성 완료")
                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
