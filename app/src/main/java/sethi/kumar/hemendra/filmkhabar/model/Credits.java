package sethi.kumar.hemendra.filmkhabar.model;

import java.util.List;

/**
 * Created by BAPI1 on 10-02-2016.
 */
public class Credits {
    private String id;
    private List<Cast> cast;
    private List<Crew> crew;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }
}
