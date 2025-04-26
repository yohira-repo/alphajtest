package com.alphacmc.alphajtest.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.alphacmc.alphajtest.bean.ProductBean;

public class ProductCsvLoader {
    // コインの種類
    private static final int[] COIN_ARRAY = { 500, 100, 50, 10, 5, 1 };

    private  List<Integer> initialSalesCoin;

    private List<ProductBean> products;

    /**
     * 商品情報の取得
     * @param filePathName
     * @return boolean <true>:成功、<false>:ファイルが存在しない
     * @throws Exception
     */
    public boolean getProducts(String filePathName, List<ProductBean> products, List<Integer> SalesCoin) throws Exception {

        this.products = products;
        this.initialSalesCoin = SalesCoin;
   
        // テキストファイルの読み込みサンプル
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {
            //読み込み行
            String line;
            //読み込み行カウンタ
            int count = 0;

            //1行ずつ読み込みを行う(読込エリア line が NULL であれば、EOF 状態)
            while ((line = br.readLine()) != null) {
                //カウンタをインクリメント
                count++;
                //カンマで分割した内容を配列に格納する（マルチフォーマット）
                String[] data = line.split(",");
                if (data.length < 2) {
                    System.out.println("データ項目不足。count=" + count);
                    continue;
                }
                // レコード区分
                String recordId = data[0].trim();
                // データ項目のセパレート
                String item[] = data[1].split("/");
                // レコード区分のチェック
                if ("1".equals(recordId)) {
                    if (item.length < 4) {
                        System.out.println("商品情報が不足しています。count=" + count);
                        continue;
                    }
                    // 商品情報の取得
                    ProductBean product = checkProductData(item);
                    // 商品情報の取得が不正の場合は、商品情報をセットしない
                    if (product == null) {
                        System.out.println("count=" + count);
                        continue;
                    }
                    // 商品情報のが重複なしの場合は、商品情報をセット（重複ありの場合は、商品IDは0のまま）
                    if (product.getProductId() != 0) {
                        products.add(product);
                    }
                } else if ("2".equals(recordId)) {
                    if (item.length < 2) {
                        System.out.println("初期売上金情報が不足しています。count=" + count);
                        continue;
                    }
                    // 初期売上金情報の取得
                    List<Integer> initSalesCoin = getIntitalSales(item);
                    if (initSalesCoin == null) {
                        System.out.println(" count=" + count);
                        continue;
                    }
                    // 初期売上金情報の追加
                    initialSalesCoin.addAll(initSalesCoin);
                } else {
                    System.out.println("レコード区分が不正です。count=" + count);
                    continue;
                }
            }
            System.out.println("CSVファイルレコードカウント:" + count);
            return true;
        // 例外処理（ファイルが見つからない？など）
        } catch (FileNotFoundException e) {
            System.out.println("ファイルが存在しません:" + filePathName);
            return false;
        }
    }

    /**
     * 初期売上金情報の取得
     * @param item[]
     * @return
     */
    private List<Integer> getIntitalSales(String[] item) {

        // 初期売上金情報
        List<Integer> initSalesCoin = new ArrayList<>();

        // コインの種類のチェック
        String strCoinUnit = item[0].trim();
        Integer coinUnit = checkInteger(strCoinUnit, 3);
        if (coinUnit == null) {
            System.out.print("コインの形式が不正です。");
            return null;
        }
        boolean isCoinUnit = false;
        for (int coin : COIN_ARRAY) {
            if (coinUnit == coin) {
                break;
            }
        }
        if (!isCoinUnit) {
            System.out.println("無効なコイン単位です。");
        }

        // コインの枚数のチェック
        String strCoinCount = item[1].trim();
        Integer coinCount = checkInteger(strCoinCount, 9);
        if (coinCount == null) {
            System.out.print("コインの枚数が不正です。");
            return null;
        }
        // 初期売上金の追加
        for (int i = 0; i < coinCount.intValue(); i++) {
            initSalesCoin.add(coinUnit);
        }
        return initSalesCoin;
    }

    /**
     * 商品情報の取得
     * @param item[]
     * @return
     */
    private ProductBean checkProductData(String[] item) {

        ProductBean product = new ProductBean();

        // 商品IDのチェック
        Integer productId = checkInteger(item[0].trim(), 9);
        if (productId == null) {
            System.out.print("商品IDが不正です。");
            return null;
        }
        // 商品IDの重複チェック
        int findIndex = -1;
        for (int i = 0; i < this.products.size(); i++) {
            ProductBean existingProduct = this.products.get(i);
            if (existingProduct.getProductId() == productId.intValue()) {
                System.out.println("商品IDが重複しています。" + productId);
                findIndex = i;
                break;
            }
        }
        // 商品IDの重複なしの場合は、商品IDをセット
        if (findIndex == -1) {
            product.setProductId(productId.intValue());
        }
        // 商品名のチェック
        String productName = item[1].trim();
        // 重複なしの商品名のチェック、重複ありで商品名が入っている場合は、商品名チェック更新
        if (findIndex == -1 || productName.length() != 0) {
            productName = checkString(productName, 12);
            if (productName.length() == 0) {
                System.out.print("商品名が不正です。");
                return null;
            }
            // 重複ありで商品名が入っている場合は、商品名更新
            if (findIndex != -1) {
                this.products.get(findIndex).setProductName(productName);
            } else{
                product.setProductName(productName);
            }
        }

        // 商品価格のチェック
        String strProductPrice = item[2].trim();
        if (findIndex == -1 || strProductPrice.length() != 0) {
            Integer productPrice = checkInteger(strProductPrice, 9);
            if (productPrice == null) {
                System.out.print("商品価格が不正です。");
                return null;
            }
            // 重複ありで商品価格が入っている場合は、商品価格更新
            if (findIndex != -1) {
                this.products.get(findIndex).setProductPrice(productPrice.intValue());
            } else{
                product.setProductPrice(productPrice.intValue());
            }
        }

        // 商品在庫数のチェック
        String strProductStock = item[3].trim();
        if (findIndex == -1 || strProductStock.length() != 0) {
            Integer productStock = checkInteger(strProductStock, 9);
            if (productStock == null) {
                System.out.print("商品在庫数が不正です。");
                return null;
            }
            // 重複ありで商品在庫数が入っている場合は、商品在庫数更新
            if (findIndex != -1) {
                this.products.get(findIndex).setProductStock(productStock.intValue());
            } else{
                product.setProductStock(productStock.intValue());
            }
        }
        // 重複の場合、返却値の商品IDは、初期値0のまま
        return product;
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

    public List<ProductBean> getProducts() {
        return products;
    }

    public List<Integer> getInitialSalesCoin() {
        return initialSalesCoin;
    }

}
