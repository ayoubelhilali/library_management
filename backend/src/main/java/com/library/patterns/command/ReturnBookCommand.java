package com.library.patterns.command;

import com.library.patterns.facade.LibraryFacade;

public class ReturnBookCommand implements Command {

    private final LibraryFacade  libraryFacade;
    private final int borrowId;

    public ReturnBookCommand(LibraryFacade borrowService, int borrowId) {
        this.libraryFacade = borrowService;
        this.borrowId = borrowId;
    }

    @Override
    public boolean execute() {
        return libraryFacade.returnBook(borrowId);
    }
}