package com.example.ifind.kidFunction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;
import com.example.ifind.lossChildFunction.ShortLossChildDI;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyKidList extends AppCompatActivity {
    ListView kidList;
    ArrayList<String> kids;
    String selected = "";
    String id;
    Context context = this;

    int functionType = 0;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kid_list);

        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        id = pref.getString("userID", "");

        kidList = (ListView) findViewById(R.id.kidlv);


        alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
    }

    @Override
    protected void onResume() {
        super.onResume();

        functionType = 0;
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
            pDialog = new ProgressDialog(MyKidList.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);

            pDialog.show();
            super.onPreExecute();
        }

        int errCode;
        @Override
        protected JSONObject doInBackground(String... strings) {
            switch (functionType) {
                case 0:
                    kids = new ArrayList<String>();
                    kids.add("새 아이 추가");
                    ArrayList<String> kid = scm.myKids(id);
                    for(int i=0; i<kid.size(); i++) kids.add(kid.get(i));

                    if(kids.size()==0) {
                        errCode = 0;
                    }
                    else {
                        if (kids.get(0).equals("errorOccure")) {
                            errCode = 1;
                        } else {
                            errCode = 7;
                        }
                    }
                    break;
                case 1:
                    if(scm.deleteMyKid(id, selected)) {
                        errCode = 2;
                    } else {
                        errCode = 5;
                    }
                    kids = scm.myKids(id);

                    if(kids.size() !=0)  {
                        if (kids.get(0).equals("errorOccure")) {
                            errCode = 4;
                        } else {
                            errCode = 6;
                        }
                    }
                    break;
                case 2:
                    kids = scm.myKids(id);

                    if(kids.size()==0) {
                        errCode = 0;
                    }
                    else {
                        if (kids.get(0).equals("errorOccure")) {
                            errCode = 1;
                        } else {
                            errCode = 7;
                        }
                    }
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();
            switch(errCode) {
                case 0:
                    Toast.makeText(getApplication(), "등록된 자녀가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(getApplication(), "삭제 성공", Toast.LENGTH_SHORT).show();
                    functionType = 2;
                    new JSONParse().execute();
                    break;
                case 3:
                    Toast.makeText(getApplication(), "등록된 자녀가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 5:
                    Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    for (int j = 0; j < kids.size(); j++) System.out.println(kids.get(j));

                    kidList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, kids));

                    kidList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //나중에 고쳐야됨
                            view.setBackgroundResource(R.color.smallTitle);
                            selected = kids.get(position);
                        }
                    });
                    break;
                case 7:
                    for (int i = 0; i < kids.size(); i++) System.out.println(kids.get(i));

                    kidList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, kids));

                    kidList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //나중에 고쳐야됨
                            if(position == 0) {
                                Intent i=new Intent(getApplication(), KidInfoAdd.class);
                                startActivity(i);
                            }
                        }
                    });
                    kidList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            if(position > 0) {
                                selected = kids.get(position);
                                alertDialogBuilder.setTitle("작업 선택");
                                alertDialogBuilder
                                        .setMessage("")
                                        .setCancelable(true)
                                        .setPositiveButton("수정",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        // 프로그램을 종료한다
                                                        Intent i = new Intent(getApplication(), KidInfoEdit.class);
                                                        i.putExtra("kidName", selected);
                                                        System.out.println(selected);
                                                        startActivity(i);
                                                    }
                                                })
                                        .setNegativeButton("삭제",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        functionType = 1;
                                                        new JSONParse().execute();
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                            return false;
                        }
                    });
                    break;


            }
            super.onPostExecute(J);
        }

    };
}