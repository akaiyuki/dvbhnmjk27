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
import java.util.GregorianCalendar;

/**
 * Created by smartwavedev on 5/13/16.
 */
public class PBirthdayPicker implements DatePickerDialog.OnDateSetListener{

    private Calendar calendar;
    private EditText mEditText;
    private Context mContext;

    public PBirthdayPicker(BaseActivity baseActivity, EditText editText) {
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

//        int dayOfWeek = GregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);

        mEditText.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);

    }

}
