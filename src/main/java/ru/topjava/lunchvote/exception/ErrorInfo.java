package ru.topjava.lunchvote.exception;

import java.util.Arrays;

public class ErrorInfo {
    private final String url;
    private final String type;
    private final String[] details;

    public ErrorInfo(CharSequence URL, String type, String... details) {
        this.url = URL.toString();
        this.type = type;
        this.details = details;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", details=" + Arrays.toString(details) +
                '}';
    }
}
