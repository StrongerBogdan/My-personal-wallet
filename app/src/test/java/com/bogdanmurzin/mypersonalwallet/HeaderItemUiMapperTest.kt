package com.bogdanmurzin.mypersonalwallet

import com.bogdanmurzin.mypersonalwallet.mapper.HeaderItemUiMapper
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class HeaderItemUiMapperTest {

    lateinit var mapper: HeaderItemUiMapper

    @Before
    fun before() {
        val locale = Locale.US
        mapper = HeaderItemUiMapper(locale)
    }

    @Test
    fun toHeaderItemUiMapper_Map() {
        // Arrange
        // GMT: Friday, February 18, 2022 8:54:49 AM
        val date = Date(1645174489000)

        // Act
        val result = mapper.toHeaderItemUiMapper(date, 211.55f)

        // Assert
        assertEquals("18", result.day)
        assertEquals("FRIDAY", result.dayOfTheWeek)
        assertEquals("FEBRUARY 2022", result.monthYear)
        assertEquals("$211.55", result.sumOfTransactions)
    }

    @Test
    fun toHeaderItemUiMapper_Map2() {
        // Arrange
        // GMT: Saturday, April 13, 1889 8:54:49 PM
        val date = Date(-2547169511000)

        // Act
        val result = mapper.toHeaderItemUiMapper(date, 0f)

        // Assert
        assertEquals("13", result.day)
        assertEquals("SATURDAY", result.dayOfTheWeek)
        assertEquals("APRIL 1889", result.monthYear)
        assertEquals("$0.00", result.sumOfTransactions)
    }

}