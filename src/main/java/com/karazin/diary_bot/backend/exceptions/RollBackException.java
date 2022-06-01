package com.karazin.diary_bot.backend.exceptions;

public class RollBackException extends RuntimeException {

    public RollBackException() {
        super("Transaction is rolled back");
    }

}
