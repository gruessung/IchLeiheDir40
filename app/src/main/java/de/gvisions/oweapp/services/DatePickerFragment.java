package de.gvisions.oweapp.services;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by alexa on 10.01.2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String formattedDate;
    EditText v = null;

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        formattedDate = sdf.format(c.getTime());

        if (v == null) {
            throw new RuntimeException("EditText muss per setView() festgelegt werden.");
        } else {
            v.setText(formattedDate);
        }

    }

    /**
     * Setzt das EditText Feld, in welches das Datum geschrieben wird
     * @param view
     */
    public void setView(EditText view) {
        v = view;
    }
}

