package com.library.patterns.strategy;

import com.library.model.Borrow;

/**
 * Classe PenaltyContext - Contexte qui utilise la stratégie de pénalité
 * Pattern Strategy: permet de changer l'algorithme de pénalité à l'exécution
 */
public class PenaltyContext {

    private PenaltyStrategy penaltyStrategy;

    public PenaltyContext() {
        // Stratégie par défaut
        this.penaltyStrategy = new DefaultPenaltyStrategy();
    }

    public PenaltyContext(PenaltyStrategy penaltyStrategy) {
        if (penaltyStrategy == null) {
            throw new IllegalArgumentException("Penalty strategy cannot be null");
        }
        this.penaltyStrategy = penaltyStrategy;
    }

    /**
     * Calculer la pénalité pour un emprunt
     * @param borrow L'emprunt pour lequel calculer la pénalité
     * @return Le montant de la pénalité
     */
    public double calculatePenalty(Borrow borrow) {
        return penaltyStrategy.calculatePenalty(borrow);
    }

    /**
     * Changer la stratégie de pénalité
     * @param penaltyStrategy La nouvelle stratégie
     */
    public void setPenaltyStrategy(PenaltyStrategy penaltyStrategy) {
        if (penaltyStrategy == null) {
            throw new IllegalArgumentException("Penalty strategy cannot be null");
        }
        this.penaltyStrategy = penaltyStrategy;
    }

    /**
     * Obtenir la stratégie actuelle
     * @return La stratégie de pénalité actuelle
     */
    public PenaltyStrategy getPenaltyStrategy() {
        return penaltyStrategy;
    }

    /**
     * Obtenir la description de la stratégie actuelle
     * @return Description de la stratégie
     */
    public String getStrategyDescription() {
        return penaltyStrategy.getStrategyDescription();
    }

    /**
     * Obtenir le taux de pénalité par jour
     * @return Le taux de la stratégie actuelle
     */
    public double getPenaltyRatePerDay() {
        return penaltyStrategy.getPenaltyRatePerDay();
    }

    /**
     * Afficher les détails de la stratégie
     */
    public void printStrategyDetails() {
        System.out.println("Current Penalty Strategy: " + penaltyStrategy.getStrategyDescription());
        System.out.println("Rate per day: " + penaltyStrategy.getPenaltyRatePerDay());
    }
}
