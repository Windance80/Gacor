package com.kingstone.smith.gacor.HeatSpot;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.kingstone.smith.gacor.R;
import com.kingstone.smith.gacor.data.GacorContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddHeatspotActivity extends AppCompatActivity {

    static EditText mEditTextTime;
    static EditText mEditTextDate;
    EditText mEditTextLocation;
    Button mButtonSimpan;
    FrameLayout mFrameLayout;

    static int mYear;
    static int mMonth;
    static int mDay;
    static int mHour;
    static int mMinute;
    static Calendar mCalendar;

    //table var
    long mPlaceDate;
    String mPlaceName;
    String mPlaceDetail;
    double mPlaceLat;
    double mPlaceLng;


    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_heatspot);

        mFrameLayout = findViewById(R.id.progressBarHolder);

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        mEditTextTime = findViewById(R.id.time);
        mEditTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        //Initialize the time
        setTime();

        mEditTextDate = findViewById(R.id.date);
        mEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        //Initialize the date
        setDate();

        final PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        mEditTextLocation = findViewById(R.id.location);
        mEditTextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(builder.build(AddHeatspotActivity.this), PLACE_PICKER_REQUEST);
                    mFrameLayout.setVisibility(View.VISIBLE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mButtonSimpan = findViewById(R.id.button_simpan);
        mButtonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar.clear();
                mCalendar.set(mYear, mMonth, mDay, mHour, mMinute);
                mPlaceDate = mCalendar.getTimeInMillis();

                // insert to db Heatspot entry
                ContentValues values = new ContentValues();
                values.put(GacorContract.HeatspotEntry.COLUMN_DATE, mPlaceDate);
                values.put(GacorContract.HeatspotEntry.COLUMN_NAME, mPlaceName);
                values.put(GacorContract.HeatspotEntry.COLUMN_DETAIL, mPlaceDetail);
                values.put(GacorContract.HeatspotEntry.COLUMN_LAT, mPlaceLat);
                values.put(GacorContract.HeatspotEntry.COLUMN_LANG, mPlaceLng);

                long id;
                try {
                    Uri newUri = getContentResolver().insert(GacorContract.HeatspotEntry.CONTENT_URI, values);
                    id = ContentUris.parseId(newUri);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(AddHeatspotActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( id > 0) {
                    Toast.makeText(AddHeatspotActivity.this, "Places added!", Toast.LENGTH_LONG).show();
                    finish();
//                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AddHeatspotActivity.this, "Insert failed! InsertErrorCode " + String.valueOf(id), Toast.LENGTH_LONG).show();
                }

//                String localizedDayName = new SimpleDateFormat("HH:mm").format(lDate);
//                Toast.makeText(getApplicationContext(), "hh:mm " + localizedDayName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            mFrameLayout.setVisibility(View.GONE);
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                mEditTextLocation.setText(place.getName() + "\n" + place.getLatLng().toString());

                mPlaceName = place.getName().toString();
                mPlaceDetail = place.getAddress().toString();
                mPlaceLat = ((double)((int)(place.getLatLng().latitude * 1000000.0)))/1000000.0;
                mPlaceLng = ((double)((int)(place.getLatLng().longitude * 1000000.0)))/1000000.0;

//                String latLong = String.format("LatLong: @s", place.getLatLng());
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(this, toastMsg + " " + place.getLatLng().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = mCalendar.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            mHour = hourOfDay;
            mMinute = minute;
            setTime();
        }
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            mYear = year;
            mMonth = month;
            mDay = day;
            setDate();
        }
    }

    private static void setTime() {
        mCalendar.clear();
        mCalendar.set(mYear, mMonth, mDay, mHour, mMinute);
        mEditTextTime.setText(new SimpleDateFormat("HH:mm").format(mCalendar.getTimeInMillis()));
    }

    private static void setDate() {
        mCalendar.clear();
        mCalendar.set(mYear, mMonth, mDay, mHour, mMinute);
        mEditTextDate.setText(new SimpleDateFormat("EEEE, dd MMM yyyy").format(mCalendar.getTimeInMillis()));
    }
}
