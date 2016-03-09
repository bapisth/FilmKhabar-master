package sethi.kumar.hemendra.filmkhabar.model;

import java.util.List;

/**
 * Created by hemendra on 09-03-2016.
 */
public class NowPlayingMovies {
    private Dates dates;
    private String page;
    private List<Movies> results;
    private  String total_pages;
    private  String total_results;

    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Movies> getResults() {
        return results;
    }

    public void setResults(List<Movies> results) {
        this.results = results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public String getTotal_results() {
        return total_results;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }
}
