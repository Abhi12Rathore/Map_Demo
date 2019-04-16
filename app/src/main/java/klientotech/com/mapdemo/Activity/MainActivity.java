package klientotech.com.mapdemo.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import klientotech.com.mapdemo.Adaper.PlacesAdapter;
import klientotech.com.mapdemo.ApiClass.Api;
import klientotech.com.mapdemo.ApiClass.ApiClient;
import klientotech.com.mapdemo.Response.NearByResponse;
import klientotech.com.mapdemo.Utility.FetchURL;
import klientotech.com.mapdemo.R;
import klientotech.com.mapdemo.Response.PlaceResponse;
import klientotech.com.mapdemo.Response.Prediction;
import klientotech.com.mapdemo.Utility.TaskLoadedCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleApiClient.ConnectionCallbacks {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng currentLocation, newLocation;
    private Polyline currentPolyLine;
    private RelativeLayout searchBar;
    private ImageView type, nearBy;
    private Api apiInterface;
    private AutoCompleteTextView txtName;
    private RecyclerView recylerView;
    private List<LatLng> locationList;
    private List<Prediction> predictionList;
    private int pos;
    private Dialog dialog;
    private FusedLocationProviderClient mFusedLocationClient;
    private BottomSheetDialog bottomSheetDialog;


    View.OnClickListener nearByClicked = v -> {
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.SheetDialog);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheetDialog.setContentView(R.layout.place_dialog);
        LinearLayout layoutRestraunts = bottomSheetDialog.findViewById(R.id.layoutRestraunts);
        LinearLayout layoutSchools = bottomSheetDialog.findViewById(R.id.layoutSchools);
        LinearLayout layoutHospital = bottomSheetDialog.findViewById(R.id.layoutHospital);
        LinearLayout layoutRailway = bottomSheetDialog.findViewById(R.id.layoutRailway);
        layoutRestraunts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNearByData("restaurant", 5000, currentLocation.latitude + "," + currentLocation.longitude);

            }
        });
        layoutHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNearByData("hospital", 5000, currentLocation.latitude + "," + currentLocation.longitude);

            }
        });
        layoutSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNearByData("school", 5000, currentLocation.latitude + "," + currentLocation.longitude);
            }
        });

        layoutRailway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNearByData("train_station", 10000, currentLocation.latitude + "," + currentLocation.longitude);
            }
        });

        View close = bottomSheetDialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    };

    private void loadNearByData(String type, int radius, String location) {
        apiInterface = ApiClient.getClient().create(Api.class);
        Call<NearByResponse> call = apiInterface.getNearbyPlaces(type, location, radius);
        call.enqueue(new Callback<NearByResponse>() {
            @Override
            public void onResponse(Call<NearByResponse> call, Response<NearByResponse> response) {
                if (response != null) {
                    NearByResponse dataResponse = response.body();
                    if (dataResponse != null) {
                        if (dataResponse.getStatus().equalsIgnoreCase("OK")) {
                            bottomSheetDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, NearByActivity.class);
                            intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) dataResponse.getResults());
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, dataResponse.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NearByResponse> call, Throwable t) {

            }
        });

    }


    View.OnClickListener typeClicked = v -> {
        List<String> typeList = new ArrayList<>();
        typeList.add("Normal");
        typeList.add("Terraine");
        typeList.add("Satelitte");
        typeList.add("Hybrid");
        PopupMenu menu = new PopupMenu(this, type);
        for (int i = 0; i < typeList.size(); i++) {
            menu.getMenu().add(Menu.NONE, i, Menu.NONE, typeList.get(i));
        }
        menu.show();
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                }
                return true;
            }
        });


    };

    View.OnClickListener searchBarClicked = v -> {
        dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.autocomplete_dialog);
        ImageView backButton = dialog.findViewById(R.id.backButton);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        txtName = dialog.findViewById(R.id.txtName);
        recylerView = dialog.findViewById(R.id.recylerView);
        recylerView.hasFixedSize();
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4) {
                    loadData(s.toString());
                }
                if (s.length() == 0) {
                    recylerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtName.getText().clear();
            }
        });
        dialog.show();
    };

    private void loadData(String location) {
        apiInterface = ApiClient.getClient().create(Api.class);
        Call<PlaceResponse> call = apiInterface.getPlaces(location);
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response != null) {
                    PlaceResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("OK")) {
                            recylerView.setVisibility(View.VISIBLE);
                            predictionList = response1.getPredictions();
                            recylerView.setAdapter(new PlacesAdapter(getApplicationContext(), MainActivity.this, predictionList));
                        } else {
                            recylerView.setAdapter(null);
                            recylerView.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, response1.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        recylerView.setAdapter(null);
                        Toast.makeText(MainActivity.this, "please try again", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initMap();
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        } else {
            checkLocationSettings();
        }

        type = findViewById(R.id.type);

        nearBy = findViewById(R.id.nearBy);
        nearBy.setOnClickListener(nearByClicked);
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnClickListener(searchBarClicked);
        type.setOnClickListener(typeClicked);
    }

    public boolean googlePlayServicesAvailable() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (apiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = apiAvailability.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "can't connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (mMap != null) {
            mMap.clear();
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    if (locationList.size() > 1) {
                        mMap.clear();
                    } else {
                        locationList.add(latLng);
                        newLocation = latLng;
                        Geocoder geocoder = new Geocoder(MainActivity.this);
                        List<Address> addressList = null;
                        try {
                            addressList = geocoder.getFromLocation(newLocation.latitude, newLocation.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addressList != null && addressList.size() > 0) {
                            String locatlity = addressList.get(0).getLocality();
                            String area = addressList.get(0).getAddressLine(0);
                            String country = addressList.get(0).getCountryName();
                            String address = area + " " + locatlity + " " + country;
                            int height = 50;
                            int width = 50;
                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_icon);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            MarkerOptions options = new MarkerOptions().title(area).position(newLocation).snippet("your new location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).draggable(true);
                            mMap.addMarker(options.position(newLocation));
                            Toast.makeText(MainActivity.this, String.valueOf(CalculationByDistance(currentLocation, newLocation)) + "  km", Toast.LENGTH_SHORT).show();
                        }

                        String url = getUrl(newLocation, currentLocation, "driving");
                        new FetchURL(MainActivity.this).execute(url, "driving");
                    }

                }
            });

        }


    }

    private String getUrl(LatLng newLocation, LatLng currentLocation, String mode) {
        String ori_gin = "origin=" + currentLocation.latitude + "," + currentLocation.longitude;
        String destination = "destination=" + newLocation.latitude + "," + newLocation.longitude;
        String md = "mode=" + mode;
        String paramter = ori_gin + "&" + destination + "&" + md;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + paramter + "&key=AIzaSyAOeY3-D_ZRb3skTbZAfpue-zWOF639oRQ";
        return url;
    }

    private void goToLocation(double lat, double lon) throws IOException {
        locationList = new ArrayList<>();
        currentLocation = new LatLng(lat, lon);
        locationList.add(currentLocation);
        CameraUpdate update = CameraUpdateFactory.newLatLng(currentLocation);
        mMap.moveCamera(update);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
        String locatlity = addressList.get(0).getLocality();
        String area = addressList.get(0).getAddressLine(0);
        String country = addressList.get(0).getCountryName();
        String address = area + " " + locatlity + " " + country;
        int height = 50;
        int width = 50;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_icon);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions options = new MarkerOptions().title(address).position(currentLocation).snippet("your  location")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).draggable(true);
        mMap.addMarker(options.position(currentLocation));
    }


    LocationRequest locationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyLine != null) {
            currentPolyLine.remove();
        } else {
            currentPolyLine = mMap.addPolyline((PolylineOptions) values[0]);

        }

    }

    public void setSearcedLocation(int pos) {
        dialog.dismiss();
        this.pos = pos;
        String location = predictionList.get(pos).getDescription();
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            int height = 50;
            int width = 50;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_icon);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            MarkerOptions options = new MarkerOptions().title(location).position(latLng).snippet("your  location")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).draggable(true);
            mMap.addMarker(options.position(latLng));
            CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
            mMap.moveCamera(update);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0) {
                if ((permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkLocationSettings();
                } else {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage("Please provide permission for access your location..........").setTitle("Important permission required");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    protected void checkLocationSettings() {
        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    if (response != null) {
                        getLastLocation();
                    }

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(MainActivity.this, 102);
                            } catch (IntentSender.SendIntentException e) {

                            } catch (ClassCastException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });


    }

    @SuppressLint("MissingPermission")

    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    try {
                        goToLocation(lat, lon);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    getLastLocation();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 102:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getLastLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        checkLocationSettings();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6378;
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
