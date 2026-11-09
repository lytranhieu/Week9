package com.practice.banksystem;

import java.util.Locale;

/** Ngoai le duoc phat sinh khi so tien nap hoac rut khong hop le. */
public class InvalidFundingAmountException extends BankException {
  /**
   * Khoi tao ngoai le ve so tien khong hop le.
   *
   * @param amount So tien khong hop le.
   */
  public InvalidFundingAmountException(double amount) {
    super("Số tiền không hợp lệ: $" + String.format(Locale.US, "%.2f", amount));
  }
}
