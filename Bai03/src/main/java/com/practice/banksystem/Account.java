package com.practice.banksystem;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Loai tai khoan co so dung chung cho tai khoan thanh toan va tiet kiem. */
public abstract class Account {
  /** Ma dinh danh tai khoan thanh toan. */
  public static final String CHECKING_TYPE = "CHECKING";

  /** Ma dinh danh tai khoan tiet kiem. */
  public static final String SAVINGS_TYPE = "SAVINGS";

  private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);

  private long accountNumber;
  private double balance;
  private List<Transaction> transactions;

  /**
   * Khoi tao tai khoan ngan hang.
   *
   * @param accountNumber So tai khoan.
   * @param balance So du ban dau.
   */
  protected Account(long accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.transactions = new ArrayList<>();
  }

  /**
   * Lay so tai khoan.
   *
   * @return So tai khoan hien tai.
   */
  public long getAccountNumber() {
    return accountNumber;
  }

  /**
   * Cap nhat so tai khoan.
   *
   * @param accountNumber So tai khoan moi.
   */
  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * Lay so du cua tai khoan.
   *
   * @return So du hien tai.
   */
  public double getBalance() {
    return balance;
  }

  /**
   * Cap nhat so du cua tai khoan.
   *
   * @param balance So du moi.
   */
  protected void setBalance(double balance) {
    this.balance = balance;
  }

  /**
   * Lay danh sach giao dich cua tai khoan.
   *
   * @return Danh sach cac giao dich thuoc tai khoan.
   */
  public List<Transaction> getTransactionList() {
    return transactions;
  }

  /**
   * Thay the danh sach giao dich cua tai khoan.
   *
   * @param transactionList Danh sach moi; neu null thi tao danh sach rong.
   */
  public void setTransactionList(List<Transaction> transactionList) {
    if (transactionList == null) {
      transactions = new ArrayList<>();
    } else {
      transactions = transactionList;
    }
  }

  /**
   * Nap tien vao tai khoan.
   *
   * @param amount So tien can nap.
   */
  public abstract void deposit(double amount);

  /**
   * Rut tien tu tai khoan.
   *
   * @param amount So tien can rut.
   */
  public abstract void withdraw(double amount);

  /**
   * Cong tien vao so du sau khi kiem tra tinh hop le.
   *
   * @param amount So tien can nap.
   * @throws InvalidFundingAmountException Neu so tien khong lon hon 0.
   */
  protected void doDepositing(double amount) throws InvalidFundingAmountException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    balance += amount;
  }

  /**
   * Tru tien khoi so du sau khi kiem tra so tien va so du hien co.
   *
   * @param amount So tien can rut.
   * @throws BankException Neu giao dich khong hop le.
   */
  protected void doWithdrawing(double amount) throws BankException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    if (amount > balance) {
      throw new InsufficientFundsException(amount);
    }
    balance -= amount;
  }

  /**
   * Them giao dich vao danh sach cua tai khoan.
   *
   * @param transaction Giao dich can them; bo qua neu gia tri la null.
   */
  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactions.add(transaction);
    }
  }

  /**
   * Tao chuoi mo ta lich su giao dich.
   *
   * @return Lich su giao dich da duoc dinh dang.
   */
  public String getTransactionHistory() {
    StringBuilder history = new StringBuilder("Lịch sử giao dịch của tài khoản ")
        .append(accountNumber)
        .append(":\n");
    for (int index = 0; index < transactions.size(); index++) {
      history.append(transactions.get(index).getTransactionSummary());
      if (index < transactions.size() - 1) {
        history.append('\n');
      }
    }
    LOGGER.debug("Transaction history generated");
    return history.toString();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Account)) {
      return false;
    }
    Account other = (Account) object;
    return accountNumber == other.accountNumber;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(accountNumber);
  }
}
