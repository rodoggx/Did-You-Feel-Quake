package com.example.rodoggx.didyoufeelearthquake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Event earthquake = Utils.getEarthquakeData(USGS_URL);
        
        updateUi(earthquake);
    }

    private void updateUi(Event earthquake) {
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(earthquake.title);

        TextView numberOfPeopleView = (TextView) findViewById(R.id.number_of_people);
        numberOfPeopleView.setText(getString(R.string.num_people_felt, earthquake.numOfPeople));

        TextView perceivedStrengthView = (TextView) findViewById(R.id.perceived_magnitude);
        perceivedStrengthView.setText(earthquake.perceivedStrength);
    }
}
