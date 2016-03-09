package sethi.kumar.hemendra.filmkhabar.viewadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sethi.kumar.hemendra.filmkhabar.R;

/**
 * Created by hemendra on 16-02-2016.
 */
public class CastListViewHolder extends RecyclerView.ViewHolder {
    private TextView people_name;
    private TextView name_in_movie;
    private ImageView people_image;

    public CastListViewHolder(View itemView) {
        super(itemView);
        people_name = (TextView) itemView.findViewById(R.id.people_name);
        name_in_movie = (TextView) itemView.findViewById(R.id.name_in_movie);
        people_image = (ImageView) itemView.findViewById(R.id.people_image);
    }

    public TextView getPeople_name() {
        return people_name;
    }

    public void setPeople_name(TextView people_name) {
        this.people_name = people_name;
    }

    public TextView getName_in_movie() {
        return name_in_movie;
    }

    public void setName_in_movie(TextView name_in_movie) {
        this.name_in_movie = name_in_movie;
    }

    public ImageView getPeople_image() {
        return people_image;
    }

    public void setPeople_image(ImageView people_image) {
        this.people_image = people_image;
    }
}
