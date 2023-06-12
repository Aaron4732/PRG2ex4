package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WatchlistRepository implements Observable {


    Dao<WatchlistMovieEntity, Long> dao;
    private List<Observer> observers;

    public WatchlistRepository() throws DataBaseException {
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
        this.observers = new ArrayList<>();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(WatchlistMovieEntity movie) {
        for (Observer observer : observers) {
            observer.update(movie);
        }
    }

    public List<WatchlistMovieEntity> readWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while reading watchlist");
        }
    }
    public void addToWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            // only add movie if it does not exist yet
            long count = dao.queryBuilder().where().eq("apiId", movie.getApiId()).countOf();
            if (count > 0 ) {
                UserDialog dialog = new UserDialog("Movie Already Added", "The movie \"" + movie.getTitle() + "\" is already in the watchlist.");
                dialog.show();
                System.out.println("Movie is already in Watchlist");
            } else if (count == 0) {
                dao.create(movie);
                notifyObservers(movie);
                UserDialog dialog = new UserDialog("Movie Successfully Added", "The movie \"" + movie.getTitle() + "\" has successfully been added to the watchlist.");
                dialog.show();
                System.out.println("Movie was added to Watchlist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while adding to watchlist");
        }
    }

    public void removeFromWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            dao.delete(movie);
        } catch (Exception e) {
            throw new DataBaseException("Error while removing from watchlist");
        }
    }

    public boolean isOnWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            return dao.queryForMatching(movie).size() > 0;
        } catch (Exception e) {
            throw new DataBaseException("Error while checking if movie is on watchlist");
        }
    }

}
