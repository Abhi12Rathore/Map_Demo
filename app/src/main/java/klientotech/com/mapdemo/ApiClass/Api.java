package klientotech.com.mapdemo.ApiClass;

import klientotech.com.mapdemo.Response.NearByResponse;
import klientotech.com.mapdemo.Response.PlaceDetailResponse;
import klientotech.com.mapdemo.Response.PlaceResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("place/autocomplete/json?types=address&key=AIzaSyAOeY3-D_ZRb3skTbZAfpue-zWOF639oRQ")
    Call<PlaceResponse> getPlaces(@Query("input") String input);

    @GET("place/nearbysearch/json?sensor=true&key=AIzaSyAOeY3-D_ZRb3skTbZAfpue-zWOF639oRQ")
    Call<NearByResponse> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    @GET("place/details/json?fields=name,rating,website,photo,formatted_phone_number&key=AIzaSyAOeY3-D_ZRb3skTbZAfpue-zWOF639oRQ")
    Call<PlaceDetailResponse> getDetail(@Query("placeid") String placeid);


}
