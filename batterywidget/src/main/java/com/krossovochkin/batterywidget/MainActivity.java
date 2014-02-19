// This file is part of SimpleBatteryWidget.
//
// SimpleBatteryWidget is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// SimpleBatteryWidget is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with SimpleBatteryWidget.  If not, see <http://www.gnu.org/licenses/>.

package com.krossovochkin.batterywidget;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Vasya Drobushkov <vasya.drobushkov@gmail.com> on 19.02.14.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
