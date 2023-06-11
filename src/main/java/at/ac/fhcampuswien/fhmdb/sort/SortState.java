package at.ac.fhcampuswien.fhmdb.sort;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public abstract class SortState {
    protected ObservableList<Movie> movies;

    public SortState(ObservableList<Movie> movies) {
        this.movies = movies;
    }

    public abstract void sort();
}
