package com.codepath.newssearch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.codepath.newssearch.model.SearchFilter;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chanis on 1/30/17.
 */

public class DateDialogFragment extends DialogFragment {
    private SearchFilter searchFilter;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = 0;
        int month = 0;
        int day = 0;

        searchFilter = Parcels.unwrap(getArguments().getParcelable("searchFilter"));

        String selectedDate = searchFilter.getDate();
        if (!selectedDate.isEmpty()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

                c.setTime(format.parse(selectedDate));
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }


        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
