package com.s8138683.assignment2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.s8138683.assignment2.R
import com.s8138683.assignment2.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private val args: DashboardFragmentArgs by navArgs()

    private lateinit var adapter: EntityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTopic.text = getString(R.string.dashboard_topic, args.keypass)

        adapter = EntityAdapter { entity ->
            findNavController().navigate(
                DashboardFragmentDirections.actionDashboardToDetails(entity)
            )
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.loadDashboard(args.keypass)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: DashboardUiState) {
        when (state) {
            DashboardUiState.Loading -> {
                binding.progressBar.isVisible = true
                binding.tvError.isVisible = false
            }
            is DashboardUiState.Success -> {
                binding.progressBar.isVisible = false
                binding.tvSummary.text =
                    getString(R.string.dashboard_summary, state.data.entityTotal)
                adapter.submitList(state.data.entities)
            }
            is DashboardUiState.Error -> {
                binding.progressBar.isVisible = false
                binding.tvError.text = state.message
                binding.tvError.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}
