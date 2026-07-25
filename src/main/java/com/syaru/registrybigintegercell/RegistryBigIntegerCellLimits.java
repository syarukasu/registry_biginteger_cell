package com.syaru.registrybigintegercell;

import java.math.BigInteger;

/** Shared exact stock limit for every key exposed by either test cell. */
public final class RegistryBigIntegerCellLimits {
    public static final int MAXIMUM_DECIMAL_POWER = 64;
    public static final BigInteger MAXIMUM_PER_KEY =
            BigInteger.TEN.pow(MAXIMUM_DECIMAL_POWER);

    private RegistryBigIntegerCellLimits() {
    }
}
