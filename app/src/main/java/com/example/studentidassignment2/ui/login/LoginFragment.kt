package com.example.studentidassignment2.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.studentidassignment2.R
import com.example.studentidassignment2.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLoginBinding.bind(view)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text?.toString() ?: ""
            val password = binding.passwordEditText.text?.toString() ?: ""
            val location = when (binding.locationSpinner.selectedItem?.toString()?.lowercase()) {
                "sydney" -> "sydney"
                "br" -> "br"
                else -> "footscray"
            }
            viewModel.login(username, password, location)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.loginButton.isEnabled = false
                            binding.errorTextView.visibility = View.GONE
                        }

                        is LoginUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.loginButton.isEnabled = true
                            binding.errorTextView.text = state.message
                            binding.errorTextView.visibility = View.VISIBLE
                        }

                        is LoginUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.loginButton.isEnabled = true
                            val bundle = Bundle().apply {
                                putString("keypass", state.keypass)
                            }
                            findNavController().navigate(R.id.action_login_to_dashboard, bundle)
                            viewModel.clearError()
                        }

                        is LoginUiState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            binding.loginButton.isEnabled = true
                            binding.errorTextView.visibility = View.GONE
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
