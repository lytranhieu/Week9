# Bài 2 – Chất lượng mã nguồn và Checkstyle (BankSystem)

## 1. Dự án ban đầu

File ZIP đầu vào gồm 9 file `.java`, **không có** `pom.xml`, mã kiểm thử hoặc `logback.xml`. Các file gốc được giữ nguyên trong thư mục `original/` để đối chiếu trước và sau khi tái cấu trúc.

## 2. Các thay đổi đã thực hiện

- Chuẩn hóa tất cả lớp vào package `com.practice.banksystem`. Trong bản gốc, chỉ `Account.java` khai báo package, dẫn đến lỗi không tìm thấy các lớp khi biên dịch cùng nhau.
- Sửa lỗi cú pháp ở `Transaction.java` (dòng bắt đầu bằng ký tự `/`); sử dụng các lệnh `import` cụ thể, thống nhất quy tắc đặt tên camelCase và UPPER_SNAKE_CASE, bổ sung JavaDoc, sửa dấu ngoặc, thụt lề và các dòng quá dài. Phương thức `get_type_string()` được đổi thành `getTypeString()`, các hằng `checking_type` và `savings_type` được đổi thành `CHECKING_TYPE` và `SAVINGS_TYPE`.
- Dùng `StringBuilder` khi tạo danh sách giao dịch và danh sách khách hàng.
- Tách phần phân tích tài khoản ra khỏi phương thức `Bank.readCustomerList()` để mã nguồn dễ đọc và bảo trì.
- Giữ nguyên quy tắc nghiệp vụ: từ chối số tiền không hợp lệ (`<= 0`), không cho rút quá số dư; tài khoản tiết kiệm được rút tối đa 1.000 đơn vị mỗi lần và phải duy trì số dư tối thiểu 5.000.
- Thay các câu lệnh in thông tin phục vụ theo dõi hoạt động bằng SLF4J và Logback.
- Không đưa trực tiếp số tài khoản, tên hoặc mã khách hàng, số tiền giao dịch hay số dư vào nội dung log. Ở các cảnh báo từ chối giao dịch, chỉ ghi loại ngoại lệ.

## 3. Tích hợp Maven Checkstyle

Dự án sử dụng Maven Checkstyle Plugin và bộ quy tắc Google Java Style để tự động kiểm tra định dạng, cách đặt tên, JavaDoc và các yêu cầu thống nhất mã nguồn. Plugin được tích hợp vào quá trình xây dựng dự án.

Chạy trong thư mục chứa `pom.xml`:

```powershell
mvn checkstyle:check
mvn clean verify
```

**Kết quả do người thực hiện xác nhận:** cả hai lệnh đều hiển thị `BUILD SUCCESS` sau khi chỉnh sửa và Việt hóa JavaDoc. Những kiểm tra này cho thấy dự án vượt qua các bước Maven/Checkstyle đã cấu hình, nhưng không thay thế việc kiểm tra hành vi nghiệp vụ thực tế.

## 4. Việt hóa JavaDoc

- Đã chuyển phần mô tả trong JavaDoc của mã Java thuộc `src/main/java` và `src/test/java` thành **tiếng Việt không dấu**, theo yêu cầu riêng của bài thực hành.
- Giữ nguyên các thẻ `@param`, `@return`, `@throws`, tên lớp, tên biến và hành vi nghiệp vụ.
- **Riêng file README này dùng tiếng Việt có dấu** để giải thích rõ ràng khi nộp bài.
- Thư mục `original/` vẫn giữ mã nguồn ban đầu để đối chiếu.

## 5. Tích hợp Logging với SLF4J và Logback

Dự án dùng **SLF4J** làm API ghi log và **Logback** làm thư viện thực thi, nhằm theo dõi sự kiện hệ thống mà không phải thêm nhiều lệnh `System.out.println()`.

### 5.1. Lý do chọn từng cấp độ log

| Cấp độ | Vị trí sử dụng trong mã nguồn | Lý do lựa chọn |
| --- | --- | --- |
| `INFO` | `CheckingAccount.deposit/withdraw`, `SavingsAccount.deposit/withdraw`, `Bank.readCustomerList` | Ghi nhận các mốc nghiệp vụ quan trọng khi xử lý thành công. Nhờ đó có thể biết hệ thống đang vận hành bình thường mà không phải bật log chi tiết. |
| `WARN` | Giao dịch bị từ chối do số tiền hoặc quy tắc tài khoản; đầu vào rỗng, bản ghi sai định dạng hoặc loại tài khoản chưa hỗ trợ | Đây là tình huống bất thường cần chú ý, nhưng chương trình vẫn có thể tiếp tục xử lý, nên không nhất thiết phải coi là lỗi hệ thống nghiêm trọng. |
| `ERROR` | `Bank.readCustomerList` khi gặp `IOException` hoặc `NumberFormatException` | Lỗi làm gián đoạn hoặc ngăn quá trình nhập dữ liệu hoàn tất bình thường; cần lưu thông tin ngoại lệ để xác định nguyên nhân. |
| `DEBUG` | Đọc và phân tích bản ghi khách hàng/tài khoản, tạo lịch sử hoặc tóm tắt giao dịch | Cung cấp thông tin kỹ thuật hỗ trợ gỡ lỗi. Không cần hiển thị thường xuyên vì dễ làm nhiễu log vận hành. |

### 5.2. Tại sao chọn những điểm ghi log này?

- **Giao dịch thành công (`INFO`):** Cho biết nghiệp vụ nạp hoặc rút tiền đã được xử lý, hỗ trợ kiểm tra luồng xử lý chính của ứng dụng.
- **Giao dịch bị từ chối (`WARN`):** Phân biệt yêu cầu không hợp lệ (chẳng hạn số tiền không hợp lệ hoặc vượt giới hạn rút) với sự cố kỹ thuật.
- **Quá trình nhập dữ liệu (`INFO`):** Xác nhận bước nhập danh sách khách hàng được thực hiện và hoàn thành; tham số `records={}` cho biết số lượng bản ghi sau xử lý.
- **Đầu vào thiếu hoặc không hợp lệ (`WARN`):** Ghi nhận rằng có dữ liệu không được chấp nhận mà không in nguyên văn dữ liệu khách hàng.
- **Lỗi đọc dữ liệu (`ERROR`):** Giúp truy vết lỗi I/O hoặc lỗi chuyển đổi dữ liệu. Khi chia sẻ log cần rà soát stack trace để tránh lộ thông tin nhạy cảm.
- **Phân tích nội bộ (`DEBUG`):** Cung cấp dấu mốc kỹ thuật trong khi chẩn đoán lỗi, chỉ cần bật khi có nhu cầu.

### 5.3. Những dữ liệu được ghi và không được ghi

**Có ghi:** thời gian (`%d`), tên luồng (`%thread`), cấp độ (`%level`), tên logger (`%logger`), nội dung sự kiện (`%msg`), số lượng bản ghi và tên loại ngoại lệ trong một số tình huống.

**Không ghi trực tiếp vào nội dung log:** số tài khoản, mã định danh hoặc tên khách hàng, số tiền và số dư. Việc này giúp giảm nguy cơ lộ dữ liệu nhạy cảm.

**Parameterized Logging:** Sử dụng các mẫu như `LOGGER.info("... records={}", customers.size())` và `LOGGER.warn("... {}", exception.getClass().getSimpleName())` thay vì nối chuỗi thủ công.

### 5.4. Cấu hình Logback và cách xác minh

File `src/main/resources/logback.xml` cấu hình hai appender:

- `CONSOLE`: ghi log ra Terminal.
- `FILE`: ghi log vào `bank.log` tại thư mục làm việc khi chạy ứng dụng.

Định dạng log gồm thời gian, thread, level, logger và nội dung thông điệp. Mặc định `<root level="INFO">` nên **không hiển thị `DEBUG`**; các mức `INFO`, `WARN`, `ERROR` có thể được ghi khi sự kiện tương ứng xảy ra. Khi cần chẩn đoán chi tiết, có thể tạm đổi sang `DEBUG`, sau đó trả về `INFO`.

Các lệnh kiểm tra:

```powershell
mvn checkstyle:check
mvn clean verify
Get-Content .\bank.log -Tail 30
```

**Lưu ý về minh chứng:** `BUILD SUCCESS` xác nhận quy trình kiểm tra đã chạy thành công; chỉ khi quan sát được thông điệp tương ứng trên Console hoặc trong `bank.log` mới có thể xác nhận Logging thực tế của từng luồng nghiệp vụ. File `bank.log` đã được thêm vào `.gitignore`.

## 6. Kiểm thử và giới hạn của bài thực hành

Các bài kiểm thử JUnit được bổ sung nhằm kiểm tra nạp/rút tiền, giới hạn tài khoản tiết kiệm, xử lý dữ liệu khách hàng và một số hành vi cốt lõi khác. Người thực hiện đã xác nhận quá trình `mvn clean verify` thành công trên máy của mình.

Đây là bài tập tái cấu trúc mã nguồn, **không phải hệ thống ngân hàng sẵn sàng vận hành thực tế**. Dự án vẫn dùng `double` cho tiền tệ, chưa có cơ sở dữ liệu và cơ chế bảo vệ giao dịch tương ứng với một hệ thống ngân hàng thật.

## 7. Kết luận

Bài 2 đã tích hợp Maven Checkstyle Plugin với Google Java Style, tái cấu trúc mã nguồn để đáp ứng quy tắc trình bày, bổ sung JavaDoc, triển khai SLF4J/Logback và các điểm ghi log theo cấp độ phù hợp. Theo kết quả chạy thực tế do người thực hiện cung cấp, `mvn checkstyle:check` và `mvn clean verify` đều `BUILD SUCCESS`. Việc quan sát log tại các tình huống nghiệp vụ vẫn cần được lưu làm minh chứng riêng khi nộp bài.
