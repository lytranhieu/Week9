# Bài 7 – Automated Code Review qua Pull Request

## Mục tiêu

Tự động kiểm tra Google Java Style khi tạo hoặc cập nhật Pull Request (PR) vào nhánh `main`. Bot đăng nhận xét trực tiếp ở những dòng vi phạm mới trong PR; Maven cũng chạy Checkstyle, Unit Test và JaCoCo. Branch Protection yêu cầu các status check thành công mới cho phép merge.

## Cấu trúc và cấu hình

- Dự án `Bai07/` kế thừa BankSystem của Bài 6, test và mức JaCoCo 80%.
- Workflow **bắt buộc ở gốc repository**: `.github/workflows/bai07-review.yml`.
- Workflow chỉ kích hoạt với sự kiện `pull_request` có nhánh đích là `main`.
- Job `bai07-inline-checkstyle` dùng `dbelyaev/action-checkstyle@v3`, `reporter: github-pr-review`, `filter_mode: added`, `level: error` và `fail_level: error` để đưa nhận xét vào dòng code mới sửa và báo fail khi có vi phạm.
- Job `bai07-maven-verify` chạy `mvn -f Bai07/pom.xml clean verify` để kiểm tra Checkstyle + JUnit + JaCoCo.
- `pull-requests: write` là quyền cần thiết để action đăng bình luận PR; chỉ dùng nhánh trong cùng repository để token có quyền ghi (không dùng PR từ fork không đáng tin).

## Thực hành một PR lỗi → sửa

**Trên nhánh chính:** giữ nguyên các bài trước. Mở Terminal tại thư mục gốc `Week9`:

```powershell
git switch main
git pull origin main
git switch -c test/bai07-review
```

**Tạo lỗi cố ý, không làm hỏng logic:** Trong `Bai07/src/main/java/com/practice/banksystem/ReportPathResolver.java`, ngay sau `import java.nio.file.Path;`, thêm dòng không sử dụng:

```java
import java.util.Date;
```

Đây là lỗi `UnusedImports` theo Google Checks. **Lưu file** và commit cả workflow + Bai07:

```powershell
git add Bai07 .github/workflows/bai07-review.yml
git commit -m "ci: demonstrate automated Checkstyle PR review"
git push -u origin test/bai07-review
```

Trên GitHub chọn **Pull requests → New pull request**: `base: main`, `compare: test/bai07-review`, rồi tạo PR. Workflow sẽ được kích hoạt bởi `pull_request`, không phải chỉ bởi `push`.

**Lưu bằng chứng:** trong PR, mở **Files changed** để xem bot bình luận vào dòng `import java.util.Date;`; tab **Checks** để xem status job. Nếu chưa thấy comment: mở logs của `bai07-inline-checkstyle` để xem token permission và filter, vì comment phụ thuộc quyền ghi và dòng mới trong diff. Chụp ảnh các lỗi và status FAIL.

**Thiết lập chặn merge:** Vào repo **Settings → Branches → Add branch protection rule**, chọn `main`, bật **Require a pull request before merging** và **Require status checks to pass before merging**. Chọn các check đã xuất hiện `bai07-maven-verify` và `bai07-inline-checkstyle`. Nếu nhánh `main` đã có ruleset, điều chỉnh ruleset tương ứng thay vì tạo quy tắc xung đột. Hãy bật **Do not allow bypassing** nếu có và cần kiểm chứng quyền admin. Nếu không thấy chức năng Branch Protection, kiểm tra quyền quản trị và gói GitHub của repo.

**Sửa lỗi:** xóa dòng `import java.util.Date;`, sau đó:

```powershell
mvn -f Bai07/pom.xml clean verify
git add Bai07/src/main/java/com/practice/banksystem/ReportPathResolver.java
git commit -m "fix: remove unused import detected by Checkstyle"
git push origin test/bai07-review
```

## Giải thích giải pháp

Workflow tự chạy khi PR hướng đến `main` được mở hoặc cập nhật. `dbelyaev/action-checkstyle` kiểm tra mã Java với Google Checks và dùng reviewdog để nhận xét trực tiếp vào các dòng code được thêm/sửa. Đồng thời Maven `verify` chạy Checkstyle, JUnit và kiểm tra ngưỡng JaCoCo 80%, do đó code chưa đạt chuẩn có thể làm job thất bại.

Branch Protection liên kết các status check với điều kiện merge. Nếu có lỗi định dạng hoặc Unit Test thất bại, PR sẽ bị chặn merge khi các status check được cấu hình là bắt buộc. Sau khi sửa lỗi và push lên cùng nhánh, workflow chạy lại và PR có thể đạt điều kiện merge.
