# Bài 4 – Kiểm thử đa hệ điều hành với Matrix Strategy

## 1. Mục tiêu

Từ dự án BankSystem đã hoàn thành ở Bài 3, sử dụng GitHub Actions Matrix Strategy để chạy cùng một bộ kiểm thử trên **Ubuntu, Windows và macOS**. Cố tình tạo lỗi đường dẫn phụ thuộc Windows, quan sát kết quả khác nhau theo hệ điều hành, sau đó sửa bằng API `java.nio.file.Path` và xác nhận cả ba môi trường đều chạy thành công.

> **Lưu ý về bài nộp:** Giữ nguyên JavaDoc không dấu trong mã Java. README này có dấu. Không thay đổi nghiệp vụ ngân hàng.

## 2. Cấu trúc

- `Bai04/pom.xml`: Maven/JUnit/Checkstyle/Logback kế thừa Bài 3.
- `Bai04/src/main/java/com/practice/banksystem/ReportPathResolver.java`: mã xác định đường dẫn tệp.
- `Bai04/src/test/java/com/practice/banksystem/ReportPathResolverTest.java`: kiểm thử sử dụng `@TempDir` và một tệp thật.
- `.github/workflows/bai04-matrix.yml`: workflow ở **gốc repository**, không nằm bên trong Bai04.

## 3. Cấu hình Matrix

```yaml
strategy:
  fail-fast: false
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
runs-on: ${{ matrix.os }}
```

- `matrix.os`: tạo ba job kiểm tra, mỗi job có một runner khác nhau.
- `runs-on`: chọn hệ điều hành của runner.
- `fail-fast: false`: không hủy các job còn lại khi một job thất bại; giúp thu đủ minh chứng so sánh.
- `working-directory: Bai04`: vì dự án Maven nằm trong thư mục `Bai04` của repository Week9.
- `mvn clean verify`: chạy qua các phase cần thiết, bao gồm kiểm thử và đóng gói.

## 4. Giai đoạn 1 – Cố tình gây lỗi

**Bản lỗi (file Java của ZIP `Bai04_Matrix_Stage1_FAIL.zip`)** sử dụng:

```java
return root.resolve("data\\report.txt");
```

Đây là chuỗi chứa dấu phân cách cứng của Windows. Trên Windows, `\` được hiểu là dấu phân cách thư mục; trên Ubuntu/macOS dấu này là một phần của tên file. Bài test tạo `data/report.txt` bằng cách dùng `Path.resolve` và kiểm tra file tồn tại. Do đó kỳ vọng:

| Runner | Kết quả dự kiến | Giải thích |
|---|---|---|
| `windows-latest` | PASS | Windows diễn giải `\` như dấu phân cách |
| `ubuntu-latest` | FAIL | Đường dẫn có `\` không trỏ đến file đã tạo |
| `macos-latest` | FAIL | Tương tự hệ thống Unix |

**Cách thực hiện:**

1. Giải nén `Bai04_Matrix_Stage1_FAIL.zip` vào máy, sao chép `Bai04/` và `.github/workflows/bai04-matrix.yml` vào **gốc repository Week9**.
2. Tạo nhánh thực nghiệm `test/bai04-matrix`, không sửa trực tiếp `main`.
3. Commit và push lên GitHub. Mở **Actions → Bai 04 - Matrix OS Test**.
4. Lưu ảnh ba job và đoạn log thất bại của `ReportPathResolverTest`. Đây là lỗi tạo có chủ đích, **không nộp bản lỗi làm bản hoàn thiện**.

Ví dụ lệnh khi đã đứng trong repo Week9:

```powershell
git switch main
git pull origin main
git switch -c test/bai04-matrix
git add Bai04 .github/workflows/bai04-matrix.yml
git commit -m "test: reproduce Windows-only file path in matrix"
git push -u origin test/bai04-matrix
```

Khi làm xong, mở Pull Request từ `test/bai04-matrix` vào `main` (không merge khi còn lỗi). Workflow cũng được kích hoạt bởi `pull_request`.

## 5. Giai đoạn 2 – Refactor để chạy đa nền tảng

**Bản sửa (file Java của ZIP `Bai04_Matrix_Stage2_FIXED.zip`)** dùng:

```java
return root.resolve(Path.of("data", "report.txt"));
```

`Path.of` dùng quy ước phân cách của hệ điều hành hiện tại. Đổi duy nhất phần ghép đường dẫn; logic nghiệp vụ BankSystem và các bài test cũ giữ nguyên.

1. Tải `Bai04_Matrix_Stage2_FIXED.zip`, thay **thư mục Bai04** đang thử nghiệm bằng phiên bản đã sửa, giữ nguyên workflow.
2. Chạy tại thư mục gốc repository:

```powershell
mvn -f Bai04/pom.xml clean verify
git add Bai04
git commit -m "fix: make report path portable across operating systems"
git push origin test/bai04-matrix
```

3. Mở GitHub Actions; cả ba runner phải báo **PASS** và không có test thất bại. Lưu ảnh và log.
4. Kiểm tra PR cũng đã chạy lại trên ba OS. Có thể đóng PR sau khi lưu minh chứng; không bắt buộc merge PR thử nghiệm.

## 6. Kết quả cần nộp và nhận xét

Minh chứng nên có:

- Ảnh workflow Matrix thể hiện 3 hệ điều hành.
- Ảnh trạng thái FAIL chỉ ở Ubuntu/macOS và PASS ở Windows trong giai đoạn cố tình gây lỗi.
- Ảnh log lỗi `ReportPathResolverTest` chứng minh không tìm được đường dẫn theo cú pháp cứng Windows.
- Ảnh cả 3 runner PASS sau khi sửa thành `Path.of`.
- Link repository, link Pull Request và link hai lần chạy GitHub Actions.

**Giải thích:** Cách viết `"data\\report.txt"` gắn chặt với cách phân tách đường dẫn trên Windows, nên không di động. `Path.of("data", "report.txt")` để Java tạo đường dẫn theo hệ điều hành, nhờ vậy tránh sự khác biệt khi chạy trên Windows và các hệ Unix.

## 7. Trạng thái xác minh

Các ví dụ được chuẩn bị bằng cách kế thừa mã nguồn Bài 3; đây là kỳ vọng kết quả cần kiểm chứng trong GitHub Actions, không phải kết quả CI thực tế đã được chạy. `BUILD SUCCESS` chỉ được kết luận sau khi xem log từ từng runner.
