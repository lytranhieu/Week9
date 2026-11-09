package com.practice.banksystem;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Bieu dien mot giao dich nap tien hoac rut tien. */
public class Transaction {
  /** Ma loai giao dich nap tien vao tai khoan thanh toan. */
  public static final int TYPE_DEPOSIT_CHECKING = 1;

  /** Ma loai giao dich rut tien tu tai khoan thanh toan. */
  public static final int TYPE_WITHDRAW_CHECKING = 2;

  /** Ma loai giao dich nap tien vao tai khoan tiet kiem. */
  public static final int TYPE_DEPOSIT_SAVINGS = 3;

  /** Ma loai giao dich rut tien tu tai khoan tiet kiem. */
  public static final int TYPE_WITHDRAW_SAVINGS = 4;

  private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

  private int type;
  private double amount;
  private double initialBalance;
  private double finalBalance;

  /**
   * Khoi tao giao dich voi loai, so tien va so du truoc/sau.
   *
   * @param type Ma loai giao dich.
   * @param amount So tien giao dich.
   * @param initialBalance So du truoc giao dich.
   * @param finalBalance So du sau giao dich.
   */
  public Transaction(int type, double amount, double initialBalance, double finalBalance) {
    this.type = type;
    this.amount = amount;
    this.initialBalance = initialBalance;
    this.finalBalance = finalBalance;
  }

  /**
   * Lay ma loai giao dich.
   *
   * @return Ma loai giao dich hien tai.
   */
  public int getType() {
    return type;
  }

  /**
   * Cap nhat ma loai giao dich.
   *
   * @param type Ma loai giao dich moi.
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Lay so tien cua giao dich.
   *
   * @return So tien giao dich.
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Cap nhat so tien giao dich.
   *
   * @param amount So tien moi.
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Lay so du truoc khi thuc hien giao dich.
   *
   * @return So du ban dau.
   */
  public double getInitialBalance() {
    return initialBalance;
  }

  /**
   * Cap nhat so du truoc giao dich.
   *
   * @param initialBalance So du ban dau moi.
   */
  public void setInitialBalance(double initialBalance) {
    this.initialBalance = initialBalance;
  }

  /**
   * Lay so du sau khi thuc hien giao dich.
   *
   * @return So du cuoi cung.
   */
  public double getFinalBalance() {
    return finalBalance;
  }

  /**
   * Cap nhat so du sau giao dich.
   *
   * @param finalBalance So du cuoi cung moi.
   */
  public void setFinalBalance(double finalBalance) {
    this.finalBalance = finalBalance;
  }

  /**
   * Chuyen ma loai giao dich thanh chuoi mo ta.
   *
   * @param transactionType Ma loai giao dich.
   * @return Ten loai giao dich tuong ung.
   */
  public static String getTypeString(int transactionType) {
    switch (transactionType) {
      case TYPE_DEPOSIT_CHECKING:
        return "Nạp tiền vãng lai";
      case TYPE_WITHDRAW_CHECKING:
        return "Rút tiền vãng lai";
      case TYPE_DEPOSIT_SAVINGS:
        return "Nạp tiền tiết kiệm";
      case TYPE_WITHDRAW_SAVINGS:
        return "Rút tiền tiết kiệm";
      default:
        return "Không rõ";
    }
  }

  /**
   * Tao chuoi tom tat thong tin giao dich.
   *
   * @return Noi dung giao dich da duoc dinh dang.
   */
  public String getTransactionSummary() {
    LOGGER.debug("Transaction summary created; type={}", type);
    return "- Kiểu giao dịch: " + getTypeString(type)
        + ". Số dư ban đầu: $" + String.format(Locale.US, "%.2f", initialBalance)
        + ". Số tiền: $" + String.format(Locale.US, "%.2f", amount)
        + ". Số dư cuối: $" + String.format(Locale.US, "%.2f", finalBalance)
        + ".";
  }
}
