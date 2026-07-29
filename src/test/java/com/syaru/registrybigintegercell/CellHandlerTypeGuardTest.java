package com.syaru.registrybigintegercell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

final class CellHandlerTypeGuardTest {
    @Test
    void returnsOnlyInstancesOfTheOwnedConcreteType() {
        Object candidate =
                "owned";

        assertEquals(
                candidate,
                CellHandlerTypeGuard.castOrNull(
                        candidate,
                        String.class));
        assertNull(
                CellHandlerTypeGuard.castOrNull(
                        candidate,
                        Integer.class));
        assertNull(
                CellHandlerTypeGuard.castOrNull(
                        null,
                        String.class));
    }
}
