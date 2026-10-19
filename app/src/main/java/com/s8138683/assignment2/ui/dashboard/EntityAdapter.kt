package com.s8138683.assignment2.ui.dashboard

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s8138683.assignment2.R
import com.s8138683.assignment2.data.model.EntityItem
import com.s8138683.assignment2.databinding.ItemEntityBinding

/** Deterministic avatar colors shared with the Details screen. */
internal val AVATAR_COLORS = listOf(
    R.color.avatar_indigo,
    R.color.avatar_pink,
    R.color.avatar_teal,
    R.color.avatar_amber,
    R.color.avatar_purple,
    R.color.avatar_red,
)

/**
 * Displays a summary of each entity (all properties except the description).
 */
class EntityAdapter(
    private val onItemClick: (EntityItem) -> Unit,
) : ListAdapter<EntityItem, EntityAdapter.EntityViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class EntityViewHolder(
        private val binding: ItemEntityBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(entity: EntityItem, position: Int) {
            // Title: the value of the first property (usually the entity's name).
            val title = entity.fields.firstOrNull()?.value ?: "Entity"
            binding.tvItemTitle.text = title

            // Subtitle: remaining properties as "key: value" pairs.
            binding.tvItemSubtitle.text = entity.fields
                .drop(1)
                .joinToString("   |   ") { "${it.name}: ${it.value}" }
                .ifEmpty {
                    entity.fields.firstOrNull()?.let { "${it.name}: ${it.value}" } ?: ""
                }

            // Colored avatar with the entity's initial.
            binding.tvAvatarLetter.text = title.first().uppercaseChar().toString()
            val color = ContextCompat.getColor(
                binding.root.context,
                AVATAR_COLORS[position % AVATAR_COLORS.size],
            )
            binding.viewAvatar.background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(color)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<EntityItem>() {
        override fun areItemsTheSame(oldItem: EntityItem, newItem: EntityItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: EntityItem, newItem: EntityItem): Boolean =
            oldItem == newItem
    }
}
