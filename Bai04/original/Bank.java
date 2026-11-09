import java.io.*; // Vi phạm: Wildcard import
import java.util.*; // Vi phạm: Wildcard import

public class Bank {
    // Vi phạm: Tên biến không rõ nghĩa, viết tắt sai chuẩn camelCase
    private List<Customer> c_list; 

    public Bank() {
        this.c_list = new ArrayList<Customer>();
    }

    public List<Customer> getCustomerList() {
        return c_list;
    }

    // Vi phạm: Thụt đầu dòng (Indentation) lung tung và Javadoc thiếu tag @param
    /**
     * Set danh sach khach hang
     */
    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.c_list = new ArrayList<Customer>();
        } else {
            this.c_list = customerList;
        }
    }

    /**
     * Ham nay rat dai và khó doc
     */
    public void readCustomerList(InputStream inputStream) {
        // Vi phạm: Log trực tiếp bằng System.out
        System.out.println("DEBUG: Bat dau doc du lieu..."); 
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            Customer current = null;
            try {
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) { // Vi phạm: Lồng nested if quá sâu
                        int last = line.lastIndexOf(' ');
                        if (last > 0) {
                            String token = line.substring(last + 1).trim();
                            // Vi phạm: Dùng biểu thức chính quy trực tiếp (Magic String)
                            if (token.matches("\\d{9}")) {
                                String name = line.substring(0, last).trim();
                                current = new Customer(Long.parseLong(token), name);
                                c_list.add(current);
                                System.out.println("Log: Them khach hang " + name);
                            } else {
                                if (current != null) {
                                    String[] parts = line.split("\\s+");
                                    if (parts.length >= 3) {
                                        long num = Long.parseLong(parts[0]);
                                        double bal = Double.parseDouble(parts[2]);
                                        // Vi phạm: So sánh String dùng toán tử == (nếu lỡ tay) hoặc không xử lý hằng số
                                        if (parts[1].equals("CHECKING")) {
                                            current.addAccount(new CheckingAccount(num, bal));
                                        } else if (parts[1].equals("SAVINGS")) {
                                            current.addAccount(new SavingsAccount(num, bal));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) { // Vi phạm: Catch Exception chung chung
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public String getCustomersInfoByIdOrder() {
        // Vi phạm: Dùng Anonymous class thay vì Lambda, thụt lề sai
        Collections.sort(c_list, new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                return Long.compare(o1.getIdNumber(), o2.getIdNumber());
            }
        });

        // Vi phạm: Cộng chuỗi (String concatenation) trong vòng lặp - Cực tệ cho performance
        String res = "";
        for (int i = 0; i < c_list.size(); i++) {
            res += c_list.get(i).getCustomerInfo(); 
            if (i < c_list.size() - 1) res += "\n";
        }
        return res;
    }

    public String getCustomersInfoByNameOrder() {
        // Vi phạm: Logic trùng lặp nhiều với hàm trên (Code Duplication)
        List<Customer> copy = new ArrayList<Customer>(c_list);
        Collections.sort(copy, (c1, c2) -> {
            int res = c1.getFullName().compareTo(c2.getFullName());
            return res != 0 ? res : Long.compare(c1.getIdNumber(), c2.getIdNumber());
        });

        // Vi phạm: Dòng code quá dài, không ngắt dòng
        StringBuilder sb = new StringBuilder(); for (Customer c : copy) { sb.append(c.getCustomerInfo()).append("\n"); }
        return sb.toString().trim();
    }
}