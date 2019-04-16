package klientotech.com.mapdemo.Adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import klientotech.com.mapdemo.Activity.MainActivity;
import klientotech.com.mapdemo.R;
import klientotech.com.mapdemo.Response.Prediction;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private List<Prediction> predictionList;
    private Context context;
    private MainActivity mainActivity;

    public PlacesAdapter(Context context,MainActivity mainActivity, List<Prediction> predictionList) {
        this.context = context;
        this.predictionList = predictionList;
        this.mainActivity=mainActivity;

    }

    @NonNull
    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.ViewHolder holder, int position) {
        holder.placeName.setText(predictionList.get(position).getDescription());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setSearcedLocation(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (predictionList != null && predictionList.size() > 0)
            return predictionList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView placeName;
        protected LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            parentLayout=itemView.findViewById(R.id.parentLayout);
        }
    }
}
