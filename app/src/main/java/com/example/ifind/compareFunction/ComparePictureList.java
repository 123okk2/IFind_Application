package com.example.ifind.compareFunction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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

import com.example.ifind.lossChildFunction.LongLossChildDI;
import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;
import com.example.ifind.lossChildFunction.ShortLossChildDI;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComparePictureList extends AppCompatActivity {

    ImageView img2;
    Bitmap img;

    int sel;
    ListView ls;

    ArrayList<compareSearchInfo> cm;
    comparePictureAdapter cpa = new comparePictureAdapter();

    private String mCurrentPhotoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_picture_list);
        //ImageView img1 = (ImageView) findViewById(R.id.imageView2);

        ls=(ListView) findViewById(R.id.lview);

        img2 = (ImageView) findViewById(R.id.imageView3);

        //img1.setImageResource(R.drawable.abc);
        //img2.setImageResource(R.drawable.dsa);

        TextView tv = (TextView) findViewById(R.id.textView17);

        tv.setText("해당 아이와 일치하는 아이들입니다.");

        //여기부터
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);

        // 제목셋팅
        alertDialogBuilder.setTitle("카메라 선택");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("")
                .setCancelable(true)
                .setPositiveButton("카메라에서 찍기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 프로그램을 종료한다
                                System.out.println(sel);
                                sel = 1;
                                System.out.println(sel);
                                dispatchTakePictureIntent();
                            }
                        })
                .setNegativeButton("앨범에서 호출",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                System.out.println(sel);
                                sel = 2;
                                System.out.println(sel);
                                dispatchTakePictureIntent();
                            }
                        });

        // 다이얼로그 생성
        AlertDialog alertDialog = alertDialogBuilder.create();

        // 다이얼로그 보여주기
        alertDialog.show();
        //여기까지

    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent;
        switch(sel) {
            case 1:
                try {
                    // Create an image file name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );

                    // Save a file: path for use with ACTION_VIEW intents
                    mCurrentPhotoPath = image.getAbsolutePath();

                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.roopre.cameratutorial.fileprovider",
                            image);
                    takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, sel);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("여기까지2");
                takePictureIntent = new Intent(Intent.ACTION_PICK);
                takePictureIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(takePictureIntent, sel);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                img = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));

                if (img != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    Matrix matrix;
                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix = new Matrix();
                            matrix.postRotate(180);
                            img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix = new Matrix();
                            matrix.postRotate(270);
                            img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(),
                                    matrix, true);
                            break;
                    }
                }

                img2.setImageBitmap(img);
            } else if (requestCode == 2 && resultCode == RESULT_OK) {                //여기부터
                int column_index=0;
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), proj, null, null, null);
                if(cursor.moveToFirst()){
                    column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                }
                String imagePath = cursor.getString(column_index);
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = 0;
                if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) exifDegree = 90;
                else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) exifDegree = 180;
                else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) exifDegree = 270;
                img = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
                // Matrix 객체 생성
                Matrix matrix = new Matrix();
                // 회전 각도 셋팅
                matrix.postRotate(exifDegree);
                // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
                img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
                //여기까지
                img2.setImageBitmap(img);
            }
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        finally {
            new JSONParse().execute();
        }
    }

    public void setListView() {

        ls.setAdapter(cpa);
        //int type, String pid, String name, Bitmap pic, int percentage
        for (int i=0; i<cm.size(); i++) cpa.addItem(cm.get(i).getType(), cm.get(i).getPid(), cm.get(i).getName(), cm.get(i).getPic(), cm.get(i).getPercentage());
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((compareSearchInfo) parent.getItemAtPosition(position)).getType() == 0){
                    Intent i = new Intent(getApplicationContext(), ShortLossChildDI.class);
                    i.putExtra("kidID", ((compareSearchInfo) parent.getItemAtPosition(position)).getName());
                    i.putExtra("writerID", ((compareSearchInfo) parent.getItemAtPosition(position)).getPid());
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), LongLossChildDI.class);
                    i.putExtra("kidID", ((compareSearchInfo) parent.getItemAtPosition(position)).getName());
                    i.putExtra("writerID", ((compareSearchInfo) parent.getItemAtPosition(position)).getPid());
                    startActivity(i);
                }

            }
        });
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

    boolean chk=false;
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        int errCode = 1;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ComparePictureList.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            if (img==null) finish();

            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
            System.out.println(pref.getString("userID",""));
            cm = scm.searchPic(img, pref.getString("userID", ""));
            if(cm.size()==0) errCode = 0;
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();
            if(errCode == 0) Toast.makeText(getApplication(), "일치하는 미아가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            else setListView();
            super.onPostExecute(J);
        }

    };
}