package de.fhdw.wipbank.server.rest;

public class Validation {
	public static boolean isReferenceValid(String reference) {
		if (reference == null)
			return false;

		char[] chars = reference.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c) && !Character.isDigit(c) && !Character.isWhitespace(c)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isAmountValid(String amount) {
		if (amount == null)
			return false;
		if (Double.valueOf(amount) <= 0)
			return false;
		return amount.matches("^\\d+(\\.\\d{1,2})?$");
	}

	public static boolean isAccountNumberValid(String accountNumber) {
		if (accountNumber == null)
			return false;
		return accountNumber.matches("^[1]+\\d{3}$");
	}
}
