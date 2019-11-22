package com.example.ifind.settingFunction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.ifind.R;
import com.example.ifind.compareFunction.ComparePictureList;

public class SettingAlarm extends AppCompatActivity {

    Switch settingAlarm;
    CheckBox seoul, gyungki, incheon, gangwon, choongbook, daegeon, saejong, choongnam, jeonbook, jeonnam, gwangju, gyungbook, daegu, gyungnam, woolsan, busan, jeju;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        settingAlarm = (Switch) findViewById(R.id.selectAlarm);

        seoul= (CheckBox) findViewById(R.id.seoul);
        gyungki=(CheckBox) findViewById(R.id.gyungki);
        incheon=(CheckBox) findViewById(R.id.incheon);
        gangwon=(CheckBox) findViewById(R.id.gangwon);
        choongbook=(CheckBox) findViewById(R.id.choongbook);
        daegeon=(CheckBox) findViewById(R.id.daegeon);
        saejong=(CheckBox) findViewById(R.id.saejong);
        choongnam=(CheckBox) findViewById(R.id.choongnam);
        jeonbook=(CheckBox) findViewById(R.id.jeonbook);
        jeonnam=(CheckBox) findViewById(R.id.jeonnam);
        gwangju=(CheckBox) findViewById(R.id.gwangju);
        gyungbook=(CheckBox) findViewById(R.id.gyungbook);
        daegu=(CheckBox) findViewById(R.id.daegu);
        gyungnam=(CheckBox) findViewById(R.id.gyungnam);
        woolsan=(CheckBox) findViewById(R.id.woolsan);
        busan=(CheckBox) findViewById(R.id.busan);
        jeju=(CheckBox) findViewById(R.id.jeju);

        if(settingAlarm.isChecked() != true) {
            seoul.setEnabled(false);
            gyungki.setEnabled(false);
            incheon.setEnabled(false);
            gangwon.setEnabled(false);
            choongbook.setEnabled(false);
            daegeon.setEnabled(false);
            saejong.setEnabled(false);
            choongnam.setEnabled(false);
            jeonbook.setEnabled(false);
            jeonnam.setEnabled(false);
            gwangju.setEnabled(false);
            gyungbook.setEnabled(false);
            daegu.setEnabled(false);
            gyungnam.setEnabled(false);
            woolsan.setEnabled(false);
            busan.setEnabled(false);
            jeju.setEnabled(false);
        }

        settingAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(settingAlarm.isChecked() == true) {
                    seoul.setEnabled(true);
                    gyungki.setEnabled(true);
                    incheon.setEnabled(true);
                    gangwon.setEnabled(true);
                    choongbook.setEnabled(true);
                    daegeon.setEnabled(true);
                    saejong.setEnabled(true);
                    choongnam.setEnabled(true);
                    jeonbook.setEnabled(true);
                    jeonnam.setEnabled(true);
                    gwangju.setEnabled(true);
                    gyungbook.setEnabled(true);
                    daegu.setEnabled(true);
                    gyungnam.setEnabled(true);
                    woolsan.setEnabled(true);
                    busan.setEnabled(true);
                    jeju.setEnabled(true);
                }
                else {
                    seoul.setEnabled(false);
                    gyungki.setEnabled(false);
                    incheon.setEnabled(false);
                    gangwon.setEnabled(false);
                    choongbook.setEnabled(false);
                    daegeon.setEnabled(false);
                    saejong.setEnabled(false);
                    choongnam.setEnabled(false);
                    jeonbook.setEnabled(false);
                    jeonnam.setEnabled(false);
                    gwangju.setEnabled(false);
                    gyungbook.setEnabled(false);
                    daegu.setEnabled(false);
                    gyungnam.setEnabled(false);
                    woolsan.setEnabled(false);
                    busan.setEnabled(false);
                    jeju.setEnabled(false);
                }
            }
        });

        seoul.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(seoul.isChecked() == true) {

                }
                else {

                }
            }
        });
        gyungki.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gyungki.isChecked() == true) {

                }
                else {

                }
            }
        });
        incheon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(incheon.isChecked() == true) {

                }
                else {

                }
            }
        });
        gangwon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gangwon.isChecked() == true) {

                }
                else {

                }
            }
        });
        choongbook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(choongbook.isChecked() == true) {

                }
                else {

                }
            }
        });
        daegeon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(daegeon.isChecked() == true) {

                }
                else {

                }
            }
        });
        saejong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(saejong.isChecked() == true) {

                }
                else {

                }
            }
        });
        choongnam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(choongnam.isChecked() == true) {

                }
                else {

                }
            }
        });
        jeonbook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jeonbook.isChecked() == true) {

                }
                else {

                }
            }
        });
        jeonnam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jeonnam.isChecked() == true) {

                }
                else {

                }
            }
        });
        gwangju.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gwangju.isChecked() == true) {

                }
                else {

                }
            }
        });
        gyungbook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gyungbook.isChecked() == true) {

                }
                else {

                }
            }
        });
        daegu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(daegu.isChecked() == true) {

                }
                else {

                }
            }
        });
        gyungnam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gyungnam.isChecked() == true) {

                }
                else {

                }
            }
        });
        woolsan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(woolsan.isChecked() == true) {

                }
                else {

                }
            }
        });
        busan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(busan.isChecked() == true) {

                }
                else {

                }
            }
        });
        jeju.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jeju.isChecked() == true) {

                }
                else {

                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);

        getSupportActionBar().setTitle("IFind");

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_setting) {
            i=new Intent(getApplicationContext(), SettingMain.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_camera) {
            i=new Intent(getApplicationContext(), ComparePictureList.class);
            startActivity(i);
            return true;
        }
        return false;
    }
}
