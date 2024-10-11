package ch.bytecraft.cheks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CheksTest {
    private var areEnabledDefault = areCheksEnabledDefault()
    private var enabledThread = areCheksEnabled()

    @BeforeEach
    fun setUp() {
        setCheksEnabledDefault(areEnabledDefault)
        setCheksEnabled(enabledThread)
    }

    @Test
    fun `should fail`() {
        assertThrows<AssertionError> { chek { false } }
            .message.let { assertThat(it).isEqualTo("Assertion failed") }
        assertThrows<AssertionError> { chek({ false }) { "Custom message" } }
            .message.let { assertThat(it).isEqualTo("Custom message") }
    }

    @Test
    fun `should pass`() {
        chek { true }
        chek({ true }) { "Custom message" }
    }

    @Test
    fun `should not execute if disabled by default`() {
        assertThat(areCheksEnabledDefault()).isEqualTo(areEnabledDefault)
        setCheksEnabledDefault(false)
        assertThat(areCheksEnabledDefault()).isFalse()
        var executed = false
        chek { executed = true; true }
        assertThat(executed).isFalse()
        chek({ executed = true; true }) { "Custom message" }
        assertThat(executed).isFalse()
    }


    @Test
    fun `should not execute if disabled for Thread`() {
        assertThat(areCheksEnabledDefault()).isEqualTo(areEnabledDefault)
        setCheksEnabled(false)
        assertThat(areCheksEnabledDefault()).isEqualTo(areEnabledDefault)
        var executed = false
        chek { executed = true; true }
        assertThat(executed).isFalse()
        chek({ executed = true; true }) { "Custom message" }
        assertThat(executed).isFalse()
    }

    @Test
    fun `should execute if disabled for other Thread`() {
        assertThat(areCheksEnabledDefault()).isEqualTo(areEnabledDefault)
        setCheksEnabled(false)
        assertThat(areCheksEnabledDefault()).isEqualTo(areEnabledDefault)
        Thread{
            var executed = false
            chek { executed = true; true }
            assertThat(executed).isTrue()
            chek({ executed = true; true }) { "Custom message" }
            assertThat(executed).isTrue()
        }.apply {
            start()
            join()
        }
    }

    @Test
    fun `should not execute even on other threads if disabled by default`() {
        assertThat(areCheksEnabledDefault()).isEqualTo(areEnabledDefault)
        setCheksEnabledDefault(false)
        assertThat(areCheksEnabledDefault()).isFalse()
        Thread{
            var executed = false
            chek { executed = true; true }
            assertThat(executed).isFalse()
            chek({ executed = true; true }) { "Custom message" }
            assertThat(executed).isFalse()
        }.apply {
            start()
            join()
        }
    }
}