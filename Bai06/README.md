# Bài 6 – Tối ưu CI/CD bằng Maven Dependency Caching

## 1. Mục tiêu

Áp dụng cơ chế `cache: 'maven'` trong GitHub Actions để tái sử dụng các thư viện đã được tải về, hạn chế tải lặp lại từ Maven Central. Dự án sử dụng lại mã nguồn BankSystem với Maven, JUnit 5, Checkstyle và JaCoCo (ngưỡng Line Coverage >= 80%) của Bài 5.

## 2. Cấu hình

Workflow đặt tại **gốc repository**: `.github/workflows/bai06-cache.yml`.

```yaml
- name: Set up JDK 17 and Maven cache
  uses: actions/setup-java@v6
  with:
    distribution: temurin
    java-version: '17'
    cache: 'maven'
    cache-dependency-path: 'Bai06/pom.xml'
    cache-jdk: false
```

- `cache: 'maven'`: bật cache Maven Local Repository (`~/.m2/repository`).
- `cache-dependency-path`: dùng riêng `Bai06/pom.xml` làm đầu vào tính khóa cache, tránh ảnh hưởng từ POM của các bài khác trong Week9.
- `cache-jdk: false`: tắt cache JDK, giúp việc so sánh tập trung hơn vào Maven dependencies.
- `mvn clean verify`: kiểm tra biên dịch, Checkstyle, Unit Test và JaCoCo như các bài trước.

## 3. Cách chạy và ghi minh chứng

**Lần push thứ nhất (cache lạnh / miss):** Push `Bai06/` và file workflow lên một nhánh Git. Trong Actions, mở bước **Set up JDK 17 and Maven cache** để xem trạng thái cache (có thể xuất hiện `Cache not found for input keys`). Chờ workflow thành công để cache có cơ hội được lưu. Ghi lại `Total duration` và thời gian step Maven.

**Lần push thứ hai (cache ấm / hit):** Chỉ thêm một dòng bất kỳ cuối `Bai06/README.md`, sau đó commit/push **trên cùng nhánh**, không thay đổi `pom.xml`. Mở lần chạy mới và tìm các thông báo dạng `Cache restored from key` hoặc `Cache hit occurred on the primary key`. Ghi lại thời gian chạy và so sánh.

> Lưu ý: Hai lần chạy trên là so sánh **trước và sau khi cache được làm nóng** (cache MISS vs HIT). Nếu giảng viên yêu cầu số liệu khi **chưa bật caching**, cần có thêm lần chạy baseline không khai báo `cache: maven`, rồi so với lần cache HIT. Dùng cùng Runner OS, lệnh Maven, POM và điều kiện gần tương đương để so sánh.

**Chứng minh bằng logs:** Trong bước `Set up JDK 17 and Maven cache`, lưu ảnh trạng thái cache MISS và HIT. Trong bước `Maven clean verify`, quan sát việc tải dependencies có giảm đi; cache HIT không bảo đảm hoàn toàn không có kết nối mạng hoặc thời gian chạy chắc chắn ngắn hơn mọi lần.

## 4. Kết quả thực tế (điền sau khi chạy)

| Lần chạy | Trạng thái cache | Tổng thời gian | Thời gian bước Maven | Kết quả |
|---|---|---|---|---|
| Push 1 | MISS / chưa có cache | ... giây | ... giây | ... |
| Push 2 | HIT / khôi phục cache | ... giây | ... giây | ... |

## 5. Giải thích giải pháp

GitHub Actions sử dụng máy chạy tạm thời. Nếu không có dependency caching, Maven có thể phải tải lại nhiều thư viện giữa các workflow runs. `actions/setup-java` hỗ trợ khôi phục và lưu Maven Local Repository dưới dạng cache; khóa cache phụ thuộc vào hệ điều hành và nội dung tệp POM. Sau khi cache được lưu thành công, các lần chạy phù hợp có thể tái sử dụng dependencies. Điều này có thể rút ngắn thời gian tải thư viện và giảm truy cập Maven Central. Để chứng minh hiệu quả, đối chiếu logs MISS/HIT và thời gian thực thi trong hai lần chạy trên GitHub Actions.

## 6. File nộp

- Thư mục `Bai06/` chứa mã nguồn, `pom.xml`, README.
- File `.github/workflows/bai06-cache.yml` đặt ở gốc repository.
- Ảnh màn hình GitHub Actions về lần chạy MISS, HIT và thời gian chạy.
- Link repository hoặc workflow run nếu giảng viên yêu cầu.

Không cần commit thư mục `target/` hoặc file `.log`.
