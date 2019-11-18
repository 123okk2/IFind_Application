package com.example.ifind.userInfoFunction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.compareFunction.ComparePictureList;
import com.example.ifind.settingFunction.SettingMain;
import com.example.ifind.userInfoFunction.MyInfoEdit;

public class InsertPwd extends AppCompatActivity {

    EditText pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pwd);

        pwd = (EditText) findViewById(R.id.chkPwd);
    }

    public void clickBtn(View v) {
        String passwd = pwd.getText().toString();

        if(passwd.equals("")) {
            Toast.makeText(getApplication(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences pref = getSharedPreferences("pwdCheck", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("pw", passwd);
            editor.commit();

            Intent i = new Intent(getApplicationContext(), MyInfoEdit.class);
            startActivity(i);
        }
    }

    //액션바
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
