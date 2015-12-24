package edu.rasmussen.isthatedible.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * @author Madison Liddell
 * @since 2015-10-07
 *
 * Date picker popup dialog. Updates date text display in caller activity.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    // Send year, month, day back to caller AddTask
    public void onDateSet(DatePicker view, int year, int month, int day) {
        DialogListener activity = (DialogListener) getActivity();
        activity.updateDate(year, month, day);
    }

    public interface DialogListener {
        void updateDate(int year, int month, int day);
    }
}