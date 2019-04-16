
package klientotech.com.mapdemo.Response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearByResponse implements Parcelable {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    protected NearByResponse(Parcel in) {
        results = in.createTypedArrayList(Result.CREATOR);
        status = in.readString();
    }

    public static final Creator<NearByResponse> CREATOR = new Creator<NearByResponse>() {
        @Override
        public NearByResponse createFromParcel(Parcel in) {
            return new NearByResponse(in);
        }

        @Override
        public NearByResponse[] newArray(int size) {
            return new NearByResponse[size];
        }
    };

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeString(status);
    }
}
