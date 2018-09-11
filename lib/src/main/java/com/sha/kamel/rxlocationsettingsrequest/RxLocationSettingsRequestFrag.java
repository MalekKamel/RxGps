package com.sha.kamel.rxlocationsettingsrequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.annimon.stream.function.BooleanConsumer;
import com.annimon.stream.function.Consumer;

public class RxLocationSettingsRequestFrag extends Fragment {

    private BooleanConsumer callback;
    private Consumer<RxLocationSettingsRequestFrag> readyCallback;


    public static RxLocationSettingsRequestFrag newInstance(Consumer<RxLocationSettingsRequestFrag> readyCallback) {
        RxLocationSettingsRequestFrag frag = new RxLocationSettingsRequestFrag();
        frag.readyCallback = readyCallback;
        return frag;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readyCallback.accept(this);
    }
}
