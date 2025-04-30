package com.alphacmc.alphajtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.alphacmc.alphajtest.bean.ProductBean;
import com.alphacmc.alphajtest.csv.ProductCsvLoader;
import com.alphacmc.alphajtest.exception.OutOfChangeException;

public class Assignment5 {
    // コインの種類(500, 100, 50, 10, 5, 1)
    private static final int[] COIN_ARRAY = { 500, 100, 50, 10, 5, 1 };
    private static final String CSV_FILE_NAME = "C:\\dev\\git\\alphajtest\\data\\課題5_入力データ.csv";
    // 売上コインのリスト
    private List<Integer> salesContList = new ArrayList<>();
    // 商品リスト
    private List<ProductBean> productList = new ArrayList<>();
    // 商品リストのアクセス排他フラグ（true:使用中）
    private boolean isProductListAccess = false;
    // 処理終了フラグ
    private boolean isContinue = true;
    // コンソール入力
    private Scanner scanner = new Scanner(System.in);

    // Timer Object to schedule tasks
    private Timer timer = new Timer();

    // 商品リストの取得
    private ProductCsvLoader productCsvLoader = new ProductCsvLoader();

    // 商品リストの取得タスク
    private TimerTask csvTask = new TimerTask() {
        public void run() {
            // 商品リストフラグ待ち合わせ
            waitProductListAccess();
            isProductListAccess = true;
            try {
                // 商品リストの取得
                boolean isLoad = productCsvLoader.getProducts(CSV_FILE_NAME, productList, salesContList);
                // CSVファイルのバックアップ
                if (isLoad) {
                    productCsvLoader.moveCsvFileToBackup();
                }
            } catch (Exception e) {
                System.out.println("商品リストの取得に失敗しました。");
            }
            System.out.println("商品リストの取得タスクが実行されました。");
            isProductListAccess = false;
        }
    };

    // 商品在庫監視タスク
    private TimerTask stockCheckTask = new TimerTask() {
        public void run() {
            // 商品在庫のチェック
            for (ProductBean product : productList) {
                if (product.getProductStock() <= 10) {
                    System.out.println("商品 " + product.getProductName() + " の在庫が少なくなりました。注文してください。");
                }
            }
            System.out.println("商品在庫監視タスクが実行されました。");
        }
    };

    /**
     * 商品自販機のメイン処理
     * 
     * @throws Exception
     */
    public void venderMachine() throws Exception {
        // 商品リストの構築(csvファイルから読み込む)
        timer.schedule(this.csvTask, 0, 3000000);
        // 商品リストの取得タスクをスケジュール);
        timer.schedule(this.stockCheckTask, 600000, 300000);
        Thread.sleep(10000);
        waitProductListAccess();

        while (isContinue) {
            // 商品選択
            int productPrice = selectProduct();

            // 投入コインの選択
            List<Integer> inputCoinList = getInputMony(productPrice);
            // 投入コインの合計金額を計算
            int inputTotal = 0;
            for (Integer coin : inputCoinList) {
                inputTotal += coin.intValue();
            }
            // 投入コインの合計金額を表示
            System.out.println("投入コインの合計金額: " + inputTotal + "円");
            // お釣りの金額
            int changeTotal = inputTotal - productPrice;
            if (changeTotal == 0) {
                System.out.println("お釣りはありません。");
                // 投入コインを売上コインリストに追加
                for (Integer coin : inputCoinList) {
                    salesContList.add(coin);
                }
                continue;
            }
            // お釣りリスト
            List<Integer> changeCoinList = new ArrayList<Integer>();
            try {
                // お釣りの計算
                changeCoinList = getCalculateChange(changeTotal);
                // 投入コインを売上コインリストに追加
                for (Integer coin : inputCoinList) {
                    salesContList.add(coin);
                }
            } catch (OutOfChangeException e) {
                System.out.println(e.getMessage());
                // 投入コインをそのままつり銭リストに追加
                changeCoinList.clear();
                for (Integer coin : inputCoinList) {
                    changeCoinList.add(coin);
                }
            }
            // つり銭リストの表示
            System.out.println("つり銭リストを表示します。");
            for (Integer coin : changeCoinList) {
                System.out.println(coin + "円");
            }
        }
        // 入力終了
        scanner.close();
        // タイマータスクの停止
        timer.cancel();
        System.out.println("タイマータスクを停止しました。");
    }

    /**
     * 商品選択
     * 
     * @return 選択商品価格
     * 
     */
    private int selectProduct() {
        ProductBean selectProductBean = null;
        // 商品が選択されない限り繰り返す
        while (selectProductBean == null) {
            System.out.println("商品リストを表示します。");
            // 商品コード順にソート
            productList.sort((p1, p2) -> Integer.compare(p1.getProductId(), p2.getProductId()));
            // 商品リストの表示
            for (ProductBean product : productList) {
                System.out.println(product.getProductId() + ": " + product.getProductName() + " - "
                        + product.getProductPrice() + "円");
            }
            // 商品IDの選択
            System.out.print("商品IDを選択してください。");
            String strProductId = scanner.nextLine();
            // 商品IDのチェック
            Integer productId = checkInteger(strProductId, 9);
            if (productId == null) {
                System.out.println("商品IDが不正です。");
                continue;
            }
            // 商品IDの存在チェック
            for (ProductBean product : productList) {
                if (product.getProductId() == productId.intValue()) {
                    // 商品在庫のチェック
                    if (product.getProductStock() > 0) {
                        // 商品在庫を減らす
                        product.setProductStock(product.getProductStock() - 1);
                        selectProductBean = product;
                    } else {
                        System.out.println("在庫がありません。");
                    }
                    break;
                }
            }
        }
        // 商品選択完了
        System.out.println(
                "商品選択完了: " + selectProductBean.getProductName() + " - " + selectProductBean.getProductPrice() + "円");
        return selectProductBean.getProductPrice();
    }

    /**
     * 投入コイン入力
     * 
     * @return 投入コインのリスト
     */
    private List<Integer> getInputMony(int getProductPrice) {
        // 投入コインのリスト
        List<Integer> inputCoinList = new ArrayList<>();
        // 投入コインの合計金額
        int inputTotal = 0;
        // 投入コインの合計金額が商品価格以上になるまでループ
        while (inputTotal < getProductPrice) {
            // 投入コインの選択
            System.out.print("投入コインを選択してください。");
            String strInputCoin = scanner.nextLine();
            // 投入コインのチェック
            Integer inputCoin = checkInteger(strInputCoin, 3);
            if (inputCoin == null) {
                System.out.println("投入コインが不正です。");
                continue;
            }
            // 投入コインの存在チェック
            boolean isExist = false;
            for (int i = 0; i < COIN_ARRAY.length; i++) {
                if (inputCoin.intValue() == COIN_ARRAY[i]) {
                    isExist = true;
                    continue;
                }
            }
            if (!isExist) {
                System.out.println("投入コインが不正です。");
                continue;
            }
            // 投入コインのリストに追加
            inputCoinList.add(inputCoin);
            inputTotal += inputCoin.intValue();
        }
        // 投入コインリストを返却
        return inputCoinList;
    }

    /**
     * お釣りの計算
     * 
     * @param changeTotal お釣りの合計金額
     * @return お釣りコインのリスト
     */
    private List<Integer> getCalculateChange(int changeTotal) throws OutOfChangeException {
        System.out.println("お釣りの計算を開始します。 お釣り金額: " + changeTotal + "円");
        // お釣りコインのリスト
        List<Integer> changeCoinList = new ArrayList<>();
        // お釣りの計算
        int remChange = changeTotal;
        for (int i = 0; i < COIN_ARRAY.length; i++) {
            // 必要コイン枚数
            int numberOfCoin = remChange / COIN_ARRAY[i];
            System.out.println(COIN_ARRAY[i] + "円コインの必要枚数: " + numberOfCoin);
            // まだ残るお釣り金額
            // remChange = remChange % COIN_ARRAY[i]; <= つり銭切れを考慮しないケース
            // 残り枚数がない場合は次のコインへ
            if (numberOfCoin == 0) {
                continue;
            }
            // 売上配列から必要コイン枚数分あるかチェック
            for (int j = 0; j < salesContList.size(); j++) {
                int sales = salesContList.get(j);

                // 該当金額をつり銭コインリストに追加
                if (sales == COIN_ARRAY[i]) {
                    // 売上金リストに仮払いマークを付与
                    salesContList.set(j, sales * -1);
                    // お釣りコインリストに追加
                    changeCoinList.add(sales);
                    // 残り枚数を減らす
                    numberOfCoin--;
                    // お釣りの残金
                    remChange -= sales;
                }
                // 残り枚数がない場合は次のコインへ
                if (numberOfCoin == 0) {
                    break;
                }
            }
            // お釣りの残りがない場合終了
            if (remChange == 0) {
                break;
            }
        }
        // お釣りが残っている場合はつり銭切れ
        if (remChange > 0) {
            // 売上金リストに仮払いマークをロールバック
            for (int i = 0; i < salesContList.size(); i++) {
                int sales = salesContList.get(i);
                if (sales < 0) {
                    salesContList.set(i, sales * -1);
                }
            }
            // お釣りが足りない場合は例外をスロー
            throw new OutOfChangeException("お釣りが足りません。");
        }
        // 売上金リストに仮払いマークをコミット
        for (int i = 0; i < salesContList.size(); i++) {
            int sales = salesContList.get(i);
            if (sales < 0) {
                salesContList.set(i, 0);
            }
        }

        return changeCoinList;
    }

    // 商品リストアクセスフラグ待ち合わせ
    private void waitProductListAccess() {
        while (isProductListAccess) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 整数桁数のチェック
     * 
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

    public static void main(String[] args) throws Exception {
        Assignment5 assignment = new Assignment5();
        assignment.venderMachine();
    }

}
