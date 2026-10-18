package com.example.studentidassignment2.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.studentidassignment2.R
import com.example.studentidassignment2.data.DashboardEntity
import com.example.studentidassignment2.databinding.FragmentDetailsBinding
import com.google.gson.Gson

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDetailsBinding.bind(view)

        val json = arguments?.getString("entity_json")
        if (json.isNullOrBlank()) {
            binding.detailsTextView.text = "No entity was selected."
            return
        }

        val entity = Gson().fromJson(json, DashboardEntity::class.java)
        binding.detailsTextView.text = entity.displayText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
