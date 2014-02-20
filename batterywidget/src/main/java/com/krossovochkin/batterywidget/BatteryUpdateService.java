/*
Copyright 2014 Vasya Drobushkov

This file is part of SimpleBatteryWidget.

SimpleBatteryWidget is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SimpleBatteryWidget is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SimpleBatteryWidget.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.krossovochkin.batterywidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Vasya Drobushkov <vasya.drobushkov@gmail.com> on 18.02.14.
 */
public class BatteryUpdateService extends Service {

    public static final String TAG = BatteryUpdateService.class.getSimpleName();

    private static final float BATTERY_FULL_WIDTH = 200;
    private static final float BATTERY_FULL_HEIGHT = 100;
    private static final float BATTERY_HEIGHT_INDENT = BATTERY_FULL_HEIGHT * 0.3f;
    private static final float BATTERY_FULL_PERCENT = 100;
    private static final float BATTERY_MAIN_PERCENT = 95;
    private static final float BATTERY_MAIN_WIDTH = BATTERY_MAIN_PERCENT * BATTERY_FULL_WIDTH / BATTERY_FULL_PERCENT;
    private static final String GRADIENT_FIRST_COLOR = "#94ba6d";
    private static final String GRADIENT_SECOND_COLOR = "#00ba6d";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        update();
        return super.onStartCommand(intent, flags, startId);
    }

    private void update() {
        int level = getBatteryStatus(this);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_main);
        remoteViews.setTextViewText(R.id.widget_textview, String.valueOf(level) + "%");
        remoteViews.setImageViewBitmap(R.id.battery_view, createBatteryBitmap(level));

        ComponentName thisWidget = new ComponentName(this, BatteryWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, remoteViews);
    }

    private int getBatteryStatus(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, filter);
        if (batteryStatus != null) {
            return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        } else {
            Log.e(TAG, "getBatteryStatus failed: batteryStatus is null");
            return -1;
        }
    }

    public Bitmap createBatteryBitmap(int levelInPercent) {
        Bitmap bitmap = Bitmap.createBitmap((int) BATTERY_FULL_WIDTH, (int) BATTERY_FULL_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        if (levelInPercent < BATTERY_MAIN_PERCENT) {
            paint.setColor(Color.RED);

            canvas.drawRect(levelInPercent * BATTERY_FULL_WIDTH / BATTERY_FULL_PERCENT, 0.0f, BATTERY_MAIN_WIDTH, BATTERY_FULL_HEIGHT, paint);
            canvas.drawRect(BATTERY_MAIN_WIDTH, BATTERY_HEIGHT_INDENT, BATTERY_FULL_WIDTH, BATTERY_FULL_HEIGHT - BATTERY_HEIGHT_INDENT, paint);

            LinearGradient gradient = getGradient();
            paint.setDither(true);
            paint.setShader(gradient);

            canvas.drawRect(0.0f, 0.0f, BATTERY_FULL_WIDTH * levelInPercent / BATTERY_FULL_PERCENT, BATTERY_FULL_HEIGHT, paint);
        } else if (levelInPercent == BATTERY_MAIN_PERCENT) {
            paint.setColor(Color.RED);

            canvas.drawRect(BATTERY_MAIN_WIDTH, BATTERY_HEIGHT_INDENT, BATTERY_FULL_WIDTH, BATTERY_FULL_HEIGHT - BATTERY_HEIGHT_INDENT, paint);

            LinearGradient gradient = getGradient();
            paint.setDither(true);
            paint.setShader(gradient);

            canvas.drawRect(0.0f, 0.0f, BATTERY_MAIN_WIDTH, BATTERY_FULL_HEIGHT, paint);
        } else if (levelInPercent == BATTERY_FULL_PERCENT) {
            LinearGradient gradient = getGradient();
            paint.setDither(true);
            paint.setShader(gradient);

            canvas.drawRect(BATTERY_MAIN_WIDTH, BATTERY_HEIGHT_INDENT, BATTERY_FULL_WIDTH, BATTERY_FULL_HEIGHT - BATTERY_HEIGHT_INDENT, paint);
            canvas.drawRect(0.0f, 0.0f, BATTERY_MAIN_WIDTH, BATTERY_FULL_HEIGHT, paint);
        } else { // BATTERY_MAIN_PERCENT < levelInPercent < BATTERY_FULL_PERCENT
            paint.setColor(Color.RED);

            canvas.drawRect(levelInPercent * BATTERY_FULL_WIDTH / BATTERY_FULL_PERCENT, BATTERY_HEIGHT_INDENT, BATTERY_FULL_WIDTH, BATTERY_FULL_HEIGHT - BATTERY_HEIGHT_INDENT, paint);

            LinearGradient gradient = getGradient();
            paint.setDither(true);
            paint.setShader(gradient);

            canvas.drawRect(0.0f, 0.0f, BATTERY_MAIN_WIDTH, BATTERY_FULL_HEIGHT, paint);
            canvas.drawRect(BATTERY_MAIN_WIDTH, BATTERY_HEIGHT_INDENT, BATTERY_FULL_WIDTH * levelInPercent / BATTERY_FULL_PERCENT, BATTERY_FULL_HEIGHT - BATTERY_HEIGHT_INDENT, paint);
        }

        return bitmap;
    }

    private LinearGradient getGradient() {
        return new LinearGradient(
                0.0f, BATTERY_FULL_HEIGHT, 0.0f, 0.0f,
                Color.parseColor(GRADIENT_FIRST_COLOR), Color.parseColor(GRADIENT_SECOND_COLOR),
                Shader.TileMode.CLAMP);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
