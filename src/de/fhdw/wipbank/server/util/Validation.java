package de.fhdw.wipbank.server.util;

import java.math.BigDecimal;

/**
 * @author Daniel Sawenko
 * @author Jannis Christoph
 */
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

		return reference.matches("^[A-Za-z0-9 ]*$");
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
			return amount.matches("^\\d+(\\.\\d{2})?$");
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
		if (accountNumber == null) {
			return false;
		} else if (accountNumber.equals("")) {
			return false;
		} else if (accountNumber.equals("0000")) {
			return true;
		} else {
			return accountNumber.matches("^[1]+\\d{3}$");
		}
	}

	/**
     * Prüft ob ein Owner valide ist
     *
     * @param owner
     * @return
     */
	public static boolean isOwnerValid(final String owner) {
		if (owner == null)
			return false;
	
		if (owner.equals(""))
			return false;

		return owner.matches("^[A-Za-z]*$");
	}
}
