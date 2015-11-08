/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.f1x.util.format;


import org.f1x.util.buffer.MutableBuffer;

/**
 * This class formats floating point number to textual representation with given precision.
 * Thread safe.
 * For large numbers this method falls back to java.lang.Double.toString(double).
 * Note: formatter always uses '.' dot as decimal separator (regardless of current locale).
 * <p/>
 * NOTE: This is an alternative to DoubleFormatter that has static formatting method (thread safe).
 * TODO: Replace DoubleFormatter by this implementation after additional testing.
 */
public abstract class DoubleFormatter2 {

    /**
     * Bit 63 represents the sign of the floating-point number.
     *
     * @see java.lang.Double#doubleToLongBits(double)
     */
    private static final long SIGN_MASK = 0x8000000000000000L;

    private static final int MIN_PRECISION = 0;
    private static final int MAX_PRECISION = 18;

    private static final long[] MULTIPLIER_TABLE = {
            10L,
            100L,
            1000L,
            10000L,
            100000L,
            1000000L,
            10000000L,
            100000000L,
            1000000000L,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L,
            10000000000000000L,
            100000000000000000L,
            1000000000000000000L
    };

    private DoubleFormatter2() {
    }

    /**
     * Formats given double number with given precision into byte buffer.
     *
     * @param value     a double value.
     * @param precision maximum number of digits after decimal point (e.g. 3). Truncated part will be rounded.
     * @param buffer    formatted double value will be written in this byte array.
     * @param offset    start index in which formatted value will be written.
     * @return the next index in byte buffer after writing formatted value.
     * @throws IllegalArgumentException if precision < 0 or 18 < precision, if value is NaN or Infinite.
     */
    public static int format(double value, int precision, MutableBuffer buffer, int offset) {
        if (precision < MIN_PRECISION)
            throw new IllegalArgumentException("precision < " + MIN_PRECISION);
        if (precision > MAX_PRECISION)
            throw new IllegalArgumentException("precision > " + MAX_PRECISION);
        if (Double.isNaN(value))
            throw new IllegalArgumentException("NaN");
        if (Double.isInfinite(value))
            throw new IllegalArgumentException("Infinity");

        long bits = Double.doubleToRawLongBits(value);
        boolean isNegative = (bits & SIGN_MASK) != 0;
        if (isNegative) {
            // reset sign bit
            bits ^= SIGN_MASK;
            value = Double.longBitsToDouble(bits);
            buffer.putByte(offset++, (byte) '-');
        }

        if (bits == 0) {
            buffer.putByte(offset++, (byte) '0');
            return offset;
        }

        // check whether we can represent integer and fractional parts of double number as long numbers
        if (value < 1e-18 || 1e18 < value)
            return CharSequenceFormatter.format(toString(value), buffer, offset);

        if (precision == 0)
            return LongFormatter.format(Math.round(value), buffer, offset);

        long integerPart = (long) value;
        offset = LongFormatter.format(integerPart, buffer, offset);

        long multiplier = getMultiplier(precision);
        long fractionalPart = Math.round((value - integerPart) * multiplier);
        if (fractionalPart == 0)
            return offset;

        buffer.putByte(offset++, (byte)'.');
        offset = addLeadingZerosIfNeeded(fractionalPart, precision, buffer, offset);

        // get rid of trailing zeros
        while (fractionalPart % 10 == 0)
            fractionalPart /= 10;

        return LongFormatter.format(fractionalPart, buffer, offset);
    }

    private static long getMultiplier(int precision) {
        return MULTIPLIER_TABLE[precision - 1];
    }

    private static int addLeadingZerosIfNeeded(long fractionalPart, int precision, MutableBuffer output, int offset) {
        int leadingZeros = precision - LongFormatter.stringSize(fractionalPart);
        for (int i = 0; i < leadingZeros; i++)
            output.putByte(offset++, (byte)'0');

        return offset;
    }

    private static String toString(double value) {
        // All exceptional cases have been covered
        // TODO: this leads to garbage
        return Double.toString(value);
    }
}
