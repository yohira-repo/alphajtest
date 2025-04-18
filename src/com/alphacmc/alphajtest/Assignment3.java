package com.alphacmc.alphajtest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.alphacmc.alphajtest.bean.ProductBean;

public class Assignment3 {

    private static final String FILE_NAME = "商品マスタ.csv";
    public static void main(String[] args) {
        Assignment3 assignment3 = new Assignment3();
        assignment3.getProducts();
    }

    public void getProducts() {
        List<ProductBean> products = new ArrayList<>();
        // テキストファイルの読み込みサンプル
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME)))) {
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
                if (data.length < 4) {
                    System.out.println("データ項目不足。");
                    continue;
                }

                // 商品IDのチェック
                String strProductId = data[0].trim();
                Integer productId = checkInteger(strProductId);
                if (productId == null) {
                    System.out.println("商品IDが不正です。");
                    continue;
                }
                //TODO: 商品IDの重複チェック
                // 商品名のチェック
                String strProductName = data[1].trim();
                if (strProductName == null || strProductName.isEmpty()) {
                    System.out.println("商品名が不正です。");
                    continue;
                }
                // TODO:商品名の長さチェック
                // TODO:商品価格の桁数チェック
                // 商品価格のチェック
                Integer productPrice = checkInteger(data[2].trim());
                if (productPrice == null) {
                    System.out.println("商品価格が不正です。");
                    continue;
                }
                // TODO:商品在庫数の桁数チェック
                // 商品在庫数のチェック
                Integer productStock = checkInteger(data[3].trim());
                if (productStock == null) {
                    System.out.println("商品在庫数が不正です。");
                    continue;
                }
                ProductBean product = new ProductBean();
                product.setProductId(productId.intValue());
                product.setProductName(strProductName);
                product.setProductPrice(productPrice.intValue());
                product.setProductStock(productStock.intValue());
                products.add(product);
            }
            System.out.println("CSVファイルレコードカウント:" + count);
        // 例外処理（ファイルが見つからない？など）
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 商品リストのソート
        products.sort((p1, p2) -> Integer.compare(p1.getProductId(), p2.getProductId()));

        for (ProductBean product : products) {
            System.out.println("商品ID:" + product.getProductId() + " 商品名:" + product.getProductName() + " 商品価格:"
                    + product.getProductPrice() + " 商品在庫数:" + product.getProductStock());
        }

ß    }

    private Integer checkInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 文字列のチェック
     * @param str
     * @return
     */
    private String checkString(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return str;
    }

}
