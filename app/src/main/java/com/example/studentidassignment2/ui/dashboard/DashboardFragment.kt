package com.example.studentidassignment2.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentidassignment2.R
import com.example.studentidassignment2.data.DashboardEntity
import com.example.studentidassignment2.databinding.FragmentDashboardBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModel()
    private val adapter = DashboardAdapter { entity ->
        val bundle = Bundle().apply {
            putString("entity_json", Gson().toJson(entity))
        }
        findNavController().navigate(R.id.action_dashboard_to_details, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDashboardBinding.bind(view)

        binding.dashboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.dashboardRecyclerView.adapter = adapter

        val keypass = arguments?.getString("keypass") ?: ""
        viewModel.loadDashboard(keypass)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is DashboardUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorTextView.visibility = View.GONE
                        }

                        is DashboardUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            adapter.submitItems(state.entities)
                        }

                        is DashboardUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorTextView.text = state.message
                            binding.errorTextView.visibility = View.VISIBLE
                        }

                        is DashboardUiState.Idle -> {
                            binding.progressBar.visibility = View.GONE
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

class DashboardAdapter(
    private val onItemClick: (DashboardEntity) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    private var items: List<DashboardEntity> = emptyList()

    fun submitItems(newItems: List<DashboardEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = android.view.LayoutInflater.from(parent.context)
        val binding = com.example.studentidassignment2.databinding.ItemDashboardBinding.inflate(inflater, parent, false)
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClick)
    }

    override fun getItemCount(): Int = items.size

    class DashboardViewHolder(
        private val binding: com.example.studentidassignment2.databinding.ItemDashboardBinding
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardEntity, onItemClick: (DashboardEntity) -> Unit) {
            binding.entityTitleText.text = item.property1 ?: item.property2 ?: "Entity"
            binding.entitySummaryText.text = item.summary()
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }
}
