package com.s8138683.assignment2.ui.details

import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.s8138683.assignment2.R
import com.s8138683.assignment2.databinding.FragmentDetailsBinding
import com.s8138683.assignment2.ui.dashboard.AVATAR_COLORS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entity = viewModel.entity
        val title = entity.fields.firstOrNull()?.value ?: "Entity"

        // Hero section
        binding.tvHeroTitle.text = title
        binding.tvHeroSubtitle.text = resources.getQuantityString(
            R.plurals.properties_count, entity.fields.size, entity.fields.size
        )
        binding.tvHeroLetter.text = title.first().uppercaseChar().toString()

        val colorIndex = (title.hashCode() and Int.MAX_VALUE) % AVATAR_COLORS.size
        val avatarColor = ContextCompat.getColor(requireContext(), AVATAR_COLORS[colorIndex])
        binding.viewHeroAvatar.background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(avatarColor)
        }

        // One key/value row per property
        entity.fields.forEachIndexed { index, field ->
            val isLast = index == entity.fields.lastIndex
            binding.llFields.addView(createKeyView(field.name))
            binding.llFields.addView(createValueView(field.value, isLast))
        }

        binding.tvDescription.text = entity.description
    }

    private fun createKeyView(name: String): TextView =
        TextView(requireContext()).apply {
            text = name
            isAllCaps = true
            setTypeface(null, Typeface.BOLD)
            textSize = 13f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.indigo_500))
        }

    private fun createValueView(value: String, isLast: Boolean): TextView {
        val density = resources.displayMetrics.density
        return TextView(requireContext()).apply {
            text = value
            textSize = 15f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                topMargin = (4 * density).toInt()
                bottomMargin = ((if (isLast) 0 else 20) * density).toInt()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
