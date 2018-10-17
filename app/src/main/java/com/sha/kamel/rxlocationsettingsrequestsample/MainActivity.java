package com.sha.kamel.rxlocationsettingsrequestsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.LocationRequest;
import com.sha.kamel.rxlocationsettingsrequest.RxGps;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocation();
    }

    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(2 * 1000);

        disposable = new RxGps()
                .enable(locationRequest, MainActivity.this)
                .subscribe(isLocationRequested -> {
                    if (isLocationRequested){
                        // Location is now enabled and you can implement your logic of
                        // location safely
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
