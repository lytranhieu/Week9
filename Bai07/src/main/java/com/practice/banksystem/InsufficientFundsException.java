package com.practice.banksystem;

import java.util.Locale;

/** Ngoai le duoc phat sinh khi so du khong du de rut tien. */
public class InsufficientFundsException extends BankException {
  /**
   * Khoi tao ngoai le khong du so du.
   *
   * @param amount So tien da yeu cau rut.
   */
  public InsufficientFundsException(double amount) {
    super("Số dư tài khoản không đủ $" + String.format(Locale.US, "%.2f", amount)
        + " để thực hiện giao dịch");
  }
}
