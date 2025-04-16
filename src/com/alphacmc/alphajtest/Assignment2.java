package com.alphacmc.alphajtest;

import java.util.Scanner;

import com.alphacmc.alphajtest.bean.ProductBean;

public class Assignment2 {

    // 商品マスタ
    private ProductBean[] productList = new ProductBean[100];
    // 商品マスタカウンター
    private int productCount = 0;

    public static void main(String[] args) {
        Assignment2 assignment = new Assignment2();
        assignment.consoleInput();
        assignment.seach();

    }
    /**
     * 商品情報の登録
     */
    public void consoleInput() {
        Scanner in = new Scanner(System.in);
        while(true) {
            // 商品名の入力
            System.out.print("商品名を入力してください: ");
            String input = in.nextLine();
            if (input.length() == 0) {
                System.out.println("再度入力してください。");
                continue;
            }
            String productName = input;
            // 商品価格の入力
            System.out.print("商品価格を入力してください: ");
            String inputPrice = in.nextLine();
            if (inputPrice.length() == 0) {
                System.out.println("再度入力してください。");
                continue;
            }
            // TODO:商品価格の数値チェック
            int productPrice = Integer.parseInt(inputPrice);

            // 商品在庫の入力
            System.out.print("商品在庫を入力してください: ");
            String inputStock = in.nextLine();
            if (inputStock.length() == 0) {
                System.out.println("再度入力してください。");
                continue;
            }
            // TODO:商品在庫の数値チェック
            int productStock = Integer.parseInt(inputStock);

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
            product.setProductPrice(productPrice);
            product.setProductStock(productStock);
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
            System.out.print("商品情報を登録しますか？(y/n): ");
            String continueInput = in.nextLine();   
            if (continueInput.equalsIgnoreCase("y")) {
                System.out.println("登録を続けます。");
                continue;
            } else {
                System.out.println("登録を終了します。");
                break;
            }
        }
        in.close();
    }

    /**
     * 商品情報の検索
     */
    public void seach() {
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.print("検索する商品名を入力してください: ");
            String searchName = in.nextLine();
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
        in.close();
    }
}
