package at.ac.fhcampuswien.fhmdb.sort;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class AscendingSortState extends SortState {
    public AscendingSortState(ObservableList<Movie> movies) {
        super(movies);
    }

    @Override
    public void sort() {
        movies.sort(Comparator.comparing(Movie::getTitle));
    }
}
