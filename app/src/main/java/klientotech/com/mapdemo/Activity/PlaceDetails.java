package klientotech.com.mapdemo.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import klientotech.com.mapdemo.Adaper.ImageAdapetr;
import klientotech.com.mapdemo.ApiClass.Api;
import klientotech.com.mapdemo.ApiClass.ApiClient;
import klientotech.com.mapdemo.R;
import klientotech.com.mapdemo.Response.NearByResponse;
import klientotech.com.mapdemo.Response.PlaceDetailResponse;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetails extends AppCompatActivity {
    private String placeId;
    private Api apiInterface;
    private ViewPager viewPage;
    private TextView name, number, website;
    private RatingBar rating;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        initUi();
    }

    private void initUi() {
        placeId = getIntent().getStringExtra("placeId");
        viewPage = findViewById(R.id.viewPage);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        rating = findViewById(R.id.rating);
        website = findViewById(R.id.website);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        apiInterface = ApiClient.getClient().create(Api.class);
        retrofit2.Call<PlaceDetailResponse> call = apiInterface.getDetail(placeId);
        call.enqueue(new Callback<PlaceDetailResponse>() {
            @Override
            public void onResponse(retrofit2.Call<PlaceDetailResponse> call, Response<PlaceDetailResponse> response) {
                if (response != null) {
                    PlaceDetailResponse detailResponse = response.body();
                    if (detailResponse != null) {
                        if (detailResponse.getStatus().equalsIgnoreCase("OK")) {
                            viewPage.setAdapter(new ImageAdapetr(getSupportFragmentManager(), detailResponse.getResult().getPhotos()));
                            name.setText(detailResponse.getResult().getName());
                            number.setText(detailResponse.getResult().getFormattedPhoneNumber());
                            website.setText(detailResponse.getResult().getWebsite());
                            rating.setRating(detailResponse.getResult().getRating());

                        } else {
                            viewPage.setAdapter(null);
                            Toast.makeText(PlaceDetails.this, detailResponse.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<PlaceDetailResponse> call, Throwable t) {

            }
        });


    }
}
