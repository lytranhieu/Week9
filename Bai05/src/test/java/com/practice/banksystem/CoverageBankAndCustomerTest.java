package com.practice.banksystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Kiem thu bo sung cho khach hang, nhap du lieu va sap xep. */
class CoverageBankAndCustomerTest {
  @Test
  void customerFieldsAndAccountManagementAreCovered() {
    Customer customer = new Customer();
    assertEquals(0L, customer.getIdNumber());
    customer.setIdNumber(123456789L);
    customer.setFullName("Nguyen A");
    assertEquals(123456789L, customer.getIdNumber());
    assertEquals("Nguyen A", customer.getFullName());
    assertTrue(customer.getCustomerInfo().contains("Nguyen A"));
    customer.setAccountList(null);
    assertTrue(customer.getAccountList().isEmpty());
    Account account = new CheckingAccount(8L, 100.0);
    customer.addAccount(null);
    customer.addAccount(account);
    customer.addAccount(account);
    assertEquals(1, customer.getAccountList().size());
    customer.removeAccount(null);
    customer.removeAccount(account);
    assertTrue(customer.getAccountList().isEmpty());
    List<Account> anotherList = new ArrayList<>();
    anotherList.add(account);
    customer.setAccountList(anotherList);
    assertEquals(1, customer.getAccountList().size());
  }

  @Test
  void bankAcceptsNullListAndNullStream() {
    Bank bank = new Bank();
    bank.setCustomerList(null);
    bank.readCustomerList(null);
    assertTrue(bank.getCustomerList().isEmpty());
    List<Customer> customers = new ArrayList<>();
    customers.add(new Customer(123456789L, "Test"));
    bank.setCustomerList(customers);
    assertEquals(1, bank.getCustomerList().size());
  }

  @Test
  void bankImporterHandlesUnknownAndMalformedRecords() {
    String input = "Ignored line\n" + "Nguyen A 123456789\n"
        + "100 CHECKING 200\n" + "101 SAVINGS 6000\n"
        + "102 OTHER 50\n" + "103 CHECKING\n" + "   \n";
    Bank bank = new Bank();
    bank.readCustomerList(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    assertEquals(1, bank.getCustomerList().size());
    assertEquals(2, bank.getCustomerList().get(0).getAccountList().size());
  }

  @Test
  void bankImporterHandlesInvalidNumbersAndIoErrors() {
    Bank bank = new Bank();
    String input = "Nguyen A 123456789\n" + "bad CHECKING 300\n";
    bank.readCustomerList(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    assertEquals(1, bank.getCustomerList().size());
    InputStream failingStream = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("simulated read error");
      }
    };
    bank.readCustomerList(failingStream);
    assertEquals(1, bank.getCustomerList().size());
  }

  @Test
  void bankCustomerOrderingCoversEmptyAndNonEmptyLists() {
    Bank bank = new Bank();
    assertTrue(bank.getCustomersInfoByIdOrder().isEmpty());
    assertTrue(bank.getCustomersInfoByNameOrder().isEmpty());
    bank.getCustomerList().add(new Customer(30L, "Zed"));
    bank.getCustomerList().add(new Customer(20L, "Anna"));
    bank.getCustomerList().add(new Customer(10L, "Anna"));
    assertTrue(bank.getCustomersInfoByIdOrder().startsWith("Số CMND: 10"));
    assertTrue(bank.getCustomersInfoByNameOrder().startsWith("Số CMND: 10"));
    assertFalse(bank.getCustomersInfoByIdOrder().isEmpty());
  }
}
