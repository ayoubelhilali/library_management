package com.library.patterns.command;

import com.library.patterns.facade.LibraryFacade;
import com.library.service.BorrowService;

public class BorrowBookCommand implements Command {

    private final LibraryFacade libraryFacade;
    private final int bookId;
    private final int memberId;

    public BorrowBookCommand(LibraryFacade borrowService, int bookId, int memberId) {
        this.libraryFacade = borrowService;
        this.bookId = bookId;
        this.memberId = memberId;
    }

    @Override
    public boolean execute() {
        return libraryFacade.borrowBook(bookId, memberId);
    }
}