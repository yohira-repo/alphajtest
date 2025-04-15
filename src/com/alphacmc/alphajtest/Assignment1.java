package com.alphacmc.alphajtest;

import java.util.Calendar;
import java.util.Scanner;

public class Assignment1 {
    public static void main(String[] args) {
        
        Assignment1 assignment = new Assignment1();
        assignment.consoleInput();        
    }

    public void consoleInput() {
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.print("生年月日を入力してください: ");
            String input = in.nextLine();
            if (input.length() == 0) {
                System.out.println("終了します。");
                break;
            }
            // '/' のチェック　yyyy/MM/dd
            if (input.length() != 10 
            || !input.substring(4,5).equals("/") 
            || !input.substring(7,8).equals("/")) {
                System.out.println("Invalid date format. Please enter in YYYY/MM/DD format.");
                continue;
            }
            // 年月日の文字列を分割
            String inYear = input .substring (0,4);
            String inMonth = input.substring(5,7);
            String inDay = input.substring(8,10);
            try {
                int birthYear = Integer.parseInt(inYear);
                int birthMonth = Integer.parseInt(inMonth);
                int birthDay = Integer.parseInt(inDay);
                // 年月日の範囲チェック
                if (birthMonth < 1 || birthMonth > 12) {
                    System.out.println("Invalid month. Please enter a month between 1 and 12.");
                    continue;
                }
                int age = calcYearsOld(birthYear, birthMonth, birthDay);
                System.out.println("あなたの年齢は " + age + " 歳です。");
            } catch (NumberFormatException e) {
                System.out.println("Invalid date format. Please enter in YYYY/MM/DD format.");
            }
        }
        in.close();
    }

    /**
     * Calculate the age based on the birth date
     * @param birthYear
     * @param birthMonth
     * @param birthDay
     * @return
     */
    private int calcYearsOld(int birthYear, int birthMonth, int birthDay) {
        // Get the current date
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        int age = currentYear - birthYear;
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--;
        }
        return age;
    }
}
