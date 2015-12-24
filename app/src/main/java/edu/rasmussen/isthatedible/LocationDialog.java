package edu.rasmussen.isthatedible;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Dialog for taking input to add a new location
 */
public class LocationDialog extends DialogFragment {
    EditText lat, longi, name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        name = (EditText) container.findViewById(R.id.text_dialog_name);
        lat = (EditText) container.findViewById(R.id.text_dialog_lat);
        longi = (EditText) container.findViewById(R.id.text_dialog_long);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    //    public LocationDialog(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setDialogLayoutResource(R.layout.dialog_add_location);
//    }
//
//    @Override
//    protected void onBindDialogView(View view) {
//        super.onBindDialogView(view);
//    }
//
//    @Override
//    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
//        builder.setTitle(R.string.pref_title_add_location);
//        super.onPrepareDialogBuilder(builder);
//    }
//
//    @Override
//    protected void onDialogClosed(boolean positiveResult) {
//        super.onDialogClosed(positiveResult);
//        persistBoolean(positiveResult);
//    }
}
