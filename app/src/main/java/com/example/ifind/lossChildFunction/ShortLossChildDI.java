package com.example.ifind.lossChildFunction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.ifind.replyFunction.RewriteReply;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;
import com.example.ifind.compareFunction.comparePicture;
import com.example.ifind.replyFunction.replyAdapter;
import com.example.ifind.replyFunction.replyInfo;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShortLossChildDI extends AppCompatActivity {

    int functionType = 1;
    int errType = 0;

    ArrayList<replyInfo> ar;
    String kidID;
    String ids;
    String writerID;
    ImageView img;
    TextView nameBox, ageBox, lossDateBox, lossSightBox, characteresticBox;
    EditText replyBox;
    ListView ls;
    replyAdapter ra;
    ConstraintLayout only4Writer;

    ShortLossChildInfo slc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_loss_child_di);

        kidID = getIntent().getStringExtra("kidID");
        if (kidID == null) kidID = getIntent().getStringExtra("kidName");
        writerID = getIntent().getStringExtra("writerID");

    }

    public void imgClk(View v) {
        try {
            Intent intent = new Intent(this, showPictureDI.class);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] byteArray;
            slc.getPic().compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byteArray = stream.toByteArray();
            intent.putExtra("image", byteArray);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        replyBox = (EditText) findViewById(R.id.inputReply);

        img = (ImageView) findViewById(R.id.shortProfileImg);
        nameBox = (TextView) findViewById(R.id.nameBox);
        ageBox = (TextView) findViewById(R.id.ageBox);
        lossDateBox = (TextView) findViewById(R.id.lossDateBox);
        lossSightBox = (TextView) findViewById(R.id.lossSightBox);
        characteresticBox = (TextView) findViewById(R.id.characteresticBox);
        ls = (ListView) findViewById(R.id.replyLists);

        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        ids = pref.getString("userID", "");

        System.out.println(ids);

        functionType = 0;
        new JSONParse().execute();
    }

    public void callPhome(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + slc.getTelNum()));
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
        int id = item.getItemId();
        Intent i;
        if (id == R.id.action_camera) {
            i = new Intent(getApplicationContext(), comparePicture.class);
            i.putExtra("id", writerID);
            i.putExtra("name", kidID);
            i.putExtra("type", 0);
            startActivity(i);
            return true;
        } else if (id == R.id.action_setting) {
            i = new Intent(getApplicationContext(), SettingMain.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_rewrite) {
            if (slc.getPid().equals(ids)) {
                i = new Intent(getApplication(), ShortLossChildEdit.class);
                i.putExtra("name", slc.getName());
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(),
                        "권한이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_delete) {
            if (slc.getPid().equals(ids)) {
                functionType = 1;
                new JSONParse().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "권한이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }

    public void writeComment(View v) {
        functionType = 2;
        new JSONParse().execute();
    }

    boolean chk = false;
    String deleteReplyID, deleteReplyCID, deleteReplyName; //m.getID(), writerID, m.getCid(), m.getName())

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ShortLossChildDI.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            switch (functionType) {
                case 0:
                    errType = 10;
                    slc = scm.getShortLossChild(writerID, kidID);
                    if (slc.getName().equals("errorOccure")) {
                        errType = 0;
                    }
                    break;
                case 1:
                    if (scm.deleteLossChild(ids, slc.getName())) {
                        errType = 22;
                    } else {
                        errType = 23;
                    }
                    break;
                case 2:
                    String comment = replyBox.getText().toString();
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    if (scm.writeComment(ids, slc.getPid(), slc.getName(), comment, date, 0)) {
                        ar = scm.getComments(slc.getPid(), slc.getName(), 0, ids);
                        ra = new replyAdapter();
                        for (int i = 0; i < ar.size(); i++)
                            ra.addItem(ar.get(i).getID(), ar.get(i).getCid(), ar.get(i).getPic(), ar.get(i).getName(), ar.get(i).getReply(), ar.get(i).getDates(), ar.get(i).getUsrName());
                        errType = 4;
                    } else {
                        errType = 5;
                    }
                    break;
                case 3:
                    ar = new ArrayList<>();
                    ar = scm.getComments(slc.getPid(), slc.getName(), 0, ids);
                    errType = 100;

                    if (ar.size() != 0) {
                        if (ar.get(0).getName().equals("errorOccure")) {
                            errType = 1;
                        } else {
                            ra = new replyAdapter();
                            for (int i = 0; i < ar.size(); i++)
                                ra.addItem(ar.get(i).getID(), ar.get(i).getCid(), ar.get(i).getPic(), ar.get(i).getName(), ar.get(i).getReply(), ar.get(i).getDates(), ar.get(i).getUsrName());
                            errType = 2;
                        }
                    }
                    break;
                case 4:
                    if (scm.deleteComment(deleteReplyID, writerID, deleteReplyCID, deleteReplyName)) {
                        errType = 9;
                        //액티비티 갱신
                        ls = (ListView) findViewById(R.id.replyLists);
                        ArrayList<replyInfo> ar = scm.getComments(slc.getPid(), slc.getName(), 0, ids);
                        ra = new replyAdapter();
                        for (int i = 0; i < ar.size(); i++)
                            ra.addItem(ar.get(i).getID(), ar.get(i).getCid(), ar.get(i).getPic(), ar.get(i).getName(), ar.get(i).getReply(), ar.get(i).getDates(), ar.get(i).getUsrName());
                        //ls.setAdapter(ra);
                    } else {
                        errType = 11;
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();
            switch (errType) {
                case 0:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 10:
                    img.setImageBitmap(slc.getPic());
                    //img.setImageResource(R.drawable.abc);
                    nameBox.setText(slc.getName());
                    ageBox.setText(Integer.toString(slc.getAge()));
                    lossDateBox.setText(slc.getLossDate());
                    lossSightBox.setText(slc.getSight());
                    characteresticBox.setText(slc.getCharacter());

                    functionType = 3;
                    new JSONParse().execute();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    System.out.println(((replyInfo) ra.getItem(0)).getReply());
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShortLossChildDI.this);

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
                                                        functionType = 4;
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
                case 4:
                    Toast.makeText(getApplicationContext(),
                            "등록되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    replyBox.setText("");
                    ls = (ListView) findViewById(R.id.replyLists);
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
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShortLossChildDI.this);

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
                                                    functionType = 4;
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
                case 5:
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
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShortLossChildDI.this);

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
                                                    functionType = 4;
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
                case 22:
                    Toast.makeText(getApplicationContext(),
                            "삭제되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 23:
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

    }

    ;
}
