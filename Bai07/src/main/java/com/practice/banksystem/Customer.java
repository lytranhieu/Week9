package com.practice.banksystem;

import java.util.ArrayList;
import java.util.List;

/** Bieu dien mot khach hang cung cac tai khoan ngan hang cua nguoi do. */
public class Customer {
  private long idNumber;
  private String fullName;
  private List<Account> accountList;

  /** Khoi tao khach hang rong, phuc vu kha nang tuong thich voi ma cu. */
  public Customer() {
    this(0L, "");
  }

  /**
   * Khoi tao khach hang voi ma dinh danh va ho ten.
   *
   * @param idNumber Ma dinh danh cua khach hang.
   * @param fullName Ho va ten khach hang.
   */
  public Customer(long idNumber, String fullName) {
    this.idNumber = idNumber;
    this.fullName = fullName;
    this.accountList = new ArrayList<>();
  }

  /**
   * Lay ma dinh danh khach hang.
   *
   * @return Ma dinh danh cua khach hang.
   */
  public long getIdNumber() {
    return idNumber;
  }

  /**
   * Cap nhat ma dinh danh khach hang.
   *
   * @param idNumber Ma dinh danh moi.
   */
  public void setIdNumber(long idNumber) {
    this.idNumber = idNumber;
  }

  /**
   * Lay ho va ten khach hang.
   *
   * @return Ho va ten hien tai.
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Cap nhat ho va ten khach hang.
   *
   * @param fullName Ho va ten moi.
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * Lay danh sach tai khoan cua khach hang.
   *
   * @return Danh sach tai khoan cua khach hang.
   */
  public List<Account> getAccountList() {
    return accountList;
  }

  /**
   * Thay the danh sach tai khoan cua khach hang.
   *
   * @param accountList Danh sach moi; neu null thi dung danh sach rong.
   */
  public void setAccountList(List<Account> accountList) {
    accountList = accountList == null ? new ArrayList<>() : accountList;
    this.accountList = accountList;
  }

  /**
   * Them tai khoan vao danh sach neu chua ton tai.
   *
   * @param account Tai khoan can them.
   */
  public void addAccount(Account account) {
    if (account != null && !accountList.contains(account)) {
      accountList.add(account);
    }
  }

  /**
   * Xoa tai khoan khoi danh sach khach hang.
   *
   * @param account Tai khoan can xoa.
   */
  public void removeAccount(Account account) {
    if (account != null) {
      accountList.remove(account);
    }
  }

  /**
   * Lay thong tin mo ta khach hang.
   *
   * @return Chuoi thong tin khach hang da duoc dinh dang.
   */
  public String getCustomerInfo() {
    return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
  }
}
