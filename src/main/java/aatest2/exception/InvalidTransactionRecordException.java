package aatest2.exception;

import java.io.IOException;

public class InvalidTransactionRecordException extends IOException {
    public InvalidTransactionRecordException(String message) {
        super(message);
    }
}