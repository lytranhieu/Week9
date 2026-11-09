package com.practice.banksystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Kiem thu cac mau thong tin giao dich va thong bao ngoai le. */
class CoverageTransactionTest {
  @Test
  void transactionAccessorsAndSummaryWork() {
    Transaction transaction = new Transaction(1, 25.0, 100.0, 125.0);
    assertEquals(1, transaction.getType());
    assertEquals(25.0, transaction.getAmount(), 1e-9);
    assertEquals(100.0, transaction.getInitialBalance(), 1e-9);
    assertEquals(125.0, transaction.getFinalBalance(), 1e-9);
    transaction.setType(4);
    transaction.setAmount(10.0);
    transaction.setInitialBalance(125.0);
    transaction.setFinalBalance(115.0);
    assertEquals(4, transaction.getType());
    assertEquals(10.0, transaction.getAmount(), 1e-9);
    assertEquals(125.0, transaction.getInitialBalance(), 1e-9);
    assertEquals(115.0, transaction.getFinalBalance(), 1e-9);
    assertTrue(transaction.getTransactionSummary().contains("Rút tiền tiết kiệm"));
  }

  @Test
  void transactionTypesCoverAllSwitchCases() {
    assertEquals("Nạp tiền vãng lai", Transaction.getTypeString(1));
    assertEquals("Rút tiền vãng lai", Transaction.getTypeString(2));
    assertEquals("Nạp tiền tiết kiệm", Transaction.getTypeString(3));
    assertEquals("Rút tiền tiết kiệm", Transaction.getTypeString(4));
    assertEquals("Không rõ", Transaction.getTypeString(999));
  }

  @Test
  void domainExceptionMessagesAreAvailable() {
    BankException exception = new BankException("Test bank failure");
    assertEquals("Test bank failure", exception.getMessage());
    assertTrue(new InsufficientFundsException(100.0).getMessage().contains("100.00"));
    assertTrue(new InvalidFundingAmountException(-1.0).getMessage().contains("-1.00"));
  }
}
