package com.library.patterns.state;

import com.library.model.enums.BookStatus;

public class BookStateFactory {

    public static BookState getState(BookStatus status) {

        if (status == null) {
            return new AvailableState();
        }

        return switch (status) {
            case AVAILABLE -> new AvailableState();
            case BORROWED -> new BorrowedState();
            case RESERVED -> new ReservedState();
            default -> throw new IllegalArgumentException("Invalid book status");
        };
    }
}