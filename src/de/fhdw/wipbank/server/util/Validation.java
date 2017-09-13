package de.fhdw.wipbank.server.util;

public class Validation {
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

	public static boolean isAmountValid(final String amount) {
		if (amount == null)
			return false;
		if (amount.equals(""))
			return false;
		try {
			if (Double.valueOf(amount) <= 0)
				return false;
			return amount.matches("^\\d+(\\.\\d{1,2})?$");
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isAccountNumberValid(final String accountNumber) {
		if (accountNumber == null)
			return false;
		if (accountNumber.equals(""))
			return false;
		if (accountNumber.equals("0000"))
			return true;
		return accountNumber.matches("^[1]+\\d{3}$");
	}
}
