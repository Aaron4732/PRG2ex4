package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import okhttp3.*;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MovieAPI {
    public static final String DELIMITER = "&";
    private static final String URL = "http://prog2.fh-campuswien.ac.at/movies"; // https if certificates work
    private static final OkHttpClient client = new OkHttpClient();

    private String base;
    private String query;
    private Genre genre;
    private String releaseYear;
    private String ratingFrom;

    public MovieAPI(MovieAPIBuilder builder) {
        this.base = builder.base;
        this.query = builder.query;
        this.genre = builder.genre;
        this.releaseYear = builder.releaseYear;
        this.ratingFrom = builder.ratingFrom;
    }

    private String buildUrl(UUID id) {
        StringBuilder url = new StringBuilder(URL);
        if (id != null) {
            url.append("/").append(id);
        }
        return url.toString();
    }

    private static String buildUrl(String query, Genre genre, String releaseYear, String ratingFrom) {
        StringBuilder url = new StringBuilder(URL);
        if ( (query != null && !query.isEmpty()) ||
                genre != null || releaseYear != null || ratingFrom != null) {

            url.append("?");

            // check all parameters and add them to the url
            if (query != null && !query.isEmpty()) {
                url.append("query=").append(query).append(DELIMITER);
            }
            if (genre != null) {
                url.append("genre=").append(genre).append(DELIMITER);
            }
            if (releaseYear != null) {
                url.append("releaseYear=").append(releaseYear).append(DELIMITER);
            }
            if (ratingFrom != null) {
                url.append("ratingFrom=").append(ratingFrom).append(DELIMITER);
            }
        }

        return url.toString();
    }

    public static List<Movie> getAllMovies() throws MovieApiException {
        return getAllMovies(null, null, null, null);
    }

    public static List<Movie> getAllMovies(String query, Genre genre, String releaseYear, String ratingFrom) throws MovieApiException{
        //String url = buildUrl(query, genre, releaseYear, ratingFrom);
        String url = new MovieAPIBuilder(URL)
                .query(query)
                .genre(genre)
                .releaseYear(releaseYear)
                .ratingFrom(ratingFrom)
                .buildUrl();

        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")  // needed for the server to accept the request
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(responseBody, Movie[].class);

            return Arrays.asList(movies);
        } catch (Exception e) {
            throw new MovieApiException(e.getMessage());
        }
    }

    public Movie requestMovieById(UUID id) throws MovieApiException {
        String url = buildUrl(id);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Gson gson = new Gson();
            return gson.fromJson(Objects.requireNonNull(response.body()).string(), Movie.class);
        } catch (Exception e) {
            throw new MovieApiException(e.getMessage());
        }
    }
    public static class MovieAPIBuilder {

        public static final String DELIMITER = "&";
        private final String base;
        private String query;
        private Genre genre;
        private String releaseYear;
        private String ratingFrom;

        public MovieAPIBuilder(String base) {
            this.base = base;
        }

        public MovieAPIBuilder query(String query) {
            this.query = query;
            return this;
        }

        public MovieAPIBuilder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public MovieAPIBuilder releaseYear(String releaseYear) {
            this.releaseYear = releaseYear;
            return this;
        }

        public MovieAPIBuilder ratingFrom(String ratingFrom) {
            this.ratingFrom = ratingFrom;
            return this;
        }

        private String buildUrl() {
            StringBuilder url = new StringBuilder(this.base);

            if ((query != null && !query.isEmpty()) ||
                    genre != null || releaseYear != null || ratingFrom != null) {

                url.append("?");

                // check all parameters and add them to the url
                if (query != null && !query.isEmpty()) {
                    url.append("query=").append(query).append(DELIMITER);
                }
                if (genre != null) {
                    url.append("genre=").append(genre).append(DELIMITER);
                }
                if (releaseYear != null) {
                    url.append("releaseYear=").append(releaseYear).append(DELIMITER);
                }
                if (ratingFrom != null) {
                    url.append("ratingFrom=").append(ratingFrom).append(DELIMITER);
                }
            }

            return url.toString();
        }
    }
}
