package com.practice.banksystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** Cac kiem thu hoi quy nham bao dam quy tac nghiep vu ngan hang khong doi. */
class BankSystemTest {
  @Test
  void checkingDepositAndWithdrawalKeepTransactionHistory() {
    CheckingAccount account = new CheckingAccount(100L, 1000.0);
    account.deposit(250.0);
    account.withdraw(100.0);
    assertEquals(1150.0, account.getBalance(), 1e-9);
    assertEquals(2, account.getTransactionList().size());
    assertTrue(account.getTransactionHistory().contains("Nạp tiền vãng lai"));
  }

  @Test
  void invalidCheckingTransactionsDoNotChangeBalance() {
    CheckingAccount account = new CheckingAccount(100L, 100.0);
    account.deposit(-10.0);
    account.withdraw(200.0);
    assertEquals(100.0, account.getBalance(), 1e-9);
    assertEquals(0, account.getTransactionList().size());
  }

  @Test
  void savingsWithdrawalRespectsMinimumBalanceAndLimit() {
    SavingsAccount account = new SavingsAccount(200L, 7000.0);
    account.withdraw(1000.0);
    account.withdraw(1500.0);
    account.withdraw(1000.0);
    account.withdraw(1.0);
    assertEquals(5000.0, account.getBalance(), 1e-9);
    assertEquals(2, account.getTransactionList().size());
  }

  @Test
  void customerImporterReadsCustomersAndAccounts() {
    String data = "Nguyen Van An 123456789\n100 CHECKING 1000\n101 SAVINGS 7000\n";
    Bank bank = new Bank();
    bank.readCustomerList(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
    assertEquals(1, bank.getCustomerList().size());
    assertEquals(2, bank.getCustomerList().get(0).getAccountList().size());
    assertEquals(123456789L, bank.getCustomerList().get(0).getIdNumber());
  }

  @Test
  void customerOrderingUsesExpectedKeys() {
    Bank bank = new Bank();
    bank.getCustomerList().add(new Customer(20L, "Binh"));
    bank.getCustomerList().add(new Customer(10L, "An"));
    assertTrue(bank.getCustomersInfoByIdOrder().startsWith("Số CMND: 10"));
    assertTrue(bank.getCustomersInfoByNameOrder().startsWith("Số CMND: 10"));
  }
}
