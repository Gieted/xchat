package pl.pawelkielb.xchat

class ConstrainedString(initialValue: String = "", val maxLength: Int = Int.MAX_VALUE, val notBlank: Boolean = false) {
    var value: String = initialValue
        set(newValue) {
            when {
                newValue.length > maxLength -> throw IllegalArgumentException("Cannot be longer than $maxLength")
                notBlank && newValue.isBlank() -> throw IllegalArgumentException("Cannot be blank")
            }

            field = newValue
        }

    init {
        value = initialValue
    }
}
