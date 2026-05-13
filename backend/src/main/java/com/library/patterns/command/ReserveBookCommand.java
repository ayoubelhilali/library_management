package com.library.patterns.command;

import com.library.model.Reservation;
import com.library.patterns.facade.LibraryFacade;

public class ReserveBookCommand implements Command {

    private final LibraryFacade libraryFacade;
    private final Reservation reservation;

    public ReserveBookCommand(
            LibraryFacade libraryFacade,
            Reservation reservation
    ) {
        this.libraryFacade = libraryFacade;
        this.reservation = reservation;
    }

    @Override
    public boolean execute() {
        return libraryFacade.reserveBook(reservation);
    }
}