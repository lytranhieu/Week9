package com.practice.banksystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Tai khoan tiet kiem co gioi han rut tien va yeu cau so du toi thieu. */
public class SavingsAccount extends Account {
  private static final Logger LOGGER = LoggerFactory.getLogger(SavingsAccount.class);
  private static final double MAX_WITHDRAWAL = 1000.0;
  private static final double MIN_BALANCE = 5000.0;

  /**
   * Khoi tao tai khoan tiet kiem.
   *
   * @param accountNumber So tai khoan.
   * @param balance So du ban dau.
   */
  public SavingsAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      addTransaction(new Transaction(
          Transaction.TYPE_DEPOSIT_SAVINGS, amount, initialBalance, getBalance()));
      LOGGER.info("Savings deposit completed");
    } catch (BankException exception) {
      LOGGER.warn("Savings deposit rejected: {}", exception.getClass().getSimpleName());
    }
  }

  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      if (amount > MAX_WITHDRAWAL) {
        throw new InvalidFundingAmountException(amount);
      }
      if (initialBalance - amount < MIN_BALANCE) {
        throw new InsufficientFundsException(amount);
      }
      doWithdrawing(amount);
      addTransaction(new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS, amount, initialBalance, getBalance()));
      LOGGER.info("Savings withdrawal completed");
    } catch (BankException exception) {
      LOGGER.warn("Savings withdrawal rejected: {}", exception.getClass().getSimpleName());
    }
  }
}
