package com.example.ifind.settingFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifind.userInfoFunction.InsertPwd;
import com.example.ifind.MainActivity;
import com.example.ifind.userInfoFunction.MyCommentsList;
import com.example.ifind.userInfoFunction.MyInfoUnregister;
import com.example.ifind.userInfoFunction.MyPostList;
import com.example.ifind.R;
import com.example.ifind.lossChildFunction.SelectChild;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.userInfoFunction.UserInfo;
import com.example.ifind.compareFunction.ComparePictureList;
import com.example.ifind.kidFunction.MyKidList;
import com.example.ifind.lossChildFunction.LongLossChildPost;

import org.json.JSONObject;

public class SettingMain extends AppCompatActivity {

    ImageView imv;
    TextView nameBox;
    //ServerConnectionManager scm = new ServerConnectionManager(SettingMain.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);

        /*
        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);

        UserInfo u = scm.getMemberInfo(pref.getString("userID", ""), pref.getString("userPwd", "") );
        String nickName;//pref.getString("userName", "");

        nameBox = (TextView) findViewById(R.id.userName);
        nameBox.setText(u.getName());

        imv = (ImageView) findViewById(R.id.imageView);
        imv.setImageBitmap(u.getPhoto());

        */
        //여기서 정보 불러오기

        getSupportActionBar().setTitle("IFind");

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    UserInfo u;
    @Override
    public void onResume() {
        super.onResume();
        new JSONParse().execute();
    }

    public void onClickBtn(View v) {
        Intent i;
        switch(v.getId()) {
            case R.id.kidInfo :
                i = new Intent(getApplicationContext(), MyKidList.class);
                startActivity(i);
                break;
            case R.id.setAlarm :
                i = new Intent(getApplicationContext(), SettingAlarm.class);
                startActivity(i);
                break;
            case R.id.miaSingo :
                i = new Intent(getApplicationContext(), SelectChild.class);
                //i = new Intent(getApplicationContext(), ShortLossChildPost.class);
                startActivity(i);
                break;
            case R.id.miaSingo2 :
                i = new Intent(getApplicationContext(), LongLossChildPost.class);
                //0 : 찾고있어요 1 : 찾아주세요
                i.putExtra("type", 0);
                startActivity(i);
                break;
            case R.id.miaSingo3 :
                i = new Intent(getApplicationContext(), LongLossChildPost.class);
                i.putExtra("type", 1);
                startActivity(i);
                break;
            case R.id.miaList :
                i = new Intent(getApplicationContext(), MyPostList.class);
                startActivity(i);
                break;
            case R.id.miaList2 :
                i = new Intent(getApplicationContext(), MyCommentsList.class);
                startActivity(i);
                break;
            case R.id.myInfo :
                i = new Intent(getApplicationContext(), InsertPwd.class);
                startActivity(i);
                break;
            case R.id.logOut :
                Toast.makeText(getApplicationContext(),
                        "로그아웃 되었습니다.",
                        Toast.LENGTH_SHORT).show();

                SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("id", "SYS_NONE");
                editor.putString("pwd", "");
                editor.commit();
                //System.exit(0);

                i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                //onDestroy();
                break;
            case R.id.unregister :
                i=new Intent(getApplicationContext(), MyInfoUnregister.class);
                startActivity(i);
                break;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu4, menu);

        getSupportActionBar().setTitle("IFind");
        getSupportActionBar().setSubtitle("Version 1.0");
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

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SettingMain.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);

            u = scm.getMemberInfo(pref.getString("userID", ""), pref.getString("userPwd", "") );

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            nameBox = (TextView) findViewById(R.id.userName);
            nameBox.setText(u.getName());

            imv = (ImageView) findViewById(R.id.imageView);
            imv.setImageBitmap(u.getPhoto());

            super.onPostExecute(J);
        }

    };
}
