package com.syaona.petalierapp.dialog;

import android.util.Log;
import com.android.datetimepicker.date.DatePickerDialog;
import com.syaona.petalierapp.core.BaseActivity;
import java.util.Calendar;

/**
 * Created by devsmartwave on 6/5/17.
 */

public class PChooseCalendar implements DatePickerDialog.OnDateSetListener{

    private Calendar calendar;

    public PChooseCalendar(BaseActivity baseActivity) {
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


        Log.d("selected date", monthOfYear+1 +"/"+dayOfMonth+"/"+year);


        }


    }




