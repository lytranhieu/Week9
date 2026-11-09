package com.practice.banksystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Luu tru va doc thong tin khach hang cua ngan hang. */
public class Bank {
  private static final Logger LOGGER = LoggerFactory.getLogger(Bank.class);
  private static final String CUSTOMER_ID_PATTERN = "\\d{9}";

  private List<Customer> customers;

  /** Khoi tao ngan hang voi danh sach khach hang rong. */
  public Bank() {
    customers = new ArrayList<>();
  }

  /**
   * Lay danh sach khach hang hien tai.
   *
   * @return Danh sach khach hang.
   */
  public List<Customer> getCustomerList() {
    return customers;
  }

  /**
   * Thay the danh sach khach hang.
   *
   * @param customerList Danh sach moi; neu null thi dung danh sach rong.
   */
  public void setCustomerList(List<Customer> customerList) {
    customers = customerList == null ? new ArrayList<>() : customerList;
  }

  /**
   * Doc thong tin khach hang va tai khoan tu luong du lieu dau vao.
   *
   * <p>Dong thong tin khach hang ket thuc bang ma dinh danh gom chin chu so.
   * Dong thong tin tai khoan chua so tai khoan, loai tai khoan va so du ban dau.
   *
   * @param inputStream Luong du lieu chua thong tin khach hang; bo qua neu null.
   */
  public void readCustomerList(InputStream inputStream) {
    if (inputStream == null) {
      LOGGER.warn("Customer import skipped because the input stream is null");
      return;
    }
    LOGGER.info("Customer import started");
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    Customer currentCustomer = null;
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        String trimmed = line.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        if (trimmed.isEmpty() || lastSpace <= 0) {
          continue;
        }
        String lastToken = trimmed.substring(lastSpace + 1).trim();
        if (lastToken.matches(CUSTOMER_ID_PATTERN)) {
          String name = trimmed.substring(0, lastSpace).trim();
          currentCustomer = new Customer(Long.parseLong(lastToken), name);
          customers.add(currentCustomer);
          LOGGER.debug("Customer record parsed");
        } else if (currentCustomer != null) {
          parseAccount(trimmed, currentCustomer);
        }
      }
      LOGGER.info("Customer import completed; records={}", customers.size());
    } catch (IOException | NumberFormatException exception) {
      LOGGER.error("Customer import failed", exception);
    }
  }

  private void parseAccount(String line, Customer customer) {
    String[] parts = line.split("\\s+");
    if (parts.length < 3) {
      LOGGER.warn("Malformed account record skipped");
      return;
    }
    long accountNumber = Long.parseLong(parts[0]);
    double initialBalance = Double.parseDouble(parts[2]);
    switch (parts[1]) {
      case Account.CHECKING_TYPE:
        customer.addAccount(new CheckingAccount(accountNumber, initialBalance));
        break;
      case Account.SAVINGS_TYPE:
        customer.addAccount(new SavingsAccount(accountNumber, initialBalance));
        break;
      default:
        LOGGER.warn("Unknown account type skipped");
        return;
    }
    LOGGER.debug("Account record parsed");
  }

  /**
   * Lay thong tin khach hang theo thu tu tang dan cua ma dinh danh.
   *
   * @return Thong tin khach hang da sap xep theo ma dinh danh.
   */
  public String getCustomersInfoByIdOrder() {
    customers.sort(Comparator.comparingLong(Customer::getIdNumber));
    return joinCustomerInfo(customers, false);
  }

  /**
   * Lay thong tin khach hang theo ten roi den ma dinh danh.
   *
   * @return Thong tin khach hang da sap xep theo ten va ma dinh danh.
   */
  public String getCustomersInfoByNameOrder() {
    List<Customer> sorted = new ArrayList<>(customers);
    sorted.sort(Comparator.comparing(Customer::getFullName)
        .thenComparingLong(Customer::getIdNumber));
    return joinCustomerInfo(sorted, true);
  }

  private static String joinCustomerInfo(List<Customer> customerList, boolean trailingNewline) {
    StringBuilder result = new StringBuilder();
    for (int index = 0; index < customerList.size(); index++) {
      result.append(customerList.get(index).getCustomerInfo());
      if (index < customerList.size() - 1 || trailingNewline) {
        result.append('\n');
      }
    }
    return trailingNewline ? result.toString().trim() : result.toString();
  }
}
