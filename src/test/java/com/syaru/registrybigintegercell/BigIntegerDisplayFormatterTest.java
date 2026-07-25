package com.syaru.registrybigintegercell;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class BigIntegerDisplayFormatterTest {
    @Test
    void formatsExactPowerOfTenWithoutTrailingZeros() {
        assertEquals(
                "1e64",
                BigIntegerDisplayFormatter.format(BigInteger.TEN.pow(64)));
    }

    @Test
    void formatsRegistryTotalWithSixSignificantDigitsAtMost() {
        BigInteger total = BigInteger.valueOf(23_581L)
                .multiply(BigInteger.TEN.pow(64));
        assertEquals("2.3581e68", BigIntegerDisplayFormatter.format(total));
    }

    @Test
    void keepsSmallAndNegativeValuesReadable() {
        assertEquals("0", BigIntegerDisplayFormatter.format(BigInteger.ZERO));
        assertEquals("23581", BigIntegerDisplayFormatter.format(
                BigInteger.valueOf(23_581L)));
        assertEquals("-1.23456e8", BigIntegerDisplayFormatter.format(
                BigInteger.valueOf(-123_456_789L)));
    }
}
