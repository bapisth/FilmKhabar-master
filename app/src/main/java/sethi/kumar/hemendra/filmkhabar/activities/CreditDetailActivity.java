package sethi.kumar.hemendra.filmkhabar.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sethi.kumar.hemendra.filmkhabar.R;
import sethi.kumar.hemendra.filmkhabar.model.Cast;
import sethi.kumar.hemendra.filmkhabar.model.Credits;
import sethi.kumar.hemendra.filmkhabar.model.Crew;
import sethi.kumar.hemendra.filmkhabar.model.Movies;
import sethi.kumar.hemendra.filmkhabar.service.ServiceFactory;
import sethi.kumar.hemendra.filmkhabar.service.TMDBService;
import sethi.kumar.hemendra.filmkhabar.utility.MovieConstants;
import sethi.kumar.hemendra.filmkhabar.utility.MovieUtility;
import sethi.kumar.hemendra.filmkhabar.viewadapter.CastRecyclerView;

public class CreditDetailActivity extends AppCompatActivity {
    String movieUrl = "";
    int movieId = 0;
    String movieName = "";
    String overview = "";
    private ImageView large_poster;
    private static final String TAG = CreditDetailActivity.class.getSimpleName();
    private TMDBService tmdbService = null;
    private static List<Cast> castList;
    private static List<Crew> crewList;
    private static TextView movie_overview;
    private RecyclerView cast_list_recycler_view;
    private LinearLayoutManager linearLayoutManager = null;
    private CastRecyclerView castRecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getParameters();

        tmdbService = ServiceFactory.createRetrofitService(TMDBService.class, TMDBService.SERVICE_END);
        Observable<Credits> creditsObservable = tmdbService.getCredits(movieId);
        creditsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Credits>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: Cast peoples length" + castList.size());
                        Log.d(TAG, "onCompleted: Crew people length length" + crewList.size() + "\n" + crewList);
                        castRecyclerView = new CastRecyclerView(CreditDetailActivity.this, castList);
                        cast_list_recycler_view.setAdapter(castRecyclerView);
                        cast_list_recycler_view.setLayoutManager(linearLayoutManager);

                        cast_list_recycler_view.setMinimumHeight(900);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Credits credits) {
                        Log.d(TAG, "onNext: Getting Credit lists....");
                        CreditDetailActivity.castList = credits.getCast();
                        CreditDetailActivity.crewList = credits.getCrew();
                    }
                });
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.detail_faid);
        slide.setSlideEdge(Gravity.RIGHT);
        getWindow().setExitTransition(slide);
    }

    private void getParameters() {
        Intent params = getIntent();
        movieUrl = params.getStringExtra(MovieConstants.IMAGE_URL);
        Log.d(TAG, "getParameters: movieUrl:"+movieUrl);
        movieId = params.getIntExtra(MovieConstants.MOVIE_ID,0);
        Log.d(TAG, "getParameters: Movie Id:"+movieId);
        movieName = params.getStringExtra(MovieConstants.MOVIE_NAME);
        overview = params.getStringExtra(MovieConstants.MOVIE_OVERVIEW);
        initializeViewComponents();
    }

    private void initializeViewComponents() {
        large_poster = (ImageView) findViewById(R.id.large_poster);
        movie_overview = (TextView) findViewById(R.id.movie_overview);
        movie_overview.setText(overview.toString());

        cast_list_recycler_view = (RecyclerView) findViewById(R.id.cast_list_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cast_list_recycler_view.setLayoutManager(linearLayoutManager);
        castRecyclerView = new CastRecyclerView(this);
        cast_list_recycler_view.setAdapter(castRecyclerView);

        Uri uri = Uri.parse(MovieUtility.IMG_URL + movieUrl);
        Picasso.with(CreditDetailActivity.this).load(uri).fit().into(large_poster);
        setTitle(movieName);
    }
}
