package com.nttdata.bankaccountsavingsservice.util;

import java.util.Random;

/**
 * The card number generator.
 */
public class CardNumberGenerator {

    /**
     * Generate card number string.
     *
     * @return the string
     */
    public static String generateCardNumber() {
        final StringBuffer number = new StringBuffer("TE");

        final int howManyMore = 14;
        final Random random = new Random();

        for (int i = 0; i < howManyMore; i++) {
            number.append(Integer.valueOf(random.nextInt(9)));
        }

        return number.toString();
    }
}
