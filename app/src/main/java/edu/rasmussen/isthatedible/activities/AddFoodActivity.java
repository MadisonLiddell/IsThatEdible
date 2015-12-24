package edu.rasmussen.isthatedible.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import edu.rasmussen.isthatedible.FoodList;
import edu.rasmussen.isthatedible.R;
import edu.rasmussen.isthatedible.database.FoodsDataSource;
import edu.rasmussen.isthatedible.fragments.DatePickerFragment;

/**
 * Activity handling the adding a new food item interaction.
 */
public class AddFoodActivity extends AppCompatActivity implements DatePickerFragment.DialogListener{
    private GregorianCalendar calendar;         // holds user's currently selected expiration date

    private EditText name;
    private EditText expiration;
    private CheckBox notify;
    private Spinner notifyPeriod;
    private Spinner type;
    private CheckBox locationNotify;
    private RadioGroup radioGroup;

    private FoodsDataSource foodsDataSource;

    public static final int ADD_FOOD_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        name = (EditText) findViewById(R.id.editTextName);
        type = (Spinner) findViewById(R.id.spinnerType);
        notify = (CheckBox) findViewById(R.id.checkBoxNotifyExpiration);
        notifyPeriod = (Spinner) findViewById(R.id.spinnerNotifyPeriod);
        locationNotify = (CheckBox) findViewById(R.id.checkBoxLocationNotify);
        radioGroup = (RadioGroup) findViewById(R.id.radioStorage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        foodsDataSource = new FoodsDataSource(this);

        expiration = (EditText) findViewById(R.id.editTextExpiration);
        expiration.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDatePickerDialog(v);
                return false;
            }
        });

        // Save button adds item to database and sends a copy back to main
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                FoodList.FoodItem item = saveFood(false);
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(FoodList.FoodItem.ITEM_KEY, item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Shows calendar picker popup
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePickerDialog");
    }

    // Receives selected calendar from calendar picker dialog fragment
    @Override
    public void updateDate(int year, int month, int day) {
        calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        // set the textview
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        sdf.setCalendar(calendar);
        expiration.setText(sdf.format(calendar.getTime()));
    }

    // Adds new food item to database
    private FoodList.FoodItem saveFood(boolean isPinned)
    {
        RadioButton selected = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        foodsDataSource.open();
        return foodsDataSource.addFoodItem(name.getText().toString(), expiration.getText().toString(),
                type.getSelectedItem().toString(), notifyPeriod.getSelectedItem().toString(),
                selected.getText().toString(), (notify.isChecked() ? 1 : 0),
                (locationNotify.isChecked() ? 1 : 0), (isPinned ? 1 : 0));
    }

    @Override
    protected void onPause() {
        super.onPause();
        foodsDataSource.close();
    }
}
