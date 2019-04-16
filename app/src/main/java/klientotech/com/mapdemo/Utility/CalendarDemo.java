package klientotech.com.mapdemo.Utility;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import klientotech.com.mapdemo.R;

public class CalendarDemo extends AppCompatActivity implements DatePickerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_demo);
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);
        picker
                .setListener(this)
                .setDays(30)
                .setOffset(0)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setTodayDateTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .showTodayButton(false)
                .init();

        picker.setBackgroundColor(Color.LTGRAY);
        Toast.makeText(this, formatDateTime(String.valueOf(new DateTime())), Toast.LENGTH_SHORT).show();
      //  picker.setDate(new DateTime());


    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Toast.makeText(this, formatDateTime(dateSelected.toString()), Toast.LENGTH_SHORT).show();
        // Log.i("HorizontalPicker", "Selected date is " + dateSelected.toString());


    }

    public static String formatDateTime(String tempDateTime) {
        if (tempDateTime != null) {
            SimpleDateFormat _24HrFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            _24HrFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat _12HrFormat = new SimpleDateFormat("MMMM ',' dd ',' yyyy", Locale.getDefault());
            _12HrFormat.setTimeZone(TimeZone.getDefault());
            try {
                Date _24HrDate = _24HrFormat.parse(tempDateTime);
                return _12HrFormat.format(_24HrDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        } else {
            return "";
        }

    }
}
