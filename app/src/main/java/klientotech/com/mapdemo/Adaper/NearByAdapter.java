package klientotech.com.mapdemo.Adaper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import klientotech.com.mapdemo.Activity.PlaceDetails;
import klientotech.com.mapdemo.ApiClass.Api;
import klientotech.com.mapdemo.ApiClass.ApiClient;
import klientotech.com.mapdemo.R;
import klientotech.com.mapdemo.Response.NearByResponse;
import klientotech.com.mapdemo.Response.Result;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.ViewHolder> {
    private List<Result> resultList;
    private Context context;
    private Api apiInterface;


    public NearByAdapter(Context context, List<Result> results) {
        this.context = context;
        this.resultList = results;
    }

    @NonNull
    @Override
    public NearByAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByAdapter.ViewHolder holder, int position) {
        holder.name.setText(resultList.get(position).getName());
        holder.rating.setText(String.valueOf(resultList.get(position).getRating()));
        holder.address.setText(resultList.get(position).getVicinity());
        try {
            if (resultList.get(position).getPhotos() != null && resultList.get(position).getPhotos().size() > 0) {
                Picasso.with(context)
                        .load(getPhoto(resultList.get(position).getPhotos().get(0).getPhotoReference()))
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.no_image)
                        .into(holder.imageView);

            }
        } catch (Exception e) {

        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceDetails.class);
                intent.putExtra("placeId", resultList.get(position).getPlaceId());
                context.startActivity(intent);
            }
        });


    }

    private String getPhoto(String photoReference) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth=" + 400);
        url.append("&photoreference=" + photoReference);
        url.append("&key=" + context.getResources().getString(R.string.api_key));
        return url.toString();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView name, address, rating;
        protected LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            rating = itemView.findViewById(R.id.rating);
            parentLayout = itemView.findViewById(R.id.parentLayout);

        }
    }
}
