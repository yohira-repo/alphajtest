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
                    System.out.println("データ項目不足。count=" + count);
                    continue;
                }

                // 商品IDのチェック
                String strProductId = data[0].trim();
                Integer productId = checkInteger(strProductId, 9);
                if (productId == null) {
                    System.out.println("商品IDが不正です。count=" + count);
                    continue;
                }

                // 商品名のチェック
                String strProductName = data[1].trim();
                strProductName = checkString(strProductName, 12);
                if (strProductName == null) {
                    System.out.println("商品名が不正です。count=" + count);
                    continue;
                }
 
                // 商品価格のチェック
                Integer productPrice = checkInteger(data[2].trim(), 9);
                if (productPrice == null) {
                    System.out.println("商品価格が不正です。count=" + count);
                    continue;
                }
                // 商品在庫数のチェック
                Integer productStock = checkInteger(data[3].trim(), 9);
                if (productStock == null) {
                    System.out.println("商品在庫数が不正です。count=" + count);
                    continue;
                }

                // 商品IDの重複チェック
                boolean isDuplicate = false;
                for (ProductBean existingProduct : products) {
                    if (existingProduct.getProductId() == productId.intValue()) {
                        System.out.println("商品IDが重複しています。" + productId);
                        existingProduct.setProductName(strProductName);
                        existingProduct.setProductPrice(productPrice.intValue());
                        existingProduct.setProductStock(productStock.intValue());
                        isDuplicate = true;
                        break;
                    }
                }
                // 重複していない場合は新規追加
                if (!isDuplicate) {
                    ProductBean product = new ProductBean();
                    product.setProductId(productId.intValue());
                    product.setProductName(strProductName);
                    product.setProductPrice(productPrice.intValue());
                    product.setProductStock(productStock.intValue());
                    products.add(product);
                }
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
    
}
