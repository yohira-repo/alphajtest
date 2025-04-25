package com.alphacmc.alphajtest.exception;

/**
 * * つり銭切れの例外クラス
 * つり銭が足りない場合にスローされる例外クラス
 */
public class OutOfChangeException extends Exception {
    private static final long serialVersionUID = 1L;

    public OutOfChangeException(String message) {
        super(message);
    }
    public OutOfChangeException(String message, Throwable cause) {
        super(message, cause);
    }    public OutOfChangeException(Throwable cause) {
        super(cause);
    }
    public OutOfChangeException() {
        super();
    }
    
}
