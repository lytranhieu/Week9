# Bài 5 – Giai đoạn 1: Đo coverage trước khi bổ sung test

Giữ nguyên **6 bài kiểm thử của Bài 4** và đã thêm plugin JaCoCo với ngưỡng Line Coverage 80%. Mục đích: chạy `mvn clean verify`, quan sát tỷ lệ thực tế, nếu dưới 80% thì lưu ảnh `BUILD FAILURE` và báo cáo JaCoCo. **Không khẳng định chắc chắn sẽ FAIL nếu chưa chạy trên Maven/GitHub.**

Cách chạy:

```powershell
mvn -f Bai05/pom.xml clean verify
```

Báo cáo: `Bai05/target/site/jacoco/index.html`. Workflow `.github/workflows/bai05-coverage.yml` được đặt ở gốc repository.

