/*
* Copyright (C) 2013 The OmniROM Project
* Copyright (C) 2021 The dot OS Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.derp.device.DeviceExtras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.PreferenceManager;

import org.derp.device.DeviceExtras.TouchscreenGestureSettings;

public class Startup extends BroadcastReceiver {

    private boolean mHBM = false;

    private static final String TAG = "BootReceiver";
    private static final String ONE_TIME_TUNABLE_RESTORE = "hardware_tunable_restored";

    @Override
    public void onReceive(final Context context, final Intent bootintent) {

        boolean enabled = false;
        TouchscreenGestureSettings.MainSettingsFragment.restoreTouchscreenGestureStates(context);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_HBM_SWITCH, false);
        if (enabled) {
        restore(HBMModeSwitch.getFile(), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_DC_SWITCH, false);
        if (enabled) {
        restore(DCModeSwitch.getFile(), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_FPS_INFO, false);
        if (enabled) {
            context.startService(new Intent(context, FPSInfoService.class));
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_GAME_SWITCH, false);
        if (enabled) {
        restore(GameModeSwitch.getFile(), enabled);
        }
        DeviceExtras.restoreSliderStates(context);
        org.derp.device.DeviceExtras.doze.DozeUtils.checkDozeService(context);
        org.derp.device.DeviceExtras.kcal.KCalSettings.restore(context);
        VibratorStrengthPreference.restore(context);
    }


    private boolean hasRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(ONE_TIME_TUNABLE_RESTORE, false);
    }

    private void setRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(ONE_TIME_TUNABLE_RESTORE, true).apply();
    }

    private void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        if (enabled) {
            FileUtils.writeValue(file, "1");
        }
    }

    private void restore(String file, String value) {
        if (file == null) {
            return;
        }
        FileUtils.writeValue(file, value);
    }
}
