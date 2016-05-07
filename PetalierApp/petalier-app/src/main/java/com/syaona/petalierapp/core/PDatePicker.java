package com.syaona.petalierapp.core;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import com.android.datetimepicker.date.DatePickerDialog;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.enums.Singleton;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by smartwavedev on 4/19/16.
 */
public class PDatePicker implements DatePickerDialog.OnDateSetListener{

    private Calendar calendar;
    private EditText mEditText;
    private Context mContext;

    public PDatePicker(BaseActivity baseActivity, EditText editText) {
        this.mEditText = editText;
        calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //try
        //calendar.set(Calendar.YEAR, -18);
//        DatePickerDialog dpd = DatePickerDialog.newInstance
//                (this, (calendar.get(Calendar.YEAR) -18), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        dpd.show(baseActivity.getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);

        //try
        //int years = year - 7;
        //calendar.set(1997, monthOfYear, dayOfMonth);

//        mEditText.setText(monthOfYear+1 +"/"+dayOfMonth+"/"+year);


//        Calendar delDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
//        Calendar minAdultAge = new GregorianCalendar();
//        minAdultAge.add(Calendar.YEAR, -18);

        GregorianCalendar GregorianCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth - 1);

        int dayOfWeek = GregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);

        mEditText.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);

        Singleton.setSelectedDay(dayOfWeek);

        int dayMonth = GregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);
        Log.i("currentday", String.valueOf(dayOfWeek));

        Singleton.setChosenDay(dayMonth);


        // Initialize a new GradientDrawable
        GradientDrawable gd = new GradientDrawable();

        // Specify the shape of drawable
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set the fill color of drawable
        gd.setColor(Color.TRANSPARENT); // make the background transparent

        // Create a 2 pixels width red colored border for drawable
        gd.setStroke(2, Color.RED); // border width and color

        // Make the border rounded
        gd.setCornerRadius(15.0f); // border corner radius


        if (dayOfWeek == 3) {
            // Finally, apply the GradientDrawable as TextView background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mEditText.setBackground(gd);
            }

            Singleton.setSelectedDay(1);

        } else if (dayOfWeek == 4) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mEditText.setBackground(gd);
            }

            Singleton.setSelectedDay(1);

        } else {

//            Calendar calendar = Calendar.getInstance();
//            int day = GregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);
//
//
//            Log.e("currentcalendar", String.valueOf(day));
//
//            if (dayOfWeek != day){
                mEditText.setBackgroundResource(R.drawable.ic_small_box);
            Singleton.setSelectedDay(0);
//            } else {
//                // Finally, apply the GradientDrawable as TextView background
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    mEditText.setBackground(gd);
//                }
//                Singleton.setSelectedDay(1);
//        }




        }







    }



}
