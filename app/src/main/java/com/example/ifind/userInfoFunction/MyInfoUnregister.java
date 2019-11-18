package com.example.ifind.userInfoFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ifind.MainActivity;
import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.lossChildFunction.ShortLossChildDI;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

public class MyInfoUnregister extends AppCompatActivity {

    EditText pwdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_unregister);

        pwdInput = (EditText) findViewById(R.id.chkPwd);


    }

    public void onClickUnregisterBtn(View v) {
        new JSONParse().execute();
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
            i = new Intent(getApplicationContext(), ShortLossChildDI.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_setting) {
            i=new Intent(getApplicationContext(), SettingMain.class);
            startActivity(i);
            return true;
        }
        return false;
    }
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        ServerConnectionManager scm = new ServerConnectionManager();
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MyInfoUnregister.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        boolean chk;
        @Override
        protected JSONObject doInBackground(String... strings) {
            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String id = pref.getString("userID", "");
            chk = scm.unregister(id, pwdInput.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();
            if(chk) {
                Toast.makeText(getApplication(), "회원 탈퇴 성공", Toast.LENGTH_SHORT).show();
                SharedPreferences pref2 = getSharedPreferences("loginInfo", MODE_PRIVATE);
                pref2.getString("id","SYS_NONE");
                pref2.getString("pwd","");

                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                //onDestroy();
            }
            else {
                Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(J);
        }

    };
}
