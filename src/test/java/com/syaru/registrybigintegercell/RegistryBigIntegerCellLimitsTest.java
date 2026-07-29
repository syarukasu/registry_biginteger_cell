package com.syaru.registrybigintegercell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

final class RegistryBigIntegerCellLimitsTest {
    @Test
    void returnsExactRemainingCapacityAtEveryBoundary() {
        assertEquals(
                RegistryBigIntegerCellLimits.MAXIMUM_PER_KEY,
                RegistryBigIntegerCellLimits.remainingCapacity(
                        BigInteger.ZERO));
        assertEquals(
                BigInteger.ONE,
                RegistryBigIntegerCellLimits.remainingCapacity(
                        RegistryBigIntegerCellLimits.MAXIMUM_PER_KEY
                                .subtract(BigInteger.ONE)));
        assertEquals(
                BigInteger.ZERO,
                RegistryBigIntegerCellLimits.remainingCapacity(
                        RegistryBigIntegerCellLimits.MAXIMUM_PER_KEY));
        assertEquals(
                BigInteger.ZERO,
                RegistryBigIntegerCellLimits.remainingCapacity(
                        RegistryBigIntegerCellLimits.MAXIMUM_PER_KEY
                                .add(BigInteger.ONE)));
    }

    @Test
    void rejectsNegativeStoredAmounts() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        RegistryBigIntegerCellLimits.remainingCapacity(
                                BigInteger.valueOf(-1L)));
    }
}
