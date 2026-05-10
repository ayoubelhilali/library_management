package com.library.patterns.strategy;

import com.library.model.Borrow;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe TeacherPenaltyStrategy - Stratégie de pénalité pour les enseignants
 * Les enseignants ont une pénalité très réduite: 0.25 par jour de retard (max 2.00)
 */
public class TeacherPenaltyStrategy implements PenaltyStrategy {

    private static final double PENALTY_RATE_PER_DAY = 0.25;
    private static final double MAX_PENALTY = 2.00;

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
        return "Teacher Penalty Strategy: 0.25 per day (max 2.00)";
    }

    @Override
    public double getPenaltyRatePerDay() {
        return PENALTY_RATE_PER_DAY;
    }

    public double getMaxPenalty() {
        return MAX_PENALTY;
    }
}
