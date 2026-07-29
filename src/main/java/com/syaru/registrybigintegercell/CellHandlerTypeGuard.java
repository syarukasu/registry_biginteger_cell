package com.syaru.registrybigintegercell;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

/** 外部セル照会で具象型を推測せず、安全に所有Itemだけを取り出す。 */
final class CellHandlerTypeGuard {
    private CellHandlerTypeGuard() {
    }

    @Nullable
    static <T> T castOrNull(
            Object candidate,
            Class<T> expectedType) {
        Class<T> checkedType =
                Objects.requireNonNull(
                        expectedType,
                        "expectedType");
        // 型が一致しない任意MODのItemはcastせず、次のAE2 Cell Handlerへ渡す。
        return checkedType.isInstance(candidate)
                ? checkedType.cast(candidate)
                : null;
    }
}
