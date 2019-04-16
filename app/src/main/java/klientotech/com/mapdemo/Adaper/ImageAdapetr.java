package klientotech.com.mapdemo.Adaper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

import klientotech.com.mapdemo.Response.Photo;
import klientotech.com.mapdemo.fragment.PhotoFragment;

public class ImageAdapetr extends FragmentPagerAdapter {
    private List<Photo> photos;

    public ImageAdapetr(FragmentManager fm, List<Photo> photos) {
        super(fm);
        this.photos = photos;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", position);
        bundle.putParcelable("data", photos.get(position));
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int getCount() {
        return (photos != null && photos.size() > 0) ? photos.size() : 0;
    }
}
