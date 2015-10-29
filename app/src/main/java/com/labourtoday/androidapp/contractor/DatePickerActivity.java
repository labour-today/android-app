package com.labourtoday.androidapp.contractor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.labourtoday.androidapp.Constants;
import com.labourtoday.androidapp.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.GregorianCalendar;

public class DatePickerActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private DatePicker datePicker;
    private EditText jobAddress;

    private final int PROFILE = 0;
    private final int LOG_OUT = 1;

    private Drawer result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Worker start date and time");
        getSupportActionBar().setSubtitle("When do you need your worker?");

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Profile"),
                        new SecondaryDrawerItem().withName("Log out")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case PROFILE:
                                Intent hireIntent = new Intent(DatePickerActivity.this, ContractorProfileActivity.class);
                                startActivity(hireIntent);
                                return true;
                            case LOG_OUT:
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                settings.edit().remove(Constants.AUTH_TOKEN).apply();
                                settings.edit().remove(Constants.LAST_LOGIN).apply();
                                // Return to the welcome page
                                Intent welcomeIntent = new Intent(DatePickerActivity.this, ContractorLoginActivity.class);
                                startActivity(welcomeIntent);
                                finish();
                                return true;
                            default:
                                return false;
                        }
                    }
                })
                .build();

        FloatingActionButton next = (FloatingActionButton) findViewById(R.id.next);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        jobAddress = (EditText) findViewById(R.id.jobAddress);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jobAddress.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the address of the job site",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                final String startDate = datePicker.getYear() + " " + datePicker.getMonth() + " " + datePicker.getDayOfMonth();
                final String startTime = timePicker.getCurrentHour() + " " + timePicker.getCurrentMinute();

                Intent i = new Intent(getApplicationContext(), HiringActivity.class);
                i.putExtra("start_date", startDate);
                i.putExtra("start_time", startTime);
                i.putExtra("job_address", jobAddress.getText().toString());
                i.putExtra("start_day", DateFormat.format("EEEE",
                        new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth())).toString());
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }
    }
}


