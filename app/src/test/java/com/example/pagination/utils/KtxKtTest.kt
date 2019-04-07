package com.example.pagination.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KtxKtTest {

    @Test
    fun `page of empty list should be empty`() {
        assertThat(emptyList<Any>().page(1, 100)).isEmpty()
    }

    @Test
    fun `first page of single element list should be single element list`() {
        val element = 100
        val page = listOf(element).page(1, 100)
        assertThat(page).hasSize(1)
        assertThat(page).containsOnly(element)
    }

    @Test
    fun `second page of single element list should be empty list`() {
        val element = 100
        assertThat(listOf(element).page(2, 100)).isEmpty()
    }

    @Test
    fun `sum of two page of two paged list should be same list`() {
        val elements = (1..100).toList()
        val firstPage = elements.page(1, 50)
        val secondPage = elements.page(2, 50)

        assertThat(firstPage).hasSize(50)
        assertThat(secondPage).hasSize(50)
        assertThat(firstPage + secondPage).containsExactlyElementsOf(elements)
    }
}