package sethi.kumar.hemendra.filmkhabar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import sethi.kumar.hemendra.filmkhabar.R;

/**
 * Created by hemendra on 04-03-2016.
 */
public class MoviePosterView extends RelativeLayout {
    public MoviePosterView(Context context) {
        super(context);
    }

    public MoviePosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoviePosterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.movie_list_layout,this);

    }

    /*public MoviePosterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/
}
