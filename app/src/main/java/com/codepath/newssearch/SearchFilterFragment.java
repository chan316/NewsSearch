package com.codepath.newssearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chanis on 1/30/17.
 */

public class SearchFilterFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Spinner spSort;
    private EditText etDate;

    public interface SearchFilterDialogListener {
        public void onSearchFilterClick(DialogFragment dialog);
    }

    SearchFilterDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (SearchFilterDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException((activity.toString()) + " must implement SearchFilterDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View filterView = layoutInflater.inflate(R.layout.filter_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        etDate = (EditText) filterView.findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        // Drop down menu for sorting options
        spSort = (Spinner) filterView.findViewById(R.id.spSort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(adapter);
        spSort.setOnItemSelectedListener(this);

        // Dynamically create checkboxes for topics
        LinearLayout llTopics = (LinearLayout) filterView.findViewById(R.id.llTopics);
        String[] categoryText = getResources().getStringArray(R.array.category_array_text);

        for (int i = 0; i < categoryText.length; i++) {
            CheckBox checkbox = new CheckBox(getActivity().getBaseContext());
            checkbox.setId(i);
            checkbox.setText(categoryText[i]);
            // TODO Figure out setTextAppearance or use styles
            checkbox.setBackgroundColor(Color.GRAY);
            checkbox.setPadding(10,10,10,10);

            llTopics.addView(checkbox);
        }

        builder.setView(filterView);
        return builder.create();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showDatePickerDialog(View v) {
        DateDialogFragment newFragment = new DateDialogFragment();
        newFragment.setTargetFragment(this, 300);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String urlDate = format.format(c.getTime());
        etDate.setText(urlDate);
    }
}
