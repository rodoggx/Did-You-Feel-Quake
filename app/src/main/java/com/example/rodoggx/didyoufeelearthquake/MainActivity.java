package com.example.rodoggx.didyoufeelearthquake;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_URL);
           }

    private void updateUi(Event earthquake) {
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(earthquake.title);

        TextView numberOfPeopleView = (TextView) findViewById(R.id.number_of_people);
        numberOfPeopleView.setText(getString(R.string.num_people_felt, earthquake.numOfPeople));

        TextView perceivedStrengthView = (TextView) findViewById(R.id.perceived_magnitude);
        perceivedStrengthView.setText(earthquake.perceivedStrength);
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, Event> {
        @Override
        protected Event doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            Event earthquake = Utils.getEarthquakeData(urls[0]);
            return earthquake;
        }

        @Override
        protected void onPostExecute(Event earthquake) {
            if (earthquake == null) {
                return;
            }
            updateUi(earthquake);
        }
    }
}
