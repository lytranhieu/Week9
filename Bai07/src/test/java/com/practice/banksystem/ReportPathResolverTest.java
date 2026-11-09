package com.practice.banksystem;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Kiem tra duong dan tep tren cac he dieu hanh. */
class ReportPathResolverTest {
  @Test
  void resolvesTransactionReportOnAllSystems(@TempDir Path root) throws IOException {
    Path folder = Files.createDirectories(root.resolve("data"));
    Files.writeString(folder.resolve("report.txt"), "report ok");

    Path report = ReportPathResolver.transactionReport(root);
    assertTrue(Files.exists(report), "Khong tim thay tep bao cao: " + report);
  }
}
