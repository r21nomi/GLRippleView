package r21nomi.com.glrippleview

/**
 * Created by Ryota Niinomi on 17/05/25.
 */
internal object AnimationUtil {

    /**
     * Translate the range to another range
     */
    fun map(value: Float, beforeMin: Float, beforeMax: Float, afterMin: Float, afterMax: Float): Float {
        var result = lerp(afterMin, afterMax, normalize(value, beforeMin, beforeMax))
        if (afterMin < afterMax) {
            if (result > afterMax) {
                result = afterMax
            } else if (result < afterMin) {
                result = afterMin
            }
        } else {
            if (result > afterMin) {
                result = afterMin
            } else if (result < afterMax) {
                result = afterMax
            }
        }

        return result
    }

    private fun normalize(value: Float, min: Float, max: Float): Float {
        return (value - min) / (max - min)
    }

    private fun lerp(sourceValue1: Float, sourceValue2: Float, amount: Float): Float {
        return sourceValue1 + (sourceValue2 - sourceValue1) * amount
    }
}
