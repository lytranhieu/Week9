package com.practice.banksystem;

/** Ngoai le co kiem tra dung chung cho cac thao tac ngan hang. */
public class BankException extends Exception {
  /**
   * Khoi tao ngoai le cua he thong ngan hang.
   *
   * @param message Thong bao mo ta nguyen nhan loi.
   */
  public BankException(String message) {
    super(message);
  }
}
