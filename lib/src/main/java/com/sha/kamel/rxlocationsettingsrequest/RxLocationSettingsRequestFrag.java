package com.sha.kamel.rxlocationsettingsrequest;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.annimon.stream.function.BooleanConsumer;

public class RxLocationSettingsRequestFrag extends Fragment {

    private BooleanConsumer callback;

    public static RxLocationSettingsRequestFrag newInstance() {
        return new RxLocationSettingsRequestFrag();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RxLocationSettingsRequest.REQUEST_CHECK_SETTINGS:
                callback.accept(resultCode == Activity.RESULT_OK);
                break;
        }
    }

    public void listenToPeResolutionResult(BooleanConsumer callback) {
        this.callback = callback;
    }
}
