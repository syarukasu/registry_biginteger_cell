package com.syaru.registrybigintegercell;

import java.math.BigInteger;
import java.util.Objects;

/** Shared exact stock limit for every key exposed by either test cell. */
public final class RegistryBigIntegerCellLimits {
    public static final int MAXIMUM_DECIMAL_POWER = 64;
    public static final BigInteger MAXIMUM_PER_KEY =
            BigInteger.TEN.pow(MAXIMUM_DECIMAL_POWER);

    private RegistryBigIntegerCellLimits() {
    }

    public static BigInteger remainingCapacity(BigInteger currentAmount) {
        BigInteger current = Objects.requireNonNull(
                currentAmount,
                "currentAmount");
        // 壊れた負数を空き容量へ変換すると複製につながるため、境界で明示的に拒否する。
        if (current.signum() < 0) {
            throw new IllegalArgumentException("current cell amount is negative");
        }
        // 上限到達・超過済みの保存値へは追加容量を与えず、既存値もここでは変更しない。
        if (current.compareTo(MAXIMUM_PER_KEY) >= 0) {
            return BigInteger.ZERO;
        }
        return MAXIMUM_PER_KEY.subtract(current);
    }
}
