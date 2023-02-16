package com.nttdata.bankaccountsavingsservice.util;

import java.util.Random;

/**
 * The account number generator.
 */
public class AccountNumberGenerator {
    static final Random random = new Random();

    private AccountNumberGenerator() {

    }

    /**
     * Generate account number string.
     *
     * @return the string
     */
    public static String generateAccountNumber() {
        StringBuilder number = new StringBuilder("TE");
        final int howManyMore = 14;

        for (int i = 0; i < howManyMore; i++) {
            number.append(Integer.valueOf(random.nextInt(9)));
        }

        return number.toString();
    }
}
