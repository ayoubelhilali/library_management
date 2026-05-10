package com.library.patterns.strategy;

import com.library.model.Borrow;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe DefaultPenaltyStrategy - Stratégie de pénalité par défaut pour les autres membres
 * Pénalité standard: 1.00 par jour de retard (max 10.00)
 */
public class DefaultPenaltyStrategy implements PenaltyStrategy {

    private static final double PENALTY_RATE_PER_DAY = 1.00;
    private static final double MAX_PENALTY = 10.00;

    @Override
    public double calculatePenalty(Borrow borrow) {

        if (borrow == null) {
            throw new IllegalArgumentException("Borrow cannot be null");
        }

        // Si le livre n'a pas encore été retourné
        if (borrow.getActualReturnDate() == null) {
            LocalDate today = LocalDate.now();
            long daysLate = ChronoUnit.DAYS.between(borrow.getExpectedReturnDate(), today);

            if (daysLate <= 0) {
                return 0.0; // Pas de retard
            }

            double penalty = daysLate * PENALTY_RATE_PER_DAY;
            return Math.min(penalty, MAX_PENALTY); // Limiter la pénalité au maximum
        }

        // Si le livre a déjà été retourné
        long daysLate = ChronoUnit.DAYS.between(borrow.getExpectedReturnDate(), borrow.getActualReturnDate());

        if (daysLate <= 0) {
            return 0.0; // Pas de retard
        }

        double penalty = daysLate * PENALTY_RATE_PER_DAY;
        return Math.min(penalty, MAX_PENALTY); // Limiter la pénalité au maximum
    }

    @Override
    public String getStrategyDescription() {
        return "Default Penalty Strategy: 1.00 per day (max 10.00)";
    }

    @Override
    public double getPenaltyRatePerDay() {
        return PENALTY_RATE_PER_DAY;
    }

    public double getMaxPenalty() {
        return MAX_PENALTY;
    }
}
