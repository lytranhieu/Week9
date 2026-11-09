# Bài 3 – CI/CD Automation với GitHub Actions (BankSystem)

## Mục tiêu

Tái sử dụng BankSystem từ Bài 2 (Maven, SLF4J/Logback, JUnit Jupiter, Checkstyle) và tạo quy trình **CI** tự động kiểm tra quá trình xây dựng.


**Quan trọng:** `.github/workflows/bai03-ci.yml` phải ở **gốc GitHub repository Week9**, không nằm trong `Bai03/.github/`. Các đường dẫn trong workflow dùng đúng cấu trúc này. Không ghi đè file của bài tập khác khi giải nén.

## Nội dung workflow

File: `.github/workflows/bai03-ci.yml`.

- `on: [push, pull_request]`: khi push hoặc mở/cập nhật Pull Request, GitHub Actions kích hoạt quy trình.
- `permissions: contents: read`: workflow chỉ xin quyền đọc repository.
- `runs-on: ubuntu-latest`: chạy trên Runner Ubuntu.
- `defaults.run.working-directory: Bai03`: các bước `run` thực thi trong thư mục chứa `Bai03/pom.xml`.
- `actions/checkout@v7`: lấy mã nguồn về Runner.
- `actions/setup-java@v6`: chuẩn bị JDK Temurin 17.
- `mvn --batch-mode --no-transfer-progress clean package`: Maven chạy `clean`, sau đó đi qua các phase của default lifecycle, **bao gồm `validate` (Checkstyle theo POM), `compile`, `test` (JUnit), `package` (JAR)**. Nếu test hoặc Checkstyle thất bại, lệnh thất bại và bước upload thông thường không được chạy.
- `actions/upload-artifact@v7`: lưu `Bai03/target/bank-system-1.0-SNAPSHOT.jar`; `if-no-files-found: error` làm job thất bại nếu thiếu JAR.


## Đẩy GitHub và kiểm tra PASS


```powershell
git add Bai03 .github/workflows/bai03-ci.yml
git commit -m "ci: add BankSystem Maven verification workflow"
git push origin main
```

## Kiểm chứng CI FAIL, đọc log và sửa lại

Thực hiện trên **nhánh thực hành** riêng, KHÔNG sửa `main` đang hoạt động:

1. Tạo nhánh: `git switch -c test/ci-failure`.
2. Mở `Bai03/src/test/java/com/practice/banksystem/BankSystemTest.java`.
3. Trong bài test `checkingDepositAndWithdrawalKeepTransactionHistory`, cố ý đổi **một assertion** từ `assertEquals(1150.0, account.getBalance(), 1e-9);` sang `assertEquals(1151.0, account.getBalance(), 1e-9);`. Chỉ thay đổi trong bản thực hành tạm thời.
4. Commit và push nhánh thực hành; mở Pull Request (ví dụ vào `dev` hoặc `main`) để GitHub Actions chạy.
5. Vào Actions, mở Job `build`, Step `Maven test and package`. Chụp ảnh dòng `expected: <1151.0> but was: <1150.0>` hoặc log kiểm thử tương ứng và `BUILD FAILURE`. Kiểm tra không có JAR artifact mới cho run thất bại.
6. Khôi phục assertion `1150.0`, commit `test: restore expected checking balance`, push lại.
7. Xác nhận pipeline trở về màu xanh. Chụp ảnh kết quả PASS và phần Artifacts.

**Mẫu giải thích lỗi:** Test kỳ vọng sai (`1151.0`) so với hành vi đúng của chương trình (`1150.0`). Maven Surefire phát hiện assertion thất bại và dừng lifecycle trước `package`. Sửa kết quả mong đợi về `1150.0` giúp test chạy thành công và tạo lại JAR. Việc cố tình gây lỗi nhằm kiểm tra khả năng quan sát và xử lý lỗi CI, không phải sửa nghiệp vụ ngân hàng.

## Minh chứng
https://github.com/lytranhieu/Week9/actions/workflows/bai03-ci.yml

## Kết luận
GitHub Actions giúp kiểm tra Build và Test tự động trên Runner Ubuntu, không chỉ phụ thuộc môi trường máy local. Artifact JAR được lưu để tải xuống xác minh. Khi có lỗi Unit Test, log giúp xác định nguyên nhân và sửa trước khi merge.
