# Bài 4 – Kiểm thử đa hệ điều hành với Matrix Strategy

## Mục tiêu

Từ dự án BankSystem đã hoàn thành ở Bài 3, sử dụng GitHub Actions Matrix Strategy để chạy cùng một bộ kiểm thử trên **Ubuntu, Windows và macOS**. Cố tình tạo lỗi đường dẫn phụ thuộc Windows, quan sát kết quả khác nhau theo hệ điều hành, sau đó sửa bằng API `java.nio.file.Path` và xác nhận cả ba môi trường đều chạy thành công.


## Cấu trúc

- `Bai04/pom.xml`: Maven/JUnit/Checkstyle/Logback kế thừa Bài 3.
- `Bai04/src/main/java/com/practice/banksystem/ReportPathResolver.java`: mã xác định đường dẫn tệp.
- `Bai04/src/test/java/com/practice/banksystem/ReportPathResolverTest.java`: kiểm thử sử dụng `@TempDir` và một tệp thật.
- `.github/workflows/bai04-matrix.yml`: workflow ở **gốc repository**, không nằm bên trong Bai04.

## Cấu hình Matrix

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

## Giai đoạn 1 – Cố tình gây lỗi


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

1. Tạo nhánh thực nghiệm `test/bai04-matrix`, không sửa trực tiếp `main`.
2. Commit và push lên GitHub. Mở **Actions → Bai 04 - Matrix OS Test**.


Khi làm xong, mở Pull Request từ `test/bai04-matrix` vào `main` (không merge khi còn lỗi). Workflow cũng được kích hoạt bởi `pull_request`.

## Giai đoạn 2 – Refactor để chạy đa nền tảng


```java
return root.resolve(Path.of("data", "report.txt"));
```

`Path.of` dùng quy ước phân cách của hệ điều hành hiện tại. Đổi duy nhất phần ghép đường dẫn; logic nghiệp vụ BankSystem và các bài test cũ giữ nguyên.

1. Mở GitHub Actions; cả ba runner phải báo **PASS** và không có test thất bại.
2. Kiểm tra PR cũng đã chạy lại trên ba OS. Có thể đóng PR sau khi lưu minh chứng; không bắt buộc merge PR thử nghiệm.
s

**Giải thích:** Cách viết `"data\\report.txt"` gắn chặt với cách phân tách đường dẫn trên Windows, nên không di động. `Path.of("data", "report.txt")` để Java tạo đường dẫn theo hệ điều hành, nhờ vậy tránh sự khác biệt khi chạy trên Windows và các hệ Unix.

Link: https://github.com/lytranhieu/Week9/actions/workflows/bai04-matrix.yml