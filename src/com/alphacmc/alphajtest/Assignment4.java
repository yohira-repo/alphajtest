package com.alphacmc.alphajtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

import com.alphacmc.alphajtest.bean.EmployeeBean;
import com.alphacmc.alphajtest.bean.ProductBean;

public class Assignment4 {

    private static final String FILE_NAME_ALL = "社員マスタ.csv";
    private static final String FILE_NAME_FULLTIME = "正社員マスタ.csv";
    private static final String FILE_NAME_CONTRACT = "契約社員マスタ.csv";

    // 正社員リスト
    private List<EmployeeBean> employeeFullList = new ArrayList<>();
    // 契約社員リスト
    private List<EmployeeBean> employeeContractList = new ArrayList<>();

    public static void main(String[] args) {
        Assignment4 assignment4 = new Assignment4();
        assignment4.getEmployeeList();
    }

    public void getEmployeeList() {
        List<ProductBean> products = new ArrayList<>();
        // テキストファイルの読み込みサンプル
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME_ALL)))
             ){
            //読み込み行
            String line;
            //読み込み行カウンタ
            int count = 0;

            //1行ずつ読み込みを行う(読込エリア line が NULL であれば、EOF 状態)
            while ((line = br.readLine()) != null) {
                //カウンタをインクリメント
                count++;
                //カンマで分割した内容を配列に格納する
                String[] data = line.split(",");
                if (data.length < 8) {
                    System.out.println("データ項目不足。count=" + count);
                    continue;
                }

                // 1行目はヘッダなのでスキップ
                if (count == 1) {
                    continue;
                }                
                
                // 社員IDのチェック
                String employeeId = data[0].trim();
                if (employeeId ==null || employeeId.isEmpty() || employeeId.length() != 4) {
                    System.out.println("社員IDが不正です。count=" + count);
                    continue;
                }
                if (!"C".equals(employeeId.substring(0,1))
                    && !"T".equals(employeeId.substring(0,1))) {
                        System.out.println("社員IDが不正です。count=" + count);
                        continue;
                }
                // 社員IDの数字部分をチェック 
                Integer employeeIdNo = checkInteger(employeeId.substring(1), 4);
                if (employeeIdNo == null) {
                    System.out.println("社員IDが不正です。count=" + count);
                    continue;
                }
                // 生年月日のチェック
                String birthDate = data[1].trim();
                if (birthDate == null || birthDate.isEmpty() || birthDate.length() != 10) {
                    System.out.println("生年月日が不正です。count=" + count);
                    continue;
                }
                if (!birthDate.matches("\\d{4}/\\d{2}/\\d{2}")) {
                    System.out.println("生年月日が不正です。count=" + count);
                    continue;
                }
                // 実在日付のチェック（簡易版）
                String[] birthDateParts = birthDate.split("/");
                int year = Integer.parseInt(birthDateParts[0]);
                int month = Integer.parseInt(birthDateParts[1]);
                int day = Integer.parseInt(birthDateParts[2]);
                if (month < 1 || month > 12 || day < 1 || day > 31) {
                    System.out.println("生年月日が不正です。count=" + count + "  社員コード=" + employeeId + "  生年月日=" + birthDate);
                    continue;
                }
                // 生年月日文字列を作成(-を抜く)
                birthDate = birthDate.replaceAll("/", "");
                
                // 性別のチェック
                String gender = data[2].trim();
                if (!"男".equals(gender) && "女".equals(gender)) {
                    System.out.println("性別が不正です。count=" + count + "  社員コード=" + employeeId + "  性別=" + gender);
                    continue;
                }
                // 性別区分の設定
                gender = ("男".equals(gender)) ? "0" : "1";

                // 入社年月日のチェック
                String joinDate = data[3].trim();
                if (joinDate == null || joinDate.isEmpty() || joinDate.length() != 10) {
                    System.out.println("入社年月日が不正です。count=" + count + "  社員コード=" + employeeId + "  入社年月日=" + joinDate);
                    continue;
                }
                // 入社年月日が実在日付かどうかのチェック（簡易版）
                String[] joinDateParts = joinDate.split("/");
                int joinYear = Integer.parseInt(joinDateParts[0]);
                int joinMonth = Integer.parseInt(joinDateParts[1]);
                int joinDay = Integer.parseInt(joinDateParts[2]);
                if (joinMonth < 1 || joinMonth > 12 || joinDay < 1 || joinDay > 31) {
                    System.out.println("入社年月日が不正です。count=" + count + "  社員コード=" + employeeId + "  入社年月日=" + joinDate);
                    continue;
                }
                // 入社年月日文字列を作成(-を抜く)
                joinDate = joinDate.replaceAll("/", "");

                // 郵便番号のチェック
                String postalCode = data[4].trim();
                if (postalCode == null || postalCode.isEmpty() || postalCode.length() != 7) {
                    System.out.println("郵便番号が不正です。count=" + count + "  社員コード=" + employeeId + "  郵便番号=" + postalCode);
                    continue;
                }
                if (!postalCode.matches("\\d{7}")) {
                    System.out.println("郵便番号が不正です。count=" + count + "  社員コード=" + employeeId + "  郵便番号=" + postalCode);
                    continue;
                }  

                // 住所のチェック
                String address = data[5].trim();
                address = checkString(address, 128);
                if (address == null) {
                    System.out.println("住所が不正です。count=" + count + "  社員コード=" + employeeId + "  住所=" + address);
                    continue;
                }

                // メールアドレスのチェック
                String emailAddress = data[6].trim();
                emailAddress = checkString(emailAddress, 128);
                if (emailAddress == null) {
                    System.out.println("メールアドレスが不正です。count=" + count + "  社員コード=" + employeeId + "  メールアドレス=" + emailAddress);
                    continue;
                }
                if (!emailAddress.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$")) {
                    System.out.println("メールアドレスが不正です。count=" + count + "  社員コード=" + employeeId + "  メールアドレス=" + emailAddress);
                    continue;
                }

                // 電話番号のチェック
                String phoneNumber = data[7].trim();
                phoneNumber = checkString(phoneNumber, 15);
                if (phoneNumber == null) {
                    System.out.println("電話番号が不正です。count=" + count + "  社員コード=" + employeeId + "  電話番号=" + phoneNumber);
                    continue;
                }
                // TODO:ハイフンありなし混在のチェック
                if (!phoneNumber.matches("\\d{2,4}-\\d{2,4}-\\d{4}")) {
                    System.out.println("電話番号が不正です。count=" + count + "  社員コード=" + employeeId + "  電話番号=" + phoneNumber);
                    continue;
                }
                // 電話番号のハイフンを除去
                phoneNumber = phoneNumber.replaceAll("-", "");

                EmployeeBean employee = new EmployeeBean();
                employee.setEmployeeId(employeeId);
                employee.setBirthDate(birthDate);
                employee.setGender(gender);
                employee.setJoinDate(joinDate);
                employee.setPostalCode(postalCode);
                employee.setAddress(address);
                employee.setEmailAddress(emailAddress);
                employee.setPhoneNumber(phoneNumber);

                // 正社員と契約社員で振り分ける
                if (!"C".equals(employeeId.substring(0,1))) {
                    // 正社員
                    employeeFullList.add(employee);
                } else {
                    // 契約社員
                    employeeContractList.add(employee);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeEmployeeList() {
        // 正社員リストの書き込み
        writeCSV(employeeFullList, FILE_NAME_FULLTIME);

        // 契約社員リストの書き込み
        writeCSV(employeeContractList, FILE_NAME_CONTRACT);
    }



    /**
     * 文字列桁数のチェック
     * @param str
     * @return
     */
    private String checkString(String str, int maxLength) {
        if (str == null || str.isEmpty() || str.length() > maxLength) {
            return null;
        }
        return str;
    }

    /**
     * 整数桁数のチェック
     * @param str
     * @return
     */
    private Integer checkInteger(String str, int maxLength) {
        if (str == null || str.isEmpty() || str.length() > maxLength) {
            return null;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
   
    private String employeeToCSV(EmployeeBean employee) {
        return String.join(",", employee.getEmployeeId(), employee.getBirthDate(), employee.getGender(),
                employee.getJoinDate(), employee.getPostalCode(), employee.getAddress(), employee.getEmailAddress(),
                   employee.getPhoneNumber());
    }

    private void writeCSV(List<EmployeeBean> employeeList, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // 社員コード順にソート
            employeeList.sort((employee1, employee2) -> employee1.getEmployeeId().compareTo(employee2.getEmployeeId()));
            // ヘッダ行の書き込み
            bw.write("社員ID,生年月日,性別,入社年月日,郵便番号,住所,メールアドレス,電話番号");
            bw.newLine();
            // データ行の書き込み
            for (EmployeeBean employee : employeeList) {
                bw.write(employeeToCSV(employee));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
