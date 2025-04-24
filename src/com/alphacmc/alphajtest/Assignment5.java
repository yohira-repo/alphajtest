package com.alphacmc.alphajtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

import com.alphacmc.alphajtest.bean.ProductBean;

public class Assignment5 {
    // コインの種類
    private static final int[] COIN_ARRAY = {500, 100, 50, 10, 5, 1};
    // 投入コインのリスト    
    private List<Integer> inputCoinList = new ArrayList<>();
    // 売上コインのリスト
    private List<Integer> salesContList = new ArrayList<>();
    // お釣りコインのリスト
    private List<Integer> changeCoinList = new ArrayList<>();
    // 商品リスト
    private List<ProductBean> productList = new ArrayList<>();
    // 商品リストのアクセス排他フラグ
    private boolean isProductListAccess = false;
    // 処理終了フラグ
    private boolean isEnd = false;
    // コンソール入力
    private Scanner scanner = new Scanner(System.in);

    // Timer Object to schedule tasks
    private Timer timer = new Timer();

    // 商品リストの取得タスク
    private TimerTask csvTask = new TimerTask() {
        public void run() {
            // 商品リストフラグ待ち合わせ
            waitProductListAccess ();
            isProductListAccess = true;
            System.out.println("タスクが実行されました。");
            isProductListAccess = false;
        }
    };

    // 商品在庫監視タスク
    private TimerTask stockCheckTask = new TimerTask() {
        public void run() {
            System.out.println("タスクが実行されました。");
       }
    };

    // Method to print a message
    public void venderMachine() {
        // TODO:商品リストの構築(csvファイルから読み込む)
        timer.schedule(this.csvTask, 3000);
        timer.schedule(this.stockCheckTask, 3000);

        while (isEnd) {
            // 商品選択
            ProductBean productBean = selectProduct();
            // 商品選択完了
            System.out.println("商品選択完了: " + productBean.getProductName() + " - " + productBean.getProductPrice() + "円");

            // 投入コインの選択
            int inputTotal = getInputMony(productBean.getProductPrice());
            // 投入コインの合計金額を表示
            System.out.println("投入コインの合計金額: " + inputTotal + "円");
            
            // お釣りの計算
            int changeTotal =  productBean.getProductPrice() - inputTotal;
            if (changeTotal == 0) {
                System.out.println("お釣りはありません。");
                changeCoinList.clear();
                // 投入コインを売上コインリストに追加
                for (Integer coin : inputCoinList) {
                    salesContList.add(coin);
                }
                continue;
            }
            // お釣りの計算
            if (getCalculateChange(changeTotal)) {
                // 売上金リスト・コミット
                for (int i = 0; i < salesContList.size(); i++) {
                    int sales = salesContList.get(i);
                    if (sales < 0) {
                        salesContList.set(i, 0);
                    }
                }
                // 投入コインを売上コインリストに追加
                for (Integer coin : inputCoinList) {
                    salesContList.add(coin);
                }
            } else {
                changeCoinList.clear();
                // 投入コインをそのままつり銭リストに追加
                for (Integer coin : inputCoinList) {
                    changeCoinList.add(coin);
                }
                // 売上金リスト・ロールバック
                for(int i = 0; i < salesContList.size(); i++ ) {
                    int sales = salesContList.get(i);
                    if (sales < 0) {
                        salesContList.set(i, sales * -1);
                    }
                }
            }
            // つり銭リストの表示
            System.out.println("つり銭リストを表示します。");
            for (int i = 0; i < changeCoinList.size(); i++) {
                System.out.println(changeCoinList.get(i) + "円");
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
     * @return
     */
    private ProductBean selectProduct() {
        ProductBean selectProductBean = null;
        while(selectProductBean == null) {
            // 商品リストの表示
            waitProductListAccess ();
            System.out.println("商品リストを表示します。");
            for (ProductBean product : productList) {
                System.out.println(product.getProductId() + ": " + product.getProductName() + " - " + product.getProductPrice() + "円");
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

        return selectProductBean;
    }

    /**
     * 投入コイン入力
     * @return 投入コインの合計金額
     */
    private int getInputMony(int getProductPrice) {
        int inputTotal = 0;
        // 投入コインのリストの初期化
        inputCoinList.clear();
        while(inputTotal < getProductPrice) {
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
                    break;
                }
            }
            if (!isExist) {
                System.out.println("投入コインが不正です。");
                continue;
            }
            // 投入コインのリストに追加
            inputCoinList.add(inputCoin);
            // 投入コインの合計金額を計算
            for (Integer coin : inputCoinList) {
                inputTotal += coin.intValue();
            }
        }
        return inputTotal;
    }

    private boolean getCalculateChange(int changeTotal) {
        // TODO:お釣りの計算

        // TODO:お釣り銭切れの処判定

        return false;
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

    public static void main(String[] args) {
        Assignment5 assignment = new Assignment5();
        assignment.venderMachine();
    }

}

