package sethi.kumar.hemendra.filmkhabar;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sethi.kumar.hemendra.filmkhabar.activities.CreditDetailActivity;
import sethi.kumar.hemendra.filmkhabar.listener.EndlessRecyclerOnScrollListener;
import sethi.kumar.hemendra.filmkhabar.listener.RecyclerItemClickListener;
import sethi.kumar.hemendra.filmkhabar.model.Movies;
import sethi.kumar.hemendra.filmkhabar.model.NowPlayingMovies;
import sethi.kumar.hemendra.filmkhabar.model.PopularMovies;
import sethi.kumar.hemendra.filmkhabar.model.TopRatedMovies;
import sethi.kumar.hemendra.filmkhabar.model.UpcomingMovies;
import sethi.kumar.hemendra.filmkhabar.service.ServiceFactory;
import sethi.kumar.hemendra.filmkhabar.service.TMDBService;
import sethi.kumar.hemendra.filmkhabar.utility.MovieConstants;
import sethi.kumar.hemendra.filmkhabar.utility.MovieUtility;
import sethi.kumar.hemendra.filmkhabar.viewadapter.RecyclerViewAdapter;
import sethi.kumar.hemendra.filmkhabar.viewadapter.TopRatedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView movieRecylerView;
    private RecyclerView topRatedmovieRecylerView;
    private RecyclerView popularRatedmovieRecylerView;

    private static List<Movies> moviesList = new ArrayList<Movies>();
    private static List<Movies> topRatedmoviesList = new ArrayList<Movies>();
    private static List<Movies> populardmoviesList = new ArrayList<Movies>();
    private static List<Movies> nowPlayingMoviesList = new ArrayList<Movies>();

    private static LinearLayoutManager layoutManager;
    private static LinearLayoutManager topRatedlayoutManager;
    private static LinearLayoutManager popularRatedlayoutManager;

    private static int upcomingPageNo = 1;
    private static int topRatedPageNo = 1;
    private static int popularRatedPageNo = 1;

   RecyclerViewAdapter recyclerViewAdapter;
   TopRatedRecyclerViewAdapter topRatedRecyclerViewAdapter;
   TopRatedRecyclerViewAdapter popularRatedRecyclerViewAdapter;
    private PagerIndicator pagerIndicator;
    private SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        movieRecylerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, moviesList);
        movieRecylerView.setLayoutManager(layoutManager);
        movieRecylerView.setAdapter(recyclerViewAdapter);

        topRatedmovieRecylerView = (RecyclerView) findViewById(R.id.top_recycler_view);
        topRatedlayoutManager = new LinearLayoutManager(MainActivity.this);
        topRatedlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topRatedRecyclerViewAdapter = new TopRatedRecyclerViewAdapter(this, topRatedmoviesList);
        topRatedmovieRecylerView.setLayoutManager(topRatedlayoutManager);
        topRatedmovieRecylerView.setAdapter(topRatedRecyclerViewAdapter);

        popularRatedmovieRecylerView = (RecyclerView) findViewById(R.id.popular_recycler_view);
        popularRatedlayoutManager = new LinearLayoutManager(MainActivity.this);
        popularRatedlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        popularRatedRecyclerViewAdapter = new TopRatedRecyclerViewAdapter(this, populardmoviesList);
        popularRatedmovieRecylerView.setLayoutManager(popularRatedlayoutManager);
        popularRatedmovieRecylerView.setAdapter(popularRatedRecyclerViewAdapter);

        topRatedmovieRecylerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(topRatedlayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                getTopRatedMoviesDetails(++topRatedPageNo);
            }
        });



        movieRecylerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                getUpcomingMoviesDetails(++upcomingPageNo);
            }
        });

        popularRatedmovieRecylerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(popularRatedlayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                getPopularMoviesDetails(++popularRatedPageNo);
            }
        });

        getUpcomingMoviesDetails(upcomingPageNo);
        getTopRatedMoviesDetails(topRatedPageNo);
        getPopularMoviesDetails(popularRatedPageNo);

        populateSliderWithNowPlayingMovies();

        pagerIndicator = (PagerIndicator)findViewById(R.id.image_slider);
        sliderLayout = (SliderLayout)findViewById(R.id.slider);
        sliderLayout.setCustomIndicator(pagerIndicator);

    }

    private void populateSliderWithNowPlayingMovies() {

        TMDBService tmdbService = ServiceFactory.createRetrofitService(TMDBService.class, TMDBService.SERVICE_END);
        Observable<NowPlayingMovies> nowPlayingMoviesObservable = tmdbService.getNowPlayingMovies(1);
        nowPlayingMoviesObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NowPlayingMovies>() {
                    Uri uri = null;
                    TextSliderView textSliderView = null;
                    @Override
                    public void onCompleted() {
                        for (Movies movies : nowPlayingMoviesList){
                            uri = Uri.parse(MovieUtility.IMG_URL+movies.getBackdrop_path());
                            textSliderView = new TextSliderView(MainActivity.this);
                            textSliderView.description(movies.getTitle())
                                    .image(uri.toString())
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            sliderLayout.addSlider(textSliderView);
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.setDuration(4000);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NowPlayingMovies nowPlayingMovies) {
                        nowPlayingMoviesList.addAll(nowPlayingMovies.getResults());
                    }
                });

    }

    private void getPopularMoviesDetails(int pageNu) {
        TMDBService tmdbService = ServiceFactory.createRetrofitService(TMDBService.class, TMDBService.SERVICE_END);
        Observable<PopularMovies> popularMoviesObservable = tmdbService.getPopularMovies(pageNu);
        popularMoviesObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<PopularMovies>() {
                    @Override
                    public void onCompleted() {
                        popularRatedRecyclerViewAdapter.notifyItemRangeChanged(0, populardmoviesList.size() -1);
                        popularRatedmovieRecylerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnListItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView movieId = (TextView) view.findViewById(R.id.movieId);
                                int id = Integer.parseInt(movieId.getText().toString());
                                Toast.makeText(MainActivity.this, "Item Clicked Movie Id=" + id, Toast.LENGTH_SHORT).show();
                                Intent creditDetailIntent = new Intent(MainActivity.this, CreditDetailActivity.class);
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_ID, id);
                                creditDetailIntent.putExtra(MovieConstants.IMAGE_URL, populardmoviesList.get(position).getBackdrop_path() != null ? populardmoviesList.get(position).getBackdrop_path().toString() : populardmoviesList.get(position).getPoster_path().toString());
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_NAME, populardmoviesList.get(position).getOriginal_title().toString());
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_OVERVIEW, populardmoviesList.get(position).getOverview().toString());
                                startActivity(creditDetailIntent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        }, popularRatedmovieRecylerView));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PopularMovies popularMovies) {
                        populardmoviesList.addAll(popularMovies.getResults());
                    }
                });

    }

    private void getTopRatedMoviesDetails(int pageNo) {
        TMDBService tmdbService = ServiceFactory.createRetrofitService(TMDBService.class, TMDBService.SERVICE_END);
        Observable<TopRatedMovies> topRatedMoviesObservable = tmdbService.getTopRatedMovies(pageNo);
        topRatedMoviesObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<TopRatedMovies>() {
                    @Override
                    public void onCompleted() {
                        topRatedRecyclerViewAdapter.notifyItemRangeChanged(0, topRatedmoviesList.size() - 1);
                        topRatedmovieRecylerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnListItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView movieId = (TextView) view.findViewById(R.id.movieId);
                                int id = Integer.parseInt(movieId.getText().toString());
                                Toast.makeText(MainActivity.this, "Item Clicked Movie Id=" + id, Toast.LENGTH_SHORT).show();
                                Intent creditDetailIntent = new Intent(MainActivity.this, CreditDetailActivity.class);
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_ID, id);
                                creditDetailIntent.putExtra(MovieConstants.IMAGE_URL, topRatedmoviesList.get(position).getBackdrop_path() != null ? topRatedmoviesList.get(position).getBackdrop_path().toString() : topRatedmoviesList.get(position).getPoster_path().toString());
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_NAME, topRatedmoviesList.get(position).getOriginal_title().toString());
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_OVERVIEW, topRatedmoviesList.get(position).getOverview().toString());
                                startActivity(creditDetailIntent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        }, topRatedmovieRecylerView));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TopRatedMovies topRatedMovies) {
                        topRatedmoviesList.addAll(topRatedMovies.getResults());
                    }
                });
    }

    private void getUpcomingMoviesDetails(int pageNo) {
        TMDBService tmdbService = ServiceFactory.createRetrofitService(TMDBService.class, TMDBService.SERVICE_END);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Downloading...");
        dialog.show();

        Toast.makeText(MainActivity.this, "Requesting Page Number :" + pageNo, Toast.LENGTH_SHORT).show();
        Observable<UpcomingMovies> upcomingMoviesObservable = tmdbService.getUpComingMovies(pageNo);
        upcomingMoviesObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpcomingMovies>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: Upcoming movies operation download completer....." + moviesList.size());
                        dialog.hide();
                        recyclerViewAdapter.notifyItemRangeChanged(0, moviesList.size() - 1);
                        movieRecylerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnListItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                                TextView movieId = (TextView) view.findViewById(R.id.movieId);
                                int id = Integer.parseInt(movieId.getText().toString());
                                Toast.makeText(MainActivity.this, "Item Clicked Movie Id=" + id, Toast.LENGTH_SHORT).show();
                                Intent creditDetailIntent = new Intent(MainActivity.this, CreditDetailActivity.class);
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_ID, id);
                                creditDetailIntent.putExtra(MovieConstants.IMAGE_URL, moviesList.get(position).getBackdrop_path() != null ? moviesList.get(position).getBackdrop_path().toString() : moviesList.get(position).getPoster_path().toString());
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_NAME, moviesList.get(position).getOriginal_title().toString());
                                creditDetailIntent.putExtra(MovieConstants.MOVIE_OVERVIEW, moviesList.get(position).getOverview().toString());
                                startActivity(creditDetailIntent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        }, movieRecylerView));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        dialog.hide();
                        Toast.makeText(MainActivity.this, "Having Trouble fetching data :" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onError: Getting Error while fetching upComing Movies............", throwable);
                    }

                    @Override
                    public void onNext(UpcomingMovies upcomingMovies) {
                        Log.d(TAG, "onNext: Yes Getting upComing Movies list........=>");
                        if (upcomingMovies.getResults() != null) {
                            Log.d(TAG, "onNext: Yes Getting upComing Movies list.......VITARE.=>");
                            moviesList.addAll(upcomingMovies.getResults());
                        }
                    }
                });
    }

}
