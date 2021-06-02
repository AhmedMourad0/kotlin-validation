package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.Case.*
import kotlin.test.*
import kotlin.test.Test

class CaseTests {

    @Test
    fun legal_createsLegalCaseInstance() {
        assertEquals(Legal(5), 5.legal())
    }

    @Test
    fun illegal_createsIllegalCaseInstance() {
        assertEquals(Illegal(5), 5.illegal())
    }

    @Test
    fun swap_swapsLegalCasesForIllegalOnesAndViceVersa() {
        assertEquals(Illegal(5), 5.legal().swap())
        assertEquals(Legal(5), 5.illegal().swap())
    }

    @Test
    fun orElse_returnsThisValueIfLegalOrTheGivenOneIfIllegal() {
        assertEquals(5, 5.legal().orElse { 4 })
        assertEquals(4, 5.illegal().orElse { 4 })
    }
}
