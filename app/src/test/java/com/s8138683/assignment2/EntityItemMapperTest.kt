package com.s8138683.assignment2

import com.s8138683.assignment2.data.model.toEntityItem
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests the JSON-to-domain mapping used by DashboardRepository.
 * Runs on the JVM — no Android framework needed.
 */
class EntityItemMapperTest {

    @Test
    fun `description is extracted and excluded from fields`() {
        val json = JsonParser.parseString(
            """{"name": "Eucalyptus", "habitat": "Forest", "description": "A tree."}"""
        ).asJsonObject

        val item = json.toEntityItem()

        assertEquals("A tree.", item.description)
        assertEquals(2, item.fields.size)
        assertTrue(item.fields.none { it.name == "description" })
        assertEquals("Eucalyptus", item.fields[0].value)
    }

    @Test
    fun `null description falls back to default text`() {
        val json = JsonParser.parseString(
            """{"name": "Kangaroo", "description": null}"""
        ).asJsonObject

        val item = json.toEntityItem()

        assertEquals("No description available.", item.description)
        assertFalse(item.fields.any { it.name == "description" })
    }

    @Test
    fun `null field values are skipped`() {
        val json = JsonParser.parseString(
            """{"name": "Kangaroo", "size": null, "description": "A marsupial."}"""
        ).asJsonObject

        val item = json.toEntityItem()

        assertFalse(item.fields.any { it.name == "size" })
    }

    @Test
    fun `numeric fields are mapped as text`() {
        val json = JsonParser.parseString(
            """{"name": "Eucalyptus", "lifespan": 250, "description": "A tree."}"""
        ).asJsonObject

        val item = json.toEntityItem()

        assertEquals("250", item.fields.first { it.name == "lifespan" }.value)
    }
}
