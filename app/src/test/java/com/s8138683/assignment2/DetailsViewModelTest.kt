package com.s8138683.assignment2

import androidx.lifecycle.SavedStateHandle
import com.s8138683.assignment2.data.model.EntityItem
import com.s8138683.assignment2.data.model.FieldEntry
import com.s8138683.assignment2.ui.details.DetailsViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DetailsViewModelTest {

    private val entity = EntityItem(
        fields = listOf(
            FieldEntry("name", "Eucalyptus"),
            FieldEntry("habitat", "Forest"),
        ),
        description = "A fast-growing Australian tree.",
    )

    @Test
    fun `entity from navigation arguments is exposed`() {
        val viewModel = DetailsViewModel(SavedStateHandle(mapOf("entity" to entity)))

        assertEquals(entity, viewModel.entity)
    }

    @Test
    fun `missing entity argument throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            DetailsViewModel(SavedStateHandle())
        }
    }
}
