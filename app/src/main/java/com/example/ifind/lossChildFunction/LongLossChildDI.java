package com.example.ifind.lossChildFunction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.compareFunction.comparePicture;
import com.example.ifind.replyFunction.RewriteReply;
import com.example.ifind.replyFunction.replyAdapter;
import com.example.ifind.replyFunction.replyInfo;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LongLossChildDI extends AppCompatActivity {

    ImageView oldImg, curImg;
    TextView nameBox, ageBox, lossDateBox, lossSightBox, characteresticBox;
    EditText replyBox;
    ListView ls;
    ConstraintLayout only4Writer;
    replyAdapter ra;

    String ids;
    String writerID;
    LongLossChildInfo lc;
    String kidID;

    int functionType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_loss_child_di);

        kidID = getIntent().getStringExtra("kidID");
        if(kidID==null) kidID = getIntent().getStringExtra("kidName");
        writerID = getIntent().getStringExtra("writerID");
    }

    public void imgClk(View v) {
        Intent intent = new Intent(this, showPictureDI.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] byteArray;
        switch(v.getId()) {
            case R.id.longOldImg :
                lc.getOldPic().compress(Bitmap.CompressFormat.JPEG, 20, stream);
                byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);
                break;
            case R.id.longCurrentImg :
                lc.getPic().compress(Bitmap.CompressFormat.JPEG, 20, stream);
                byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);
                break;

        }
        startActivity(intent);
    }

    ArrayList<replyInfo> ar;
    @Override
    public void onResume() {
        super.onResume();

        replyBox = (EditText) findViewById(R.id.inputReply);

        oldImg = (ImageView) findViewById(R.id.longOldImg);
        curImg = (ImageView) findViewById(R.id.longCurrentImg);
        nameBox = (TextView) findViewById(R.id.nameBox);
        ageBox = (TextView) findViewById(R.id.ageBox);
        lossDateBox = (TextView) findViewById(R.id.lossDateBox);
        lossSightBox = (TextView) findViewById(R.id.lossSightBox);
        characteresticBox = (TextView)findViewById(R.id.characteresticBox);
        ls=(ListView) findViewById(R.id.replyLists);

        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        ids = pref.getString("userID", "");

        new JSONParse().execute();
    }


    public void callPhome(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData( Uri.parse("tel:"+lc.getTelNum()));
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu5, menu);

        getSupportActionBar().setTitle("IFind");

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        Intent i;
        if (id == R.id.action_camera) {
            i = new Intent(getApplicationContext(), comparePicture.class);
            i.putExtra("id", writerID);
            i.putExtra("name",kidID);
            i.putExtra("type",1);
            startActivity(i);
            return true;
        } else if (id == R.id.action_setting) {
            i=new Intent(getApplicationContext(), SettingMain.class);
            startActivity(i);
            return true;
        }
        else if(id == R.id.action_rewrite) {
            if(lc.getPid().equals(ids)) {
                i = new Intent(getApplication(),LongLossChildEdit.class);
                i.putExtra("name", lc.getName());
                startActivity(i);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "권한이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(id == R.id.action_delete) {
            if(lc.getPid().equals(ids)){
                functionType = 2;
                new JSONParse().execute();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "권한이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }
    public void writeComment(View v) {
        functionType = 3;
        new JSONParse().execute();

        //페이지 갱신
    }

    boolean chk=false;
    int errCode=0;
    String deleteReplyID, deleteReplyCID, deleteReplyName; //m.getID(), writerID, m.getCid(), m.getName())
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LongLossChildDI.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }
        ArrayList<replyInfo> ar;
        @Override
        protected JSONObject doInBackground(String... strings) {
            switch(functionType) {
                case 1:
                    //미아 정보 받아오기
                    errCode = 10;
                    lc = scm.getLongLossChild(writerID, kidID);
                    if(lc.getName().equals("errorOccure")) {
                        errCode = 0;
                    }
                    break;
                case 2:
                    if(scm.deleteLossChild(ids, lc.getName())) {
                        errCode = 5;
                    }
                    else {
                        errCode = 6;
                    }
                    break;
                case 3:
                    String comment = replyBox.getText().toString();
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    if(scm.writeComment(ids, lc.getPid(), lc.getName(), comment, date, 1)) {
                        ar = scm.getComments(lc.getPid(), lc.getName(), 1, ids);
                        ra = new replyAdapter();
                        for(int i=0;i<ar.size();i++) ra.addItem(ar.get(i).getID(), ar.get(i).getCid(), ar.get(i).getPic(), ar.get(i).getName(), ar.get(i).getReply(), ar.get(i).getDates(), ar.get(i).getUsrName());
                        errCode = 7;
                    }
                    else {
                        errCode = 8;
                    }
                    break;
                case 4:
                    ar = new ArrayList<>();
                    ar = scm.getComments(lc.getPid(), lc.getName(), 1, ids);
                    errCode = 100;
                    if(ar.size() != 0) {
                        if (ar.get(0).getName().equals("errorOccure")) {
                            errCode = 2;
                        } else {
                            ra = new replyAdapter();
                            for (int i = 0; i < ar.size(); i++)
                                ra.addItem(ar.get(i).getID(), ar.get(i).getCid(), ar.get(i).getPic(), ar.get(i).getName(), ar.get(i).getReply(), ar.get(i).getDates(), ar.get(i).getUsrName());
                            errCode = 3;
                        }
                    }
                    break;
                case 5:
                    if (scm.deleteComment(deleteReplyID, writerID, deleteReplyCID, deleteReplyCID)) {
                        errCode = 9;
                        ar = scm.getComments(lc.getPid(), lc.getName(), 1, ids);
                        ra = new replyAdapter();
                        for (int i = 0; i < ar.size(); i++)
                            ra.addItem(ar.get(i).getID(), ar.get(i).getCid(), ar.get(i).getPic(), ar.get(i).getName(), ar.get(i).getReply(), ar.get(i).getDates(), ar.get(i).getUsrName());
                } else {
                        errCode = 11;
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
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    ls.setAdapter(ra);
                    setListViewHeightBasedOnChildren(ls);

                    ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            final replyInfo m = ((replyInfo) parent.getItemAtPosition(position));
                            if (!ids.equals(m.getID())) {
                                Toast.makeText(getApplicationContext(),
                                        "권한이 없습니다.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LongLossChildDI.this);

                                // 제목셋팅
                                alertDialogBuilder.setTitle("작업을 선택하세요.");

                                // AlertDialog 셋팅
                                alertDialogBuilder
                                        .setMessage("")
                                        .setCancelable(true)
                                        .setPositiveButton("수정",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        // 댓글 수정
                                                        Intent i = new Intent(getApplicationContext(), RewriteReply.class);
                                                        //여기에 정보 넘기기
                                                        //String id, String pid, String cid, String name, String content, String date
                                                        i.putExtra("content", m.getReply());
                                                        i.putExtra("id", m.getID());
                                                        i.putExtra("pid", writerID);
                                                        i.putExtra("cid", m.getCid());
                                                        i.putExtra("name", m.getName());
                                                        startActivity(i);
                                                    }
                                                })
                                        .setNegativeButton("삭제",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        // 댓글 삭제
                                                        deleteReplyID = m.getID();
                                                        deleteReplyCID = m.getCid();
                                                        deleteReplyName = m.getName();
                                                        functionType = 5;
                                                        new JSONParse().execute();
                                                    }
                                                });

                                // 다이얼로그 생성
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // 다이얼로그 보여주기
                                alertDialog.show();
                            }
                            return false;
                        }
                    });
                    break;
                case 10:
                    System.out.println(lc.getPid());

                    oldImg.setImageBitmap(lc.getOldPic());
                    //oldImg.setImageResource(R.drawable.baby);
                    curImg.setImageBitmap(lc.getPic());
                    //curImg.setImageResource(R.drawable.abc);
                    nameBox.setText(lc.getName());
                    ageBox.setText(Integer.toString(lc.getAge()));
                    lossDateBox.setText(lc.getLossDate());
                    lossSightBox.setText(lc.getSight());
                    characteresticBox.setText(lc.getCharacter());

                    functionType = 4;
                    new JSONParse().execute();

                    //댓글
                    break;
                case 5:
                    errCode = 5;
                    Toast.makeText(getApplicationContext(),
                            "삭제되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(),
                            "등록되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    replyBox.setText("");
                    ls=(ListView) findViewById(R.id.replyLists);
                    ls.setAdapter(ra);
                    setListViewHeightBasedOnChildren(ls);ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        final replyInfo m = ((replyInfo) parent.getItemAtPosition(position));
                        if (!ids.equals(m.getID())) {
                            Toast.makeText(getApplicationContext(),
                                    "권한이 없습니다.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LongLossChildDI.this);

                            // 제목셋팅
                            alertDialogBuilder.setTitle("작업을 선택하세요.");

                            // AlertDialog 셋팅
                            alertDialogBuilder
                                    .setMessage("")
                                    .setCancelable(true)
                                    .setPositiveButton("수정",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // 댓글 수정
                                                    Intent i = new Intent(getApplicationContext(), RewriteReply.class);
                                                    //여기에 정보 넘기기
                                                    //String id, String pid, String cid, String name, String content, String date
                                                    i.putExtra("content", m.getReply());
                                                    i.putExtra("id", m.getID());
                                                    i.putExtra("pid", writerID);
                                                    i.putExtra("cid", m.getCid());
                                                    i.putExtra("name", m.getName());
                                                    startActivity(i);
                                                }
                                            })
                                    .setNegativeButton("삭제",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // 댓글 삭제
                                                    deleteReplyID = m.getID();
                                                    deleteReplyCID = m.getCid();
                                                    deleteReplyName = m.getName();
                                                    functionType = 5;
                                                    new JSONParse().execute();
                                                }
                                            });

                            // 다이얼로그 생성
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // 다이얼로그 보여주기
                            alertDialog.show();
                        }
                        return false;
                    }
                });
                    break;
                case 8:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    Toast.makeText(getApplicationContext(),
                            "삭제되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    //액티비티 갱신
                    ls.setAdapter(ra);
                    setListViewHeightBasedOnChildren(ls);ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        final replyInfo m = ((replyInfo) parent.getItemAtPosition(position));
                        if (!ids.equals(m.getID())) {
                            Toast.makeText(getApplicationContext(),
                                    "권한이 없습니다.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LongLossChildDI.this);

                            // 제목셋팅
                            alertDialogBuilder.setTitle("작업을 선택하세요.");

                            // AlertDialog 셋팅
                            alertDialogBuilder
                                    .setMessage("")
                                    .setCancelable(true)
                                    .setPositiveButton("수정",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // 댓글 수정
                                                    Intent i = new Intent(getApplicationContext(), RewriteReply.class);
                                                    //여기에 정보 넘기기
                                                    //String id, String pid, String cid, String name, String content, String date
                                                    i.putExtra("content", m.getReply());
                                                    i.putExtra("id", m.getID());
                                                    i.putExtra("pid", writerID);
                                                    i.putExtra("cid", m.getCid());
                                                    i.putExtra("name", m.getName());
                                                    startActivity(i);
                                                }
                                            })
                                    .setNegativeButton("삭제",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // 댓글 삭제
                                                    deleteReplyID = m.getID();
                                                    deleteReplyCID = m.getCid();
                                                    deleteReplyName = m.getName();
                                                    functionType = 5;
                                                    new JSONParse().execute();
                                                }
                                            });

                            // 다이얼로그 생성
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // 다이얼로그 보여주기
                            alertDialog.show();
                        }
                        return false;
                    }
                });
                    break;
                case 11:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            super.onPostExecute(J);
        }
        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                //listItem.measure(0, 0);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();

            params.height = totalHeight;
            listView.setLayoutParams(params);

            listView.requestLayout();
        }
    };
}
