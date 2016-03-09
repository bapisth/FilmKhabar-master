package sethi.kumar.hemendra.filmkhabar.viewadapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import sethi.kumar.hemendra.filmkhabar.R;
import sethi.kumar.hemendra.filmkhabar.model.Cast;
import sethi.kumar.hemendra.filmkhabar.utility.MovieUtility;

/**
 * Created by hemendra on 16-02-2016.
 */
public class CastRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cast> castList;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder viewHolder = null;
    private Context context = null;
    private Uri uri = null;

    public CastRecyclerView(Context context) {
        this.context = context;
    }

    public CastRecyclerView(Context context, List<Cast> castList) {
        this.castList = castList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_cast_details, null);
        viewHolder = new CastListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CastListViewHolder castListViewHolder = (CastListViewHolder)viewHolder;
        castListViewHolder.getPeople_name().setText(castList.get(position).getName());
        castListViewHolder.getName_in_movie().setText(castList.get(position).getCharacter().toString());
        uri = Uri.parse(MovieUtility.IMG_URL+castList.get(position).getProfile_path());
        Picasso.with(context).load(uri).fit().into(castListViewHolder.getPeople_image());
    }

    @Override
    public int getItemCount() {
        int v = castList== null ? 0:castList.size();
        Log.d(this.getClass().getSimpleName(), "getItemCount: Size" + v);
        return castList== null ? 0:castList.size();
    }

    public void updateList(List<Cast> castUpdateList){
        if (castList==null){
            castList = castUpdateList;
        }
        notifyDataSetChanged();
    }
}
