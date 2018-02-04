package com.dott.webapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;


/**
 * Created by jahid on 12/10/15.
 */
public class DatePickerFragment extends DialogFragment
         implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        TextView tv1= (TextView) getActivity().findViewById(R.id.date_of_birth);

        int yearData = view.getYear();
        int monthData = view.getMonth()+1;
        int dayData = view.getDayOfMonth();

        String monthString =   Integer.toString(monthData);
        String dayString =   Integer.toString(dayData);

        if (view.getMonth()<10)
        {
            monthString = "0"+monthString ;
        }

        if (view.getDayOfMonth()<10)
        {
            dayString = "0"+dayString ;
        }

        tv1.setText(yearData+"-"+monthString+"-"+dayString);

    }
}