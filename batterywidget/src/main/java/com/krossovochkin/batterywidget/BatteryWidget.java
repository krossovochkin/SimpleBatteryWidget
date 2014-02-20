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

/**
 * Created by Vasya Drobushkov <vasya.drobushkov@gmail.com> on 18.02.14.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class BatteryWidget extends AppWidgetProvider {

    private static final int UPDATE_TIME_MILLIS = 60000;

    private PendingIntent service = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final Intent intent = new Intent(context, BatteryUpdateService.class);

        if (service == null) {
            service = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTime().getTime(), UPDATE_TIME_MILLIS, service);
    }

    @Override
    public void onDisabled(Context context) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(service);
    }
}
