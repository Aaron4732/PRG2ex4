package at.ac.fhcampuswien.fhmdb.observer;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;

public interface Observer {
    void update(WatchlistMovieEntity movie);
}
