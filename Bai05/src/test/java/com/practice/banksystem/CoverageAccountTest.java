package com.practice.banksystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Kiem thu bo sung cho quy tac tai khoan va cac nhanh xu ly ngoai le. */
class CoverageAccountTest {
  @Test
  void checkingAccountAcceptsValidDepositAndWithdrawal() {
    CheckingAccount account = new CheckingAccount(300L, 500.0);
    account.deposit(125.0);
    account.withdraw(75.0);
    assertEquals(550.0, account.getBalance(), 1e-9);
    assertEquals(2, account.getTransactionList().size());
    assertTrue(account.getTransactionHistory().contains("Rút tiền vãng lai"));
  }

  @Test
  void checkingAccountRejectsInvalidAmountsAndInsufficientFunds() {
    CheckingAccount account = new CheckingAccount(301L, 200.0);
    account.deposit(0.0);
    account.deposit(-2.0);
    account.withdraw(0.0);
    account.withdraw(-3.0);
    account.withdraw(201.0);
    assertEquals(200.0, account.getBalance(), 1e-9);
    assertTrue(account.getTransactionList().isEmpty());
  }

  @Test
  void savingsAccountCoversSuccessAndRejectionPaths() {
    SavingsAccount account = new SavingsAccount(400L, 6300.0);
    account.deposit(200.0);
    account.deposit(0.0);
    account.deposit(-2.0);
    account.withdraw(900.0);
    account.withdraw(1001.0);
    account.withdraw(900.0);
    account.withdraw(0.0);
    account.withdraw(-2.0);
    assertEquals(5600.0, account.getBalance(), 1e-9);
    assertEquals(2, account.getTransactionList().size());
  }

  @Test
  void accountIdentityUsesAccountNumber() {
    CheckingAccount first = new CheckingAccount(31L, 100.0);
    SavingsAccount sameNumber = new SavingsAccount(31L, 5000.0);
    CheckingAccount differentNumber = new CheckingAccount(32L, 100.0);
    assertTrue(first.equals(first));
    assertTrue(first.equals(sameNumber));
    assertEquals(first.hashCode(), sameNumber.hashCode());
    assertFalse(first.equals(differentNumber));
    assertFalse(first.equals("31"));
    assertNotEquals(first, differentNumber);
    first.setAccountNumber(44L);
    assertEquals(44L, first.getAccountNumber());
  }

  @Test
  void replacingTransactionListAndIgnoringNullAreSupported() {
    CheckingAccount account = new CheckingAccount(33L, 50.0);
    account.addTransaction(null);
    assertEquals(0, account.getTransactionList().size());
    List<Transaction> custom = new ArrayList<>();
    custom.add(new Transaction(1, 1.0, 50.0, 51.0));
    account.setTransactionList(custom);
    assertEquals(1, account.getTransactionList().size());
    account.setTransactionList(null);
    assertEquals(0, account.getTransactionList().size());
    assertTrue(account.getTransactionHistory().contains("33"));
  }
}
