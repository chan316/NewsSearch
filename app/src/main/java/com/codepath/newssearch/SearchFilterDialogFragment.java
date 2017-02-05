package com.codepath.newssearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.newssearch.model.SearchFilter;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chanis on 1/30/17.
 */

public class SearchFilterDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Spinner spSort;
    private Spinner spCategory;
    private EditText etDate;
    private Button btnSave;

    private SearchFilter searchFilter;

    private SearchFilterDialogListener mListener;

    public interface SearchFilterDialogListener {
        public void onSearchFilterClick(Parcelable p, DialogFragment dialog);
    }

    public SearchFilterDialogFragment() {

    }

    public static SearchFilterDialogFragment newInstance(Bundle bundle) {
        SearchFilterDialogFragment frag = new SearchFilterDialogFragment();
        frag.setArguments(bundle);
        return frag;
    }

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
        searchFilter = (SearchFilter) Parcels.unwrap(getArguments().getParcelable("searchFilter"));
        Toast.makeText(getContext(), searchFilter.getDate(), Toast.LENGTH_SHORT).show();

        View filterView = layoutInflater.inflate(R.layout.filter_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Date picker
        etDate = (EditText) filterView.findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        if (searchFilter.getDate() != "") {
            etDate.setText(searchFilter.getDate());
        }

        // Drop down menu for sorting options
        spSort = (Spinner) filterView.findViewById(R.id.spSort);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(sortAdapter);
        spSort.setOnItemSelectedListener(this);

        for (int i = 0; i < sortAdapter.getCount(); i++) {
            if (sortAdapter.getItem(i).toString() == searchFilter.getSort()) {
                spSort.setSelection(i);
                break;
            }
        }

        // Drop down menu for categories
        spCategory = (Spinner) filterView.findViewById(R.id.spCategory);
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(catAdapter);
        spCategory.setOnItemSelectedListener(this);

        for (int i = 0; i < catAdapter.getCount(); i++) {
            if (catAdapter.getItem(i).toString() == searchFilter.getCategory()) {
                spCategory.setSelection(i);
                break;
            }
        }

        // Save button
        btnSave = (Button) filterView.findViewById(R.id.btnSaveFilter);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = !etDate.getText().toString().isEmpty() ? etDate.getText().toString() : null;
                String sort = spSort.getSelectedItemPosition() > 0 ? spSort.getSelectedItem().toString() : null;
                String category = spCategory.getSelectedItemPosition() > 0 ? spCategory.getSelectedItem().toString() : null;
                int page = searchFilter.getPage();

                Parcelable wrapped = Parcels.wrap(new SearchFilter(searchFilter.getQuery(), date, sort, category, page));
                mListener.onSearchFilterClick(wrapped, SearchFilterDialogFragment.this);
            }

        });

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

        Bundle bundle = new Bundle();
        bundle.putParcelable("searchFilter", Parcels.wrap(searchFilter));

        DateDialogFragment newFragment = new DateDialogFragment();
        newFragment.setArguments(bundle);
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
        searchFilter.setDate(urlDate);
    }
}
