package at.ac.fhcampuswien.fhmdb.observer;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;

import java.util.function.Consumer;

public interface Observable {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(WatchlistMovieEntity movie);
}
