package com.syaru.registrybigintegercell;

import java.math.BigInteger;
import java.util.Objects;

/** 巨大なBigIntegerを、ツールチップ内で折り返さない短い指数表記へ変換する。 */
final class BigIntegerDisplayFormatter {
    /** 仮数は最大6桁に抑え、量の比較に必要な精度と表示幅を両立する。 */
    private static final int MAXIMUM_SIGNIFICANT_DIGITS = 6;

    private BigIntegerDisplayFormatter() {
    }

    static String format(BigInteger value) {
        Objects.requireNonNull(value, "value");
        // 0は桁数と指数を定義せず、そのまま表示する。
        if (value.signum() == 0) {
            return "0";
        }

        String digits = value.abs().toString();
        // 短い値は指数表記にするとかえって読みにくいため、通常の10進表記を維持する。
        if (digits.length() <= MAXIMUM_SIGNIFICANT_DIGITS) {
            return value.toString();
        }

        int significantLength = Math.min(
                MAXIMUM_SIGNIFICANT_DIGITS,
                digits.length());
        String significant = digits.substring(0, significantLength);
        // 10の累乗は「1.00000e64」ではなく「1e64」と表示する。
        int lastNonZero = significant.length() - 1;
        while (lastNonZero > 0 && significant.charAt(lastNonZero) == '0') {
            lastNonZero--;
        }
        significant = significant.substring(0, lastNonZero + 1);

        String mantissa = significant.length() == 1
                ? significant
                : significant.charAt(0) + "." + significant.substring(1);
        String sign = value.signum() < 0 ? "-" : "";
        return sign + mantissa + "e" + (digits.length() - 1);
    }
}
