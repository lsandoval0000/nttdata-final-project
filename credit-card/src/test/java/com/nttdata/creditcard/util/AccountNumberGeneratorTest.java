package com.nttdata.creditcard.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberGeneratorTest {

    @Test
    void generateAccountNumber() {
        Assertions.assertEquals(16, AccountNumberGenerator.generateAccountNumber().length());
    }
}