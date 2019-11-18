package com.example.ifind.replyFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.compareFunction.ComparePictureList;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RewriteReply extends AppCompatActivity {

    String id;
    String pid; //신고글 작성자
    String cid; //제보글 식별자
    String name; //미아 이름

    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewrite_reply);

        t=(TextView) findViewById(R.id.editReply);

        t.setText(getIntent().getStringExtra("content"));
        id = getIntent().getStringExtra("id");
        pid = getIntent().getStringExtra("pid");
        cid = getIntent().getStringExtra("cid");
        name = getIntent().getStringExtra("name");
    }

    public void onClick(View v) {
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
    boolean chk=false;
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        int errType = 0;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(RewriteReply.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            if(scm.editComment(id, pid, cid, name, t.getText().toString(), date)) {
                errType = 1;
            }
            else {
                errType = 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            switch (errType) {
                case 1:
                    Toast.makeText(getApplicationContext(),
                            "수정되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),
                            "수정에 실패했습니다.",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

            super.onPostExecute(J);
        }

    };
}
