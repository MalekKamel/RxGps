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

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class RxLocationSettingsRequest {

    static final int REQUEST_CHECK_SETTINGS = 0;
    private RxLocationSettingsRequestFrag frag;
    private PublishSubject<Boolean> ps = PublishSubject.create();
    private IntentSender intentSender;

    private synchronized void addFragment(@NonNull final FragmentManager fragmentManager) {
        frag = findFragment(fragmentManager);
        if (frag == null) {
            frag = RxLocationSettingsRequestFrag.newInstance(this::startResolutionForResult);
            fragmentManager
                    .beginTransaction()
                    .add(frag, RxLocationSettingsRequestFrag.class.getSimpleName())
                    .commitAllowingStateLoss();
            return;
        }
        startResolutionForResult(frag);
    }

    /**
     * Since we add the fragment using commitAllowingStateLoss, we need to start resolution
     * request only when the fragment is ready to request. So we call
     * this method when the fragment onCreate is called.
     */
    private void startResolutionForResult(RxLocationSettingsRequestFrag frag) {
        try {
            // show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().

            frag.startIntentSenderForResult(
                    intentSender,
                    REQUEST_CHECK_SETTINGS,
                    null,
                    0,
                    0,
                    0,
                    null);

            frag.listenToPeResolutionResult(this::onResult);

        } catch (Exception e) {
            // Ignore the error
            e.printStackTrace();
        }
    }

    private RxLocationSettingsRequestFrag findFragment(@NonNull final FragmentManager fragmentManager) {
        return (RxLocationSettingsRequestFrag) fragmentManager.findFragmentByTag(RxLocationSettingsRequestFrag.class.getSimpleName());
    }

    /**
     * Request to enable GPS
     * @param locationRequest location request
     * @param fragmentActivity fragment activity
     * @return single
     */
    public Single<Boolean> request(
            LocationRequest locationRequest,
            FragmentActivity fragmentActivity) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        LocationServices.getSettingsClient(fragmentActivity).checkLocationSettings(builder.build())
                .addOnSuccessListener(fragmentActivity, locationSettingsResponse -> onResult(true))
                .addOnFailureListener(fragmentActivity, e -> {
                    e.printStackTrace();
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            intentSender = ((ResolvableApiException) e).getResolution().getIntentSender();
                             addFragment(fragmentActivity.getSupportFragmentManager());
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            onResult(false);
                            break;
                    }
                });
        return Single.fromObservable(ps);
    }

    private void onResult(boolean result){
        ps.onNext(result);
        ps.onComplete();
    }

}
