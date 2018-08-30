package com.sha.kamel.rxlocationsettingsrequest;

import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import io.reactivex.subjects.PublishSubject;

public class RxLocationSettingsRequest {

    static final int REQUEST_CHECK_SETTINGS = 0;
    private RxLocationSettingsRequestFrag frag;
    private PublishSubject<Boolean> ps = PublishSubject.create();

    private synchronized void addFragment(@NonNull final FragmentManager fragmentManager) {
        frag = findFragment(fragmentManager);
        if (frag == null) {
            frag = RxLocationSettingsRequestFrag.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(frag, RxLocationSettingsRequestFrag.class.getSimpleName())
                    .commitNow();
        }
    }

    private RxLocationSettingsRequestFrag findFragment(@NonNull final FragmentManager fragmentManager) {
        return (RxLocationSettingsRequestFrag) fragmentManager.findFragmentByTag(RxLocationSettingsRequestFrag.class.getSimpleName());
    }

    public PublishSubject<Boolean> request(
            LocationRequest locationRequest,
            FragmentActivity fragmentActivity) {

        addFragment(fragmentActivity.getSupportFragmentManager());

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        LocationServices.getSettingsClient(fragmentActivity).checkLocationSettings(builder.build())
                .addOnSuccessListener(fragmentActivity, locationSettingsResponse -> onResult(true))
                .addOnFailureListener(fragmentActivity, e -> {
                    e.printStackTrace();
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // location settings are not satisfied, but this can be fixed by showing the user a dialog.
                            try {
                                // show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;

                                frag.startIntentSenderForResult(
                                        resolvable.getResolution().getIntentSender(),
                                        REQUEST_CHECK_SETTINGS,
                                        null,
                                        0,
                                        0,
                                        0,
                                        null);

                                frag.listenToPeResolutionResult(this::onResult);

                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error
                                e.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // location settings are not satisfied, however no way to fix the settings so don't show dialog
                            onResult(false);
                            break;
                    }
                });
        return ps;
    }

    private void onResult(boolean result){
        ps.onNext(result);
        ps.onComplete();
    }

}
