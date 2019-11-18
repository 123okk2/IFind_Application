package com.example.ifind.kidFunction;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;
import com.example.ifind.compareFunction.ComparePictureList;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KidInfoAdd extends AppCompatActivity {

    ImageButton img;
    Bitmap pic = null;
    EditText name;
    EditText age;
    EditText character;
    int sel;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid_info);

        img = (ImageButton) findViewById(R.id.addImg);
        name = (EditText) findViewById(R.id.nameInput);
        age = (EditText) findViewById(R.id.ageInput);
        character = (EditText) findViewById(R.id.character);
    }

    public void uploadImage(View v) {
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
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent;
        System.out.println("여기까지1");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                pic = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));

                if (pic != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    Matrix matrix;
                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix = new Matrix();
                            matrix.postRotate(180);
                            pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix = new Matrix();
                            matrix.postRotate(270);
                            pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(),
                                    matrix, true);
                            break;
                    }
                }

                img.setImageBitmap(pic);
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
                pic = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
                // Matrix 객체 생성
                Matrix matrix = new Matrix();
                // 회전 각도 셋팅
                matrix.postRotate(exifDegree);
                // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
                pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(), matrix, true);
                //여기까지
                img.setImageBitmap(pic);
            }
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }

    public void onClick(View v) {
        new JSONParse().execute();
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

    int errcode = 0;
    boolean chk=false;
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(KidInfoAdd.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String names = name.getText().toString();
            int ages = Integer.parseInt(age.getText().toString());
            String charac = character.getText().toString();

            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            String id = pref.getString("userID", "");


            if(pic==null || names.equals("") || age.getText().toString().equals("") || charac.equals("")) {
                errcode=1;
            }
            else {
                if (scm.registerMyKid(id, names, ages, pic, charac)) {
                    errcode=0;
                } else {
                    errcode=2;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            switch (errcode) {
                case 0:
                    Toast.makeText(getApplication(), "아이 등록 성공", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 1:
                    Toast.makeText(getApplication(), "누락된 메시지가 존재합니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                    break;
            }

            super.onPostExecute(J);
        }

    };
}
