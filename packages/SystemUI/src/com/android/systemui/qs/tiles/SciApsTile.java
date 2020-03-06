/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.qs.tiles;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.R;

import com.android.systemui.qs.QSTile;

import java.util.Arrays;
import java.util.Objects;

//import com.android.systemui.sciaps.IXRFService;
import com.sciaps.xrfhardware.IXRFService;
import com.android.systemui.qs.QSTileView;

import android.os.IBinder;
import java.lang.reflect.Method;
import android.os.RemoteException;

public class SciApsTile extends QSTile<QSTile.State> {
    private static final String TAG = SciApsTile.class.getSimpleName();

    private int mCurrentUserId;

    private IXRFService mXrfService;
    private QSTileView mQSTileView;

    public SciApsTile(Host host) {
        super(host);
        Log.d(TAG, "SciApsTile");

        getServiceInstance();
    }

    private void getServiceInstance() {
        try {
            if (mXrfService == null) {
                Class localClass = Class.forName("android.os.ServiceManager");
                Method getService = localClass.getMethod("getService", new Class[]{String.class});
                if (getService == null) {
                    Log.d(TAG, "no getService found");
                    throw new IllegalStateException(
                            "android.os.ServiceManager getService method not found! We kinda need this for things to work!");
                }

                Object result = getService.invoke(localClass, "com.sciaps.xrfhardware.IXRFService");
                if (result == null) {
                    result = getService.invoke(localClass, "XRFService");
                    if (result == null) {
                        Log.d(TAG, "no XRFService found");
                        throw new IllegalAccessException("com.sciaps.xrfhardware.IXRFService not running");
                    }
                }
                Log.d(TAG, "wow XRFService found");

                IBinder binder = (IBinder) result;
                mXrfService = IXRFService.Stub.asInterface(binder);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception" + e.toString());
        }
    }

    @Override
    protected void handleDestroy() {
        super.handleDestroy();
    }

    @Override
    protected State newTileState() {
        return new State();
    }

    @Override
    public void handleClick() {
        Log.i(TAG, "handle click");
    }

    @Override
    protected void handleUpdateState(State state, Object arg) {
        Log.i(TAG, "handleUpdateState");
        state.visible = true;
        String version = "unknown";
        int armStatus = -10;
        try {
            armStatus = mXrfService.getArmStatus();
//            version = mXrfService.versionInfo();
        } catch (RemoteException e) {
            Log.d(TAG, "remote exception" + e.toString());
        }
        state.label = "SciAps: " + armStatus;//version;
        state.icon = ResourceIcon.get(R.drawable.ic_qs_apn1);
        //state.iconId = R.drawable.ic_qs_apn1;
    }

    @Override
    public QSTileView createTileView(Context context) {
        mQSTileView = new QSTileView(context);
        return mQSTileView;
    }

    public void setListening(boolean listening) {
        // empty
    }
}

