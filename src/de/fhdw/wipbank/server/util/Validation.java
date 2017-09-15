package de.fhdw.wipbank.server.util;

import java.math.BigDecimal;

public class Validation {

    /**
     * Prüft ob ein Betreff valide ist
     *
     * @param reference
     * @return
     */
	public static boolean isReferenceValid(final String reference) {
		if (reference == null)
			return false;
	
		if (reference.equals(""))
			return false;

		char[] chars = reference.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c) && !Character.isDigit(c) && !Character.isWhitespace(c)) {
				return false;
			}
		}

		return true;
	}

    /**
     * Prüft ob ein Betrag als String valide ist
     *
     * @param amount
     * @return
     */
	public static boolean isAmountValid(final String amount) {
		if (amount == null)
			return false;
		if (amount.equals(""))
			return false;
		try {
			if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) < 1 ) {
                return false;
            }
			return amount.matches("^\\d+(\\.\\d{1,2})?$");
		} catch (NumberFormatException e) {
			return false;
		}
	}

    /**
     * Prüft ob eine Account Nummer valide ist
     *
     * @param accountNumber
     * @return
     */
	public static boolean isAccountNumberValid(final String accountNumber) {
		if (accountNumber == null)
			return false;
		if (accountNumber.equals(""))
			return false;
		return accountNumber.matches("^[1]+\\d{3}$");
	}
}
