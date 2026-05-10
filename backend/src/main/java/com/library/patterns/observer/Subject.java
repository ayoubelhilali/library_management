package com.library.patterns.observer ;

import java.util.ArrayList;
import java.util.List;



public abstract class Subject {

    private List<Observer> observers ;
    protected String notificationMessage ;

    public Subject() {
        this.observers = new ArrayList<>() ;
    }

    // Attach observer
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer) ;
        }
    }

    // Detach observer
    public void detach(Observer observer) {
        observers.remove(observer) ;
    }

    // Notify all observers
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update() ;
        }
    }

    // Set notification message and notify
    public void setNotificationMessage(String message) {
        this.notificationMessage = message ;
        notifyObservers() ;
    }

    public String getNotificationMessage() {
        return notificationMessage ;
    }

    public List<Observer> getObservers() {
        return new ArrayList<>(observers) ;
    }

    public int getObserverCount() {
        return observers.size() ;
    }


}
