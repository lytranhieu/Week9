package com.practice.banksystem;

import java.nio.file.Path;

/** Ho tro xac dinh duong dan tep bao cao giao dich. */
public final class ReportPathResolver {
  private ReportPathResolver() {
  }

  /**
   * Tra ve duong dan tep bao cao giao dich.
   *
   * @param root Thu muc goc.
   * @return Duong dan tep bao cao giao dich.
   */
  public static Path transactionReport(Path root) {
    return root.resolve(Path.of("data", "report.txt"));
  }
}
