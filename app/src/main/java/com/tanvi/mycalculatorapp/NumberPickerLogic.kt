package com.tanvi.mycalculatorapp

import android.widget.EditText

class NumberPickerLogic @JvmOverloads constructor(
    var number: EditText,
    minimum: Int = 0,
    maximum: Int = Int.MAX_VALUE
) {
    var minimum = 0
    var maximum = Int.MAX_VALUE
    fun increment() {
        val newValue = clamp(value + 1)
        value = newValue
    }

    fun decrement() {
        val newValue = clamp(value - 1)
        value = newValue
    }

    /** Ensure that the value to be set falls within the allowable range  */
    fun clamp(newValue: Int): Int {
        var newValue = newValue
        if (newValue < minimum) {
            newValue = minimum
        }
        if (newValue > maximum) {
            newValue = maximum
        }
        return newValue
    }
    /** Return the integer value of the clicker.  */
    /** Force the value  */
    var value: Int
        get() = number.text.toString().toInt()
        set(value) {
            number.setText(Integer.toString(value))
        }

    init {
        this.minimum = minimum
        this.maximum = maximum
    }
}