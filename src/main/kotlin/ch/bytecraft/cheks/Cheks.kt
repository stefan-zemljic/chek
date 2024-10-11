package ch.bytecraft.cheks

import java.util.concurrent.atomic.AtomicBoolean

private val default = AtomicBoolean(true)
private val isEnabled = ThreadLocal.withInitial<Boolean?> { null }

fun areCheksEnabledDefault(): Boolean {
    return default.get()
}

fun setCheksEnabledDefault(enabled: Boolean) {
    default.set(enabled)
}

fun setCheksEnabled(enabled: Boolean?) {
    isEnabled.set(enabled)
}

fun areCheksEnabled(): Boolean {
    return isEnabled.get() ?: default.get()
}

inline fun chek(condition: () -> Boolean) {
    if (areCheksEnabled() && !condition()) {
        throw AssertionError("Assertion failed")
    }
}

inline fun chek(condition: () -> Boolean, lazyMessage: () -> Any) {
    if (areCheksEnabled() && !condition()) {
        throw AssertionError(lazyMessage())
    }
}