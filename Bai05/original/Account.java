package bank_system;
import java.util.*; // Vi phạm: Wildcard import (nên import cụ thể List, ArrayList)
import java.io.*;   // Vi phạm: Import thừa, không sử dụng

/**
 * Lớp này đại diện cho tài khoản nhưng viết Javadoc rất sơ sài và sai format
 */
public abstract class Account {
    // Vi phạm: Đặt tên hằng số không đúng chuẩn (phải là UPPER_SNAKE_CASE)
    public static final String checking_type = "CHECKING";
    public static final String savings_type = "SAVINGS";

    // Vi phạm: Tên biến instance bắt đầu bằng dấu gạch dưới hoặc quá ngắn, không rõ nghĩa
    private long _accNum; 
    private double B; 
    protected List<Transaction> list; 

    // Vi phạm: Thụt lề (Indentation) không đồng nhất, không dùng 2 spaces theo chuẩn Google
    public Account(long accountNumber, double balance) {
        this._accNum = accountNumber;
        this.B = balance;
        this.list = new ArrayList<Transaction>();
    }

    // Vi phạm: Viết hàm trên một dòng, thiếu khoảng trắng giữa các toán tử/ngoặc
    public long getAccountNumber(){return _accNum;}

        public void setAccountNumber(long accountNumber) {
            _accNum = accountNumber;
        }

        public double getBalance() {
            return B;
        }

        protected void setBalance(double balance) {
            this.B = balance;
        }

        public List<Transaction> getTransactionList() {
            return list;
        }

        public void setTransactionList(List<Transaction> transactionList) {
            // Vi phạm: Thiếu dấu ngoặc nhọn cho câu lệnh if (mặc dù vẫn chạy đúng)
            if (transactionList == null) 
                this.list = new ArrayList<Transaction>();
            else 
                this.list = transactionList;
        }

        // Vi phạm: Thiếu Javadoc cho phương thức public
        public abstract void deposit(double amount);

        public abstract void withdraw(double amount);

        protected void doDepositing(double amount) throws InvalidFundingAmountException {
            // Vi phạm: Whitespace quanh toán tử (amount<=0)
            if (amount<=0) {
                throw new InvalidFundingAmountException(amount);
            }
            B += amount;
        }

        protected void doWithdrawing(double amount) throws Exception { 
            // Vi phạm: Tung ra Exception quá chung chung thay vì Exception cụ thể
            if (amount <= 0) throw new InvalidFundingAmountException(amount);
            if (amount > B) throw new InsufficientFundsException(amount);
            B -= amount;
        }

        public void addTransaction(Transaction transaction) {
            if (transaction != null) {
                list.add(transaction);
            }
        }

        public String getTransactionHistory() {
            // Vi phạm: Dòng code quá dài (Line length) và dùng cộng chuỗi trong vòng lặp (Performance smell)
            String s = "Lịch sử giao dịch của tài khoản " + _accNum + ":\n"; 
            for (int i = 0; i < list.size(); i++) {
                s += list.get(i).getTransactionSummary(); // Vi phạm: Không dùng StringBuilder
                if (i < list.size() - 1) s += "\n";
            }
            // Vi phạm: In log trực tiếp ra console để debug (Thay vì dùng Logger)
            System.out.println("[DEBUG] Đã lấy lịch sử cho tài khoản: " + _accNum); 
            return s;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Account)) return false;
            Account other = (Account) obj;
            return this._accNum == other._accNum;
        }

        @Override
        public int hashCode() {
            // Vi phạm: Format code lộn xộn
            return (int) (_accNum ^ (_accNum >>> 32));
        }
    }