package com.s8138683.assignment2.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s8138683.assignment2.data.model.EntityItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Details screen. One ViewModel per Fragment, following
 * the app architecture: the entity arrives as a Navigation argument and is
 * exposed to the Fragment as immutable state.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val entity: EntityItem = checkNotNull(savedStateHandle.get<EntityItem>(ENTITY_ARG)) {
        "DetailsFragment requires an 'entity' navigation argument."
    }

    companion object {
        const val ENTITY_ARG = "entity"
    }
}
