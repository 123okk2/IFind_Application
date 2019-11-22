package com.example.ifind.settingFunction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.ifind.R;
import com.example.ifind.compareFunction.ComparePictureList;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingAlarm extends AppCompatActivity {

    Switch settingAlarm;
    CheckBox seoul, gyungki, incheon, gangwon, choongbook, daegeon, saejong, choongnam, jeonbook, jeonnam, gwangju, gyungbook, daegu, gyungnam, woolsan, busan, jeju;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        pref = getSharedPreferences("isSubscribed", MODE_PRIVATE);;

        settingAlarm = (Switch) findViewById(R.id.selectAlarm);

        Boolean isSubscribed = pref.getBoolean("wholeSub", false);

        if(isSubscribed) settingAlarm.setChecked(true);

        Boolean seoulSubscribed = pref.getBoolean("seoulSubscribed", false);
        Boolean gyungkiSubscribed = pref.getBoolean("gyungkiSubscribed", false);
        Boolean incheonSubscribed = pref.getBoolean("incheonSubscribed", false);
        Boolean gangwonSubscribed = pref.getBoolean("gangwonSubscribed", false);
        Boolean choongbookSubscribed = pref.getBoolean("choongbookSubscribed", false);
        Boolean daegeonSubscribed = pref.getBoolean("daegeonSubscribed", false);
        Boolean saejongSubscribed = pref.getBoolean("saejongSubscribed", false);
        Boolean choongnamSubscribed = pref.getBoolean("choongnamSubscribed", false);
        Boolean jeonbookSubscribed = pref.getBoolean("jeonbookSubscribed", false);
        Boolean jeonnamSubscribed = pref.getBoolean("jeonnamSubscribed", false);
        Boolean gwangjuSubscribed = pref.getBoolean("gwangjuSubscribed", false);
        Boolean gyungbookSubscribed = pref.getBoolean("gyungbookSubscribed", false);
        Boolean daeguSubscribed = pref.getBoolean("daeguSubscribed", false);
        Boolean gyungnamSubscribed = pref.getBoolean("gyungnamSubscribed", false);
        Boolean woolsanSubscribed = pref.getBoolean("woolsanSubscribed", false);
        Boolean busanSubscribed = pref.getBoolean("busanSubscribed", false);
        Boolean jejuSubscribed = pref.getBoolean("jejuSubscribed", false);

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
            seoul.setChecked(false);
            seoul.setEnabled(false);
            gyungki.setChecked(false);
            gyungki.setEnabled(false);
            incheon.setChecked(false);
            incheon.setEnabled(false);
            gangwon.setChecked(false);
            gangwon.setEnabled(false);
            choongbook.setChecked(false);
            choongbook.setEnabled(false);
            daegeon.setChecked(false);
            daegeon.setEnabled(false);
            saejong.setChecked(false);
            saejong.setEnabled(false);
            choongnam.setChecked(false);
            choongnam.setEnabled(false);
            jeonbook.setChecked(false);
            jeonbook.setEnabled(false);
            jeonnam.setChecked(false);
            jeonnam.setEnabled(false);
            gwangju.setChecked(false);
            gwangju.setEnabled(false);
            gyungbook.setChecked(false);
            gyungbook.setEnabled(false);
            daegu.setChecked(false);
            daegu.setEnabled(false);
            gyungnam.setChecked(false);
            gyungnam.setEnabled(false);
            woolsan.setChecked(false);
            woolsan.setEnabled(false);
            busan.setChecked(false);
            busan.setEnabled(false);
            jeju.setChecked(false);
            jeju.setEnabled(false);
        }
        else {
            if(seoulSubscribed) seoul.setChecked(true);
            if(gyungkiSubscribed) gyungki.setChecked(true);
            if(incheonSubscribed) incheon.setChecked(true);
            if(gangwonSubscribed) gangwon.setChecked(true);
            if(choongbookSubscribed) choongbook.setChecked(true);
            if(daegeonSubscribed) daegeon.setChecked(true);
            if(saejongSubscribed) saejong.setChecked(true);
            if(choongnamSubscribed) choongnam.setChecked(true);
            if(jeonbookSubscribed) jeonbook.setChecked(true);
            if(jeonnamSubscribed) jeonnam.setChecked(true);
            if(gwangjuSubscribed) gwangju.setChecked(true);
            if(gyungbookSubscribed) gyungbook.setChecked(true);
            if(daeguSubscribed) daegu.setChecked(true);
            if(gyungnamSubscribed) gyungnam.setChecked(true);
            if(woolsanSubscribed) woolsan.setChecked(true);
            if(busanSubscribed) busan.setChecked(true);
            if(jejuSubscribed) jeju.setChecked(true);
        }

        settingAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(settingAlarm.isChecked() == true) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("wholeSub", true);
                    editor.commit();
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
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("wholeSub", false);
                    editor.commit();
                    seoul.setChecked(false);
                    seoul.setEnabled(false);
                    gyungki.setChecked(false);
                    gyungki.setEnabled(false);
                    incheon.setChecked(false);
                    incheon.setEnabled(false);
                    gangwon.setChecked(false);
                    gangwon.setEnabled(false);
                    choongbook.setChecked(false);
                    choongbook.setEnabled(false);
                    daegeon.setChecked(false);
                    daegeon.setEnabled(false);
                    saejong.setChecked(false);
                    saejong.setEnabled(false);
                    choongnam.setChecked(false);
                    choongnam.setEnabled(false);
                    jeonbook.setChecked(false);
                    jeonbook.setEnabled(false);
                    jeonnam.setChecked(false);
                    jeonnam.setEnabled(false);
                    gwangju.setChecked(false);
                    gwangju.setEnabled(false);
                    gyungbook.setChecked(false);
                    gyungbook.setEnabled(false);
                    daegu.setChecked(false);
                    daegu.setEnabled(false);
                    gyungnam.setChecked(false);
                    gyungnam.setEnabled(false);
                    woolsan.setChecked(false);
                    woolsan.setEnabled(false);
                    busan.setChecked(false);
                    busan.setEnabled(false);
                    jeju.setChecked(false);
                    jeju.setEnabled(false);
                }
            }
        });

        seoul.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(seoul.isChecked() == true) {
                    saveSubscribed("seoul", seoul.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("seoul");
                }
                else {
                    saveSubscribed("seoul", seoul.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("seoul");
                }
            }
        });
        gyungki.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gyungki.isChecked() == true) {
                    saveSubscribed("gyungki", gyungki.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("gyungki");
                }
                else {
                    saveSubscribed("gyungki", gyungki.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("gyungki");
                }
            }
        });
        incheon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(incheon.isChecked() == true) {
                    saveSubscribed("incheon", incheon.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("incheon");
                }
                else {
                    saveSubscribed("incheon", incheon.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("incheon");
                }
            }
        });
        gangwon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gangwon.isChecked() == true) {
                    saveSubscribed("gangwon", gangwon.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("gangwon");
                }
                else {
                    saveSubscribed("gangwon", gangwon.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("gangwon");
                }
            }
        });
        choongbook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(choongbook.isChecked() == true) {
                    saveSubscribed("choongbook", choongbook.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("choongbook");
                }
                else {
                    saveSubscribed("choongbook", choongbook.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("choongbook");
                }
            }
        });
        daegeon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(daegeon.isChecked() == true) {
                    saveSubscribed("daegeon", daegeon.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("daegeon");
                }
                else {
                    saveSubscribed("daegeon", daegeon.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("daegeon");
                }
            }
        });
        saejong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(saejong.isChecked() == true) {
                    saveSubscribed("saejong", saejong.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("saejong");
                }
                else {
                    saveSubscribed("saejong", saejong.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("saejong");
                }
            }
        });
        choongnam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(choongnam.isChecked() == true) {
                    saveSubscribed("choongnam", choongnam.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("choongnam");
                }
                else {
                    saveSubscribed("choongnam", choongnam.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("choongnam");
                }
            }
        });
        jeonbook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jeonbook.isChecked() == true) {
                    saveSubscribed("jeonbook", jeonbook.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("jeonbook");
                }
                else {
                    saveSubscribed("jeonbook", jeonbook.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jeonbook");
                }
            }
        });
        jeonnam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jeonnam.isChecked() == true) {
                    saveSubscribed("jeonnam", jeonnam.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("jeonnam");
                }
                else {
                    saveSubscribed("jeonnam", jeonnam.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jeonnam");
                }
            }
        });
        gwangju.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gwangju.isChecked() == true) {
                    saveSubscribed("gwangju", gwangju.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("gwangju");
                }
                else {
                    saveSubscribed("gwangju", gwangju.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("gwangju");
                }
            }
        });
        gyungbook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gyungbook.isChecked() == true) {
                    saveSubscribed("gyungbook", gyungbook.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("gyungbook");
                }
                else {
                    saveSubscribed("gyungbook", gyungbook.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("gyungbook");
                }
            }
        });
        daegu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(daegu.isChecked() == true) {
                    saveSubscribed("daegu", daegu.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("daegu");
                }
                else {
                    saveSubscribed("daegu", daegu.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("daegu");
                }
            }
        });
        gyungnam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gyungnam.isChecked() == true) {
                    saveSubscribed("gyungnam", gyungnam.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("gyungnam");
                }
                else {
                    saveSubscribed("gyungnam", gyungnam.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("gyungnam");
                }
            }
        });
        woolsan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(woolsan.isChecked() == true) {
                    saveSubscribed("woolsan", woolsan.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("woolsan");
                }
                else {
                    saveSubscribed("woolsan", woolsan.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("woolsan");
                }
            }
        });
        busan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(busan.isChecked() == true) {
                    saveSubscribed("busan", busan.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("busan");
                }
                else {
                    saveSubscribed("busan", busan.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("busan");
                }
            }
        });
        jeju.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jeju.isChecked() == true) {
                    saveSubscribed("jeju", jeju.isChecked());
                    FirebaseMessaging.getInstance().subscribeToTopic("jeju");
                }
                else {
                    saveSubscribed("jeju", jeju.isChecked());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jeju");
                }
            }
        });
    }

    public void saveSubscribed(String cityName, Boolean chk) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(cityName + "Subscribed", chk);
        editor.commit();
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