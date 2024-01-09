// Name: 		Mostapha A
// Class: 		CST8132
// Assessment:	Lab 8
// Description: Class for an array of bank accounts, executes their functions as well

package lab8;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A class for the Bank, using an array list to keep track of multiple accounts.
 * Uses the bank account methods to execute all necessary actions.
 * 
 * @author Mostapha A
 * @version 1.1
 * @since 1.8
 * @see BankAccount, ChequingAccount, SavingsAccount
 */
public class Bank {
	// instance variable
	private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
	private Scanner input;
	private Scanner fileInput;
	private static Formatter output;
	// testing variable
	private Boolean success;

	// constructor that takes in a scanner
	/**
	 * Constructor to create an object.
	 * 
	 * @param input A scanner object. Taken from main method to be able to close the
	 *              stream once in the main method without errors.
	 */
	Bank(Scanner input) {
		this.input = input;
	}

	// check account type and add to array
	/**
	 * Add an account to the array list and use their methods to collect the
	 * information.
	 * 
	 * @return Whether adding an account was successful or not
	 */
	public boolean addAccount() {
		boolean success = false;
		// variable to store input
		String type = " ";
		// loop condition
		Boolean loop = true;

		// header
		System.out.println("Enter the details of account Holder " + (accounts.size() + 1)
				+ "\n=================================\n");

		// ask for account type, loop to catch exceptions and validate input
		do {
			// prompt
			System.out.print("Please select the type of account you wish to add. \n"
					+ "Type (without quotations) 'c' for chequing account or 's' for savings account : ");
			// try the input and catch errors
			try {
				type = input.nextLine();
				// if it a valid choice, set loop condition to exit loop
				if (type.equalsIgnoreCase("c") || type.equals("s")) {
					loop = false;
				} else {
					// else throw exception
					throw new WrongMenuException();
				}
			} catch (WrongMenuException e) {
				// print error and exception
				System.out.println(e);
			}

		} while (loop == true);
		// reset loop variable
		loop = true;

		// variable to store the bank account
		BankAccount account;
		// determine which array object needs to be created based on account type
		if (type.equalsIgnoreCase("c")) {
			// if they selected Checking type, create a checking
			account = new ChequingAccount();
			accounts.add(account);
		} else {
			// if they selected savings type, create a savings
			account = new SavingsAccount();
			accounts.add(account);
		}

		// ask for account number here, to check with other account numbers, ensure its
		// not taken
		long inputAccNumber = -1;
		do {
			// try the input and catch any errors
			try {
				System.out.print("Enter account number (up to 8 digits) : ");
				inputAccNumber = input.nextLong();
				if (inputAccNumber > 99999999) {
					throw new DigitAccountNumberException();
				} else if (inputAccNumber < 0) {
					throw new NegativeAccountNumberException();
				} else {
					int arrayIndex;
					// check if we have this account or not
					arrayIndex = findAccount(inputAccNumber);
					// if the account was found, index doesn't equal -1, throw exception
					if (arrayIndex != -1) {
						throw new DuplicateAccountNumberException();
					}
					// if no exceptions were thrown exit loop
					loop = false;
				}
			} catch (InputMismatchException e) {
				// print error and exception
				System.err.println("Input is Wrong! :You must provide an integer value\n" + e);
				input.nextLine();
			} catch (DigitAccountNumberException e) {
				// print error and exception
				System.out.println(e);
				input.nextLine();
			} catch (NegativeAccountNumberException e) {
				// print error and exception
				System.out.println(e);
				input.nextLine();
			} catch (DuplicateAccountNumberException e) {
				// print error and exception
				System.out.println(e);
				input.nextLine();
			}
		} while (loop == true);
		// set the validated account number to the account
		account.accountNumber = inputAccNumber;

		// call method to read rest of the info
		account.addBankAccount(input);

		// success is true
		success = true;
		return success;
	}

	// find a specific account
	/**
	 * Compare a provided account number with the account numbers in our bank
	 * 
	 * @return The index value of which the account is stored, or -1 for an error.
	 */
	private int findAccount(long accountNumber) {
		int arrayIndex = -1;

		// check if we have this account or not
		for (int i = 0; i < accounts.size(); i++) {
			if (accounts.get(i).accountNumber == accountNumber) {
				arrayIndex = i;
				i = accounts.size();
			}
		}

		// return the index number or -1
		return arrayIndex;
	}

	// use account number to display account
	/**
	 * Uses the findAccount method to return a string of a specific accounts
	 * information or an error message.
	 * 
	 * @return The string with an error message or account information.
	 */
	public String displayAccount() {
		int arrayIndex = -1;
		String info = null;
		long account = 0;
		boolean loop = true;

		// ask for account number, loop for input
		do {
			System.out.print("Please enter an account number to display the information of : ");
			// try this input and catch errors
			try {
				account = input.nextLong();
				if (account > 99999999) {
					throw new DigitAccountNumberException();
				} else if (account < 0) {
					throw new NegativeAccountNumberException();
				} else {
					arrayIndex = findAccount(account);
					if (arrayIndex == -1) {
						throw new NotExistAccountException();
					} else {
						loop = false;
					}
				}
			} catch (InputMismatchException e) {
				// print error and exception
				System.err.println(e + ": This is not a number, try again.");
			} catch (DigitAccountNumberException e) {
				// print error and exception
				System.out.println(e);
			} catch (NegativeAccountNumberException e) {
				// print error and exception
				System.out.println(e);
			} catch (NotExistAccountException e) {
				// store error and exception, exit loop
				info = e + "\nReturning to main menu...";
				loop = false;
			}
		} while (loop == true);

		// if the array was not found return error message, else return the information
		if (arrayIndex == -1) {
			// info was already given the error string
		} else {
			info = accounts.get(arrayIndex).toString();
		}

		// return the string of error or account info
		return info;
	}

	// print all account details
	/**
	 * Uses a loop to print all the information of every account in our bank array.
	 */
	public void printAccountDetails() {
		// Header
		System.out.println(
				"Banking System\n" + "******************\n" + "Number of Account Holders : " + accounts.size() + "\n");
		//open the output file to write to
		openOutputFile();
		// print each index of the array
		for (int i = 0; i < accounts.size(); i++) {
			// print to console
			System.out.print(accounts.get(i).toString()+"\n");
			// write to file, opening then closing
			writeIntoFile(accounts.get(i).toString());
		}
		//close the file
		closeOutputFile();
	}

	// use account number to update an account
	/**
	 * Asks for a withdrawal or deposit and updates the balance of the account
	 * accordingly.
	 */
	public void updateAccount() {
		int arrayIndex = -1;
		double changes;
		long account;
		String info = " ";
		boolean loop = true;

		// ask for account number, loop for input
		do {
			System.out.print("Please enter an account number to update the balance of : ");
			// try this input and catch errors
			try {
				account = input.nextLong();
				if (account > 99999999) {
					throw new DigitAccountNumberException();
				} else if (account < 0) {
					throw new NegativeAccountNumberException();
				} else {
					arrayIndex = findAccount(account);
					if (arrayIndex == -1) {
						throw new NotExistAccountException();
					} else {
						loop = false;
					}
				}
			} catch (InputMismatchException e) {
				// print error and exception
				System.err.println(e + ": This is not a number, try again.");
			} catch (DigitAccountNumberException e) {
				// print error and exception
				System.out.println(e);
			} catch (NegativeAccountNumberException e) {
				// print error and exception
				System.out.println(e);
			} catch (NotExistAccountException e) {
				// store error and exception, exit loop
				info = e + "\nReturning to main menu...";
				loop = false;
			}
		} while (loop == true);

		// if the array was not found return error message, else ask for the changes and
		// execute
		if (arrayIndex == -1) {
			System.out.println(info);
		} else {
			// ask for change
			System.out.print("Enter the amount to deposit/withdraw "
					+ "(positive number to deposit, negative number to withdraw) \n");
			// try the input and catch exceptions
			try {
				changes = input.nextDouble();
				// check if you have enough balance for withdrawals
				if ((changes * -1) > accounts.get(arrayIndex).balance) {
					throw new InsufficientFundsException((changes * -1) - accounts.get(arrayIndex).balance);
				} else {
					// update account
					accounts.get(arrayIndex).updateBalance(changes);
				}
			} catch (InputMismatchException e) {
				// print error and exception
				System.err.println(e + ": This is not a number, try again.");
			} catch (InsufficientFundsException e) {
				// print error and exception
				System.out.println(e);
			}
		}
	}

	// process monthly update for all accounts
	/**
	 * Perform all the necessary calculations for fees or interest on all accounts
	 * in our array.
	 */
	public void monthlyUpdate() {
		// update each index of the array
		for (int i = 0; i < accounts.size(); i++) {
			accounts.get(i).monthlyAccountUpdate();
		}
	}

	// this is to open output file
	/**
	 * Opens the file that we are taking input from.
	 */
	public void openInputFile() {
		try {
			// file location is relative, uses the Eclipse project root folder (or the grandparent folder)
			fileInput = new Scanner(Paths.get(".\\bankInput.txt"));
			success = true;
		} catch (FileNotFoundException e) {
			System.err.println("error, file not found");
			success = false;
		} catch (IOException e) {
			System.err.println("error, IOException");
			success = false;
		}
	}

	// reads from the open file
	/**
	 * Reads from the file we are taking input from.
	 */
	public void readFromFile() {
		// to test the type
		String type;
		// open the file
		openInputFile();

		// loop until there are no more entries in the file
		while (success == true && fileInput.hasNext()) {
			// variable to determine account type
			type = fileInput.next();

			// variable to store the bank account
			BankAccount account;
			// determine which array object needs to be created based on account type
			if (type.equalsIgnoreCase("c")) {
				// if they selected Chequing type, create a chequing
				account = new ChequingAccount();
				// set the values from the file
				// take the account number
				account.accountNumber = Long.parseLong(fileInput.next());
				// take the first name
				account.accHolder.setFirstName(fileInput.next());
				// take the last name
				account.accHolder.setLastName(fileInput.next());
				// take the phone number
				account.accHolder.setPhoneNumber(Long.parseLong(fileInput.next()));
				// take the email
				account.accHolder.setEmail(fileInput.next());
				// take the balance
				account.balance = Double.parseDouble(fileInput.next());
				// take the fee
				account.fee = Double.parseDouble(fileInput.next());

			} else {
				// if they selected savings type, create a savings
				account = new SavingsAccount();
				// set the values from the file
				// take the account number
				account.accountNumber = Long.parseLong(fileInput.next());
				// take the first name
				account.accHolder.setFirstName(fileInput.next());
				// take the last name
				account.accHolder.setLastName(fileInput.next());
				// take the phone number
				account.accHolder.setPhoneNumber(Long.parseLong(fileInput.next()));
				// take the email
				account.accHolder.setEmail(fileInput.next());
				// take the balance
				account.balance = Double.parseDouble(fileInput.next());
				// take the minimum balance
				account.minBalance = Double.parseDouble(fileInput.next());
				// take the interest rate
				account.interestRate = Double.parseDouble(fileInput.next());
			}

			// add the created account to the array
			accounts.add(account);
		}

		// close the file if it was opened
		if (success == true) {
			closeInputFile();
		}
	}

	// closes the file we opened for input
	/**
	 * Closes the file we were taking input from.
	 */
	public void closeInputFile() {
		if (fileInput != null) {
			fileInput.close();
		}
	}

	// opens the file that we will be writing in
	/**
	 * Opens the file that we will be writing our information to.
	 */
	public void openOutputFile() {
		try {
			// file location is relative, uses the Eclipse project root folder (or the grandparent folder)
			output = new Formatter(".\\bankOutput.txt");
			success = true;
		} catch (SecurityException e) {
			System.err.println("error, not allowed");
			success = false;
		} catch (FileNotFoundException e) {
			System.err.println("error, file not found");
			success = false;
		}
	}

	// writes into the open file
	/**
	 * Writes into the file we have opened.
	 * 
	 * @param info The string of account information to be written into the file.
	 */
	public void writeIntoFile(String info) {
		// if the file was opened successfully
		if (success == true) {
			// takes the string and prints into file
			output.format("%s\n", info);
		}
	}

	// closes the output file
	/**
	 * Closes the file we have written into.
	 */
	public void closeOutputFile() {
		//if the file was opened successfully output is not null close
		if (success == true && output != null) {
			output.close();
		}
	}
}