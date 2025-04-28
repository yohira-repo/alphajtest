package com.alphacmc.alphajtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alphacmc.alphajtest.bean.EmployeeBean;
import com.alphacmc.alphajtest.bean.ProductBean;

public class Assignment4 {

    private static final String FILE_NAME_ALL = "C:\\Users\\alphauser\\git\\alphajtest\\data\\社員情報ファイル.csv";
    private static final String FILE_NAME_FULLTIME = "C:\\Users\\alphauser\\git\\alphajtest\\data\\正社員マスタ.csv";
    private static final String FILE_NAME_CONTRACT = "C:\\Users\\alphauser\\git\\alphajtest\\data\\契約社員マスタ.csv";

    // 正社員リスト
    private List<EmployeeBean> employeeFullList = new ArrayList<>();
    // 契約社員リスト
    private List<EmployeeBean> employeeContractList = new ArrayList<>();

    public static void main(String[] args) {
        Assignment4 assignment4 = new Assignment4();
        assignment4.getEmployeeList();
        assignment4.writeEmployeeList();
    }

    /**
     * 社員マスタのCSVファイルを読み込む
     */
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
                // ヘッダ行はスキップ
                if (count == 1) {
                    continue;
                }
                //カンマで分割した内容を配列に格納する
                String[] data = line.split(",");
                if (data.length < 8) {
                    System.out.println("データ項目不足。count=" + count);
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
                // 日付のチェック
                birthDate = checkDate(birthDate);
                if (birthDate == null) {
                    System.out.println("生年月日が不正です。count=" + count + "  社員コード=" + employeeId + "  生年月日=" + birthDate);
                    continue;
                }
                
                // 性別のチェック
                String gender = data[2].trim();
                if (!"男".equals(gender) && !"女".equals(gender)) {
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
                // 日付のチェック
                joinDate = checkDate(joinDate);
                if (joinDate == null) {
                    System.out.println("入社年月日が不正です。count=" + count + "  社員コード=" + employeeId + "  入社年月日=" + joinDate);
                    continue;
                }

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
                if (!phoneNumber.matches("\\d{2,4}-\\d{2,4}-\\d{4}") && !phoneNumber.matches("\\d{10,11}")) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 社員マスタのCSVファイルを出力する
     */
    public void writeEmployeeList() {
        // 正社員リストの書き込み
        System.out.println("正社員リストの書き込み開始 count=" + employeeFullList.size());
        writeCSV(employeeFullList, FILE_NAME_FULLTIME);

        // 契約社員リストの書き込み
        System.out.println("契約社員リストの書き込み開始 count=" + employeeContractList.size());
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

    /**
     * 日付のチェック
     * @param str
     * @return
     */
    private String checkDate(String str) {
        if (str == null || str.isEmpty() || str.length() != 10) {
            return null;
        }
        if (!str.matches("\\d{4}/\\d{2}/\\d{2}")) {
            return null;
        }
        // 実在日付のチェック
        try {
            String[] dateParts = str.split("/");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            Calendar cal = Calendar.getInstance();
            // 厳密な日付チェックを有効にする
            cal.setLenient(false); 
            // 月は0から始まるので-1する 
            cal.set(year, month - 1, day); 
            // 日付を取得して例外が発生しないか確認
            cal.getTime(); 
        } catch (IllegalArgumentException e) {
            return null; // 日付が不正な場合はnullを返す
        }
        return str.replaceAll("/", "");       
    }

    /**
     * 社員マスタのCSVファイルを出力する
     * @param employeeList
     * @param fileName
     */
    private void writeCSV(List<EmployeeBean> employeeList, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // 社員コード順にソート
            employeeList.sort((employee1, employee2) -> employee1.getEmployeeId().compareTo(employee2.getEmployeeId()));
            // ヘッダ行の書き込み
            bw.write("社員ID,生年月日,性別,入社年月日,郵便番号,住所,メールアドレス,電話番号");
            bw.newLine();
            // データ行の書き込み
            for (EmployeeBean employee : employeeList) {
                String line = String.join(",", employee.getEmployeeId(), employee.getBirthDate(), employee.getGender(),
                employee.getJoinDate(), employee.getPostalCode(), employee.getAddress(), employee.getEmailAddress(),
                   employee.getPhoneNumber());
                // 1行ずつ書き込み   
                bw.write(line);
                // 改行コードの書き込み
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
