package com.practice.banksystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Tai khoan thanh toan voi cac quy tac nap va rut tien co ban. */
public class CheckingAccount extends Account {
  private static final Logger LOGGER = LoggerFactory.getLogger(CheckingAccount.class);

  /**
   * Khoi tao tai khoan thanh toan.
   *
   * @param accountNumber So tai khoan.
   * @param balance So du ban dau.
   */
  public CheckingAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      addTransaction(new Transaction(
          Transaction.TYPE_DEPOSIT_CHECKING, amount, initialBalance, getBalance()));
      LOGGER.info("Checking deposit completed");
    } catch (BankException exception) {
      LOGGER.warn("Checking deposit rejected: {}", exception.getClass().getSimpleName());
    }
  }

  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      doWithdrawing(amount);
      addTransaction(new Transaction(
          Transaction.TYPE_WITHDRAW_CHECKING, amount, initialBalance, getBalance()));
      LOGGER.info("Checking withdrawal completed");
    } catch (BankException exception) {
      LOGGER.warn("Checking withdrawal rejected: {}", exception.getClass().getSimpleName());
    }
  }
}
