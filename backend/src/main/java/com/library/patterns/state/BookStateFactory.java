package com.library.patterns.state;

public class BookStateFactory {

    public static BookState getState(String status) {

        if (status == null) {
            return new AvailableState();
        }

        return switch (status.toUpperCase()) {
            case "AVAILABLE" -> new AvailableState();
            case "BORROWED" -> new BorrowedState();
            case "RESERVED" -> new ReservedState();
            default -> throw new IllegalArgumentException("Invalid book status");
        };
    }
}