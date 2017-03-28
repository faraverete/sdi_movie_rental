package ro.ubb.lab.domain;

/**
 * Created by horatiu on 13.03.2017.
 */
public class Movie extends BaseEntity<Long> {

    private String name;
    private String genre;

    public Movie() {}

    public Movie(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (name != null ? !name.equals(movie.name) : movie.name != null) return false;
        return genre != null ? genre.equals(movie.genre) : movie.genre == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }
}
