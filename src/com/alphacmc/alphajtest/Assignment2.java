package com.alphacmc.alphajtest;

import java.util.Scanner;

import com.alphacmc.alphajtest.bean.ProductBean;

public class Assignment2 {

    // 商品マスタ
    private ProductBean[] productList = new ProductBean[100];
    // 商品マスタカウンター
    private int productCount = 0;
    // キーボード入力用
    public Scanner in = new Scanner(System.in);

    /**
     * 商品情報の登録と検索を行うメインメソッド
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        Assignment2 assignment = new Assignment2();
        assignment.consoleInput();
        assignment.seach();
        assignment.in.close();
    }
    /**
     * 商品情報の登録
     */
    public void consoleInput() {
        while(true) {
            // 商品名の入力
            System.out.print("商品名を入力してください: ");
            String input = this.in.nextLine();
            if (input.length() == 0) {
                System.out.println("再度入力してください。");
                continue;
            }
            String productName = input;
            // 商品価格の入力
            System.out.print("商品価格を入力してください: ");
            String inputPrice = this.in.nextLine();
            if (inputPrice.length() == 0) {
                System.out.println("再度入力してください。");
                continue;
            }
            // 商品価格の数値チェック
            Integer productPrice = checkNumber(inputPrice);
            if (productPrice == null) {
                System.out.println("数値を入力してください。");
                continue;
            }

            // 商品在庫の入力
            System.out.print("商品在庫を入力してください: ");
            String inputStock = this.in.nextLine();
            if (inputStock.length() == 0) {
                System.out.println("再度入力してください。");
                continue;
            }
            // 商品在庫の数値チェック
            Integer productStock = checkNumber(inputStock);
            if (productStock == null) {
                System.out.println("数値を入力してください。");
                continue;
            }

            // 商品名の重複チェック
            boolean isDuplicate = false;
            for (int i = 0; i < productCount; i++) {
                if (productList[i] != null && productList[i].getProductName().equals(productName)) {
                    isDuplicate = true;
                    System.out.println("この商品名はすでに存在します: " + productName);
                    break;
                }
            }
            if (isDuplicate) {
                continue;
            }
            // 商品情報の登録
            ProductBean product = new ProductBean();
            product.setProductName(productName);
            product.setProductPrice(productPrice.intValue());
            product.setProductStock(productStock.intValue());
            // 商品マスタに登録
            productList[productCount] = product;
            productCount++;
            System.out.println("商品情報を登録しました: " + productName);
            // 商品マスタの上限チェック
            if (productCount >= productList.length) {
                System.out.println("商品マスタの上限に達しました。");
                break;
            }
            // 登録継続確認
            System.out.print("商品登録を継続しますか？(y/n): ");
            String continueInput = in.nextLine();   
            if (continueInput.equalsIgnoreCase("y")) {
                System.out.println("登録を続けます。");
                continue;
            } else {
                System.out.println("登録を終了します。");
                break;
            }
        }
    }

    /**
     * 商品情報の検索
     */
    public void seach() {
        while(true) {
            System.out.print("検索する商品名を入力してください: ");
            String searchName = this.in.nextLine();
            if (searchName.length() == 0) {
                continue;
            }
            if ("END".equals(searchName)) {
                System.out.println("終了します。");
                break;
            }
            boolean found = false;
            for (int i = 0; i < productCount; i++) {
                if (productList[i] != null && productList[i].getProductName().startsWith(searchName)) {
                    System.out.println("商品名: " + productList[i].getProductName() +
                                       ", 価格: " + productList[i].getProductPrice() +
                                       ", 在庫: " + productList[i].getProductStock());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("商品が見つかりませんでした: " + searchName);
            }
        }
    }

    /**
     * 数値チェックメソッド
     * @param str
     * @return  数値に変換できた場合はその数値、できなかった場合はnull
     */
    Integer checkNumber(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
