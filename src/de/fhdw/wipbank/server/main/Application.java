package de.fhdw.wipbank.server.main;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.fhdw.wipbank.server.business.FindAccountByNumber;
import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;
import de.fhdw.wipbank.server.util.TestDataHelper;

public class Application {

	private static Logger logger = Logger.getLogger(Application.class);

	/**
	 * Einstieg in das Projekt
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		JettyServer.run();

		initDB(); // Erzeugt Datenbanktabellen, wenn nicht vorhanden.

		/*
		 * Testbetrieb abgeschlossen -> Keine Testdaten mehr notwendig. Bei
		 * weiterer Programmierung können die Testdaten jedoch wieder
		 * reaktiviert werden. initTestData();
		 */
	}

	/**
	 * Datenbanktabellen erzeugen, wenn nicht vorhanden.
	 */
	private static void initDB() {
		AccountService accountService = new AccountService();
		TransactionService transactionService = new TransactionService();
		try {
			logger.info("Datenbank wird initialisiert");
			accountService.createTable();
			transactionService.createTable();
			// Konto der Bank vorhanden?
			Account account = accountService.getAccount("0000");
			if (account == null) {
				// Account "0000" nicht vorhanden -> neu erstellen
				account = new Account();
				account.setOwner("Bank");
				account.setNumber("0000");
				logger.info("Bankkonto wird erstellt: " + account);
				try {
					accountService.create(account);
				} catch (SQLException e1) {
					logger.error("Konnte Bankkonto nicht erstellen");
				}
			}

		} catch (SQLException e) {
			logger.error("Konnte Tabellen nicht erzeugen");
		} catch (Exception e) {
			logger.error("Fehler initDB");
			e.printStackTrace();
		}
	}

	/**
	 * Testdaten werden initialisiert
	 */
	private static void initTestData() {
		TestDataHelper helper = new TestDataHelper();
		helper.wipeDatabase();
		Account bank = helper.createAccount("Bank", "0000");
		Account daniel = helper.createAccount("Daniel", "1000");
		Account philipp = helper.createAccount("Philipp", "1001");
		Account jannis = helper.createAccount("Jannis", "1002");
		Account alexander = helper.createAccount("Alexander", "1003");
		helper.createTransaction(bank, daniel, BigDecimal.valueOf(10000.00));
		helper.createTransaction(bank, philipp, BigDecimal.valueOf(10000.00));
		helper.createTransaction(bank, jannis, BigDecimal.valueOf(10000.00));
		helper.createTransaction(bank, alexander, BigDecimal.valueOf(10000.00));
	}

}
