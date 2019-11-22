package com.example.ifind.userInfoFunction;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.compareFunction.ComparePictureList;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

public class login extends AppCompatActivity {

    //뒤로가기시 종료
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    Context context = login.this;

    String id;
    String pwd;

    EditText idInput;
    EditText pwdInput;

    CheckBox autoLogin;

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkPermiss();

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);

        idInput = (EditText) findViewById(R.id.idInput);
        pwdInput = (EditText) findViewById(R.id.pwdInput);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);
        loginBtn = (Button) findViewById(R.id.logInBtn);

        String id = pref.getString("id", "SYS_NONE");
        String pw = pref.getString("pwd", "");


        System.out.println(id.equals("SYS_NONE"));
        System.out.println("id:"+idInput.getText());

        if(id.equals("SYS_NONE")) {
            idInput.setText("");
            autoLogin.setChecked(false);
        }
        else{
            idInput.setText(id);

            autoLogin.setChecked(true);
        }

        pwdInput.setText(pw);

        System.out.println(idInput.getText() + " " + pwdInput.getText());

        if(!idInput.getText().equals("") && autoLogin.isChecked()) {
            autoLogin.setChecked(true);
            loginBtn.performClick();
        }

    }

    public void checkPermiss() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.INTERNET}, 0);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA}, 0);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CALL_PHONE}, 0);
        }

        /*
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_DENIED ||)
        */

    }

    public void onClickLogin(View v) {
        id = idInput.getText().toString();
        pwd = pwdInput.getText().toString();
        if(id.equals("")) {
            Toast.makeText(getApplication(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if (pwd.equals("")) {
            Toast.makeText(getApplication(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            new JSONParse().execute();
        }
    }

    public void onRegisterClicked(View v) {
        Intent i = new Intent(getApplicationContext(), RegisterClass.class);
        startActivity(i);
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(this);
            System.runFinalizersOnExit(true);
            System.exit(0);
            toast.cancel();
        }
    }

    private void showGuide() {
      Toast.makeText(getApplicationContext(),
                "뒤로 버튼을 한번 더 터치하시면 종료됩니다.",
                Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu4, menu);

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

    boolean chk;

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(login.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            chk = scm.logIn(id,pwd);

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            if (scm.logIn(id, pwd)) {
                if (autoLogin.isChecked()) {
                    SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id", id);
                    editor.putString("pwd", pwd);
                    editor.commit();
                } else {
                    SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id", "SYS_NONE");
                    editor.putString("pwd", "");
                    editor.commit();
                }

                SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userPwd", pwd);
                editor.putString("userID", id);
                editor.commit();

                finish();
            } else {
                Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(J);
        }

    };
}
