package com.library.patterns.strategy;

import com.library.model.Borrow;

/**
 * Interface PenaltyStrategy - Définit le contrat pour les différentes stratégies de calcul de pénalités
 * Pattern Strategy permet de changer l'algorithme de calcul de pénalité à l'exécution
 */
public interface PenaltyStrategy {

    /**
     * Calculer la pénalité basée sur l'emprunt
     * @param borrow L'emprunt avec les dates de retour
     * @return Le montant de la pénalité
     */
    double calculatePenalty(Borrow borrow);

    /**
     * Obtenir la description de la stratégie
     * @return Description de la stratégie
     */
    String getStrategyDescription();

    /**
     * Obtenir le taux de pénalité par jour (en unité monétaire)
     * @return Le taux de pénalité
     */
    double getPenaltyRatePerDay();
}
