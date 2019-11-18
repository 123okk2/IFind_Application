package com.example.ifind.lossChildFunction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.util.ArrayList;

public class SelectChild extends AppCompatActivity {
    ListView kidList;
    ArrayList<String> kids;
    String selected = "";
    Button select;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("저기1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_child);
        System.out.println("저기2");
        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        id = pref.getString("userID", "");
        kidList = (ListView) findViewById(R.id.kidlv);
        select = (Button) findViewById(R.id.select);

        new JSONParse().execute();

        System.out.println("저기4");
    }

    public void onClick(View v) {
        SharedPreferences pref = getSharedPreferences("postMiaByKid", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Intent i;

        System.out.println(selected);
        switch(v.getId()) {
            case R.id.addNewKid2 :
                editor.putInt("type", 0);
                editor.commit();
                break;
            case R.id.select :
                editor.putInt("type", 1);
                editor.putString("kidName", selected);
                editor.commit();
                break;
        }
        i = new Intent(getApplicationContext(), ShortLossChildPost.class);
        startActivity(i);
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

    Context context = this;
    boolean chk=false;

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SelectChild.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            kids = scm.myKids(id);
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            select.setEnabled(false);
            kidList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, kids));
            kidList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //나중에 고쳐야됨
                    view.setBackgroundResource(R.color.smallTitle);
                    selected = kids.get(position);
                    select.setEnabled(true);
                }
            });
            super.onPostExecute(J);
        }

    };
}
