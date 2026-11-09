### Bài 1: Quản lý Dependency với Maven

**1. Nâng cấp cấu hình Maven**

Dự án được nâng cấp từ Java 8 lên Java 17 để tương thích với các thư viện mới. Đồng thời, Maven Compiler Plugin được nâng cấp lên phiên bản 3.13.0 nhằm hỗ trợ quá trình biên dịch.

**2. Bổ sung và quản lý Dependencies**

- **Logback Classic 1.4.11:** Bổ sung framework Logging, sử dụng SLF4J để ghi nhận hoạt động của chương trình thay cho cách in thông tin thủ công bằng `System.out.println()`.
- **Hibernate Core 6.2.0.Final:** Bổ sung thư viện ORM, hỗ trợ khả năng làm việc với cơ sở dữ liệu trong tương lai.
- **JUnit Jupiter 5.9.2:** Thay thế JUnit 4.10 bằng JUnit 5. Sử dụng `scope=test` vì thư viện chỉ cần thiết trong quá trình biên dịch và thực thi kiểm thử.

Ngoài ra, Maven Surefire Plugin 3.2.5 được cấu hình để hỗ trợ chạy Unit Test bằng JUnit Jupiter.

**3. Kiểm tra và xử lý Dependencies**

Sử dụng lệnh `mvn dependency:tree` để kiểm tra các thư viện phụ thuộc. Kết quả cho thấy Maven đã sử dụng đúng ba thư viện bắt buộc và không phát hiện xung đột phiên bản rõ ràng trong cây dependency đã giải quyết.

Các thư viện cũ vẫn được giữ lại nhằm tránh ảnh hưởng đến mã nguồn hiện có. Việc nâng cấp chúng cần được kiểm tra tương thích riêng.

**4. Kiểm thử và xác minh Build**

Sử dụng các lệnh:

- `mvn dependency:tree`: Kiểm tra cây thư viện phụ thuộc.
- `mvn clean verify`: Biên dịch, kiểm thử và xác minh quá trình build.

Kết quả thực tế:

- **BUILD SUCCESS.**
- **Tests run: 17.**
- **Failures: 0.**
- **Errors: 0.**
- **Skipped: 0.**

**5. Kết luận**

Dự án MathUtils đã được nâng cấp cấu hình Maven, bổ sung các thư viện theo yêu cầu và chuyển hệ thống kiểm thử sang JUnit Jupiter 5.9.2. Kết quả 17/17 Unit Test thành công, quá trình Maven Build hoàn tất và các dependency bắt buộc được giải quyết đúng phiên bản.

Cấu hình Logback đã được bổ sung để hỗ trợ Logging có cấu trúc. Việc thay thế hoàn toàn các lệnh in thủ công cần được xác nhận bằng cách kiểm tra mã nguồn Java.