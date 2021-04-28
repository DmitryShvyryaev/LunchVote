package ru.topjava.lunchvote.exception;

public class RepeatVoteException extends RuntimeException {
    public RepeatVoteException(String message) {
        super(message);
    }
}
