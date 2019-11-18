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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.lossChildFunction.LongLossChildDI;
import com.example.ifind.lossChildFunction.ShortLossChildDI;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyPostList extends AppCompatActivity {

    MyPostAdapter sca;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_singo_list);


        lv = (ListView) findViewById(R.id.listView);
    }

    String id;
    ArrayList<MyPost> al;
    @Override
    public void onResume() {
        super.onResume();


        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        id = pref.getString("userID", "");

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

    boolean chk=false;
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MyPostList.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            al = scm.posts(id);
            sca = new MyPostAdapter();
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            if(al.size() != 0) {
                if (al.get(0).getId().equals("errorOccure")) {
                    Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (al.size() == 0)
                    Toast.makeText(getApplication(), "작성글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                else {
                    for (int i = 0; i < al.size(); i++)
                        sca.addItem(al.get(i).getId(), al.get(i).getName(), al.get(i).getPic(), al.get(i).getMissingDate(), al.get(i).getType());

                    lv.setAdapter(sca);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (sca.getItem(position).getType() == 0) {
                                Intent i = new Intent(getApplicationContext(), ShortLossChildDI.class);
                                i.putExtra("writerID", sca.getItem(position).getId());
                                i.putExtra("kidName", sca.getItem(position).getName());
                                startActivity(i);
                            } else {
                                Intent i = new Intent(getApplicationContext(), LongLossChildDI.class);
                                i.putExtra("writerID", sca.getItem(position).getId());
                                i.putExtra("kidName", sca.getItem(position).getName());
                                startActivity(i);
                            }
                        }
                    });
                }
            }

            super.onPostExecute(J);
        }

    };
}
