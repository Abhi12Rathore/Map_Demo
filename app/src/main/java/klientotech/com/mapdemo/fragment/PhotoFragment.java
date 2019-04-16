package klientotech.com.mapdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import klientotech.com.mapdemo.R;
import klientotech.com.mapdemo.Response.Photo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {
    private ImageView imgNear;
    private Photo photo;
    private int pos;

    public PhotoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        imgNear = view.findViewById(R.id.imgNear);
        photo = getArguments().getParcelable("data");
        pos = getArguments().getInt("index");
        try {
            Picasso.with(getContext())
                    .load(getPhoto(photo.getPhotoReference(), photo.getHeight(), photo.getWidth()))
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(imgNear);



        } catch (Exception e) {

        }
        return view;
    }

    private String getPhoto(String photoReference, int heig, int widht) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth=" + widht);
        url.append("&photoreference=" + photoReference);
        url.append("&key=" + getResources().getString(R.string.api_key));
        return url.toString();
    }

}
