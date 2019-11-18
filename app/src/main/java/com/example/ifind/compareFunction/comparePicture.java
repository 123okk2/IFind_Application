package com.example.ifind.compareFunction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ifind.lossChildFunction.LongLossChildInfo;
import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;
import com.example.ifind.lossChildFunction.ShortLossChildInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class comparePicture extends AppCompatActivity {
    int functionType = 0;

    Bitmap imageBitmap;
    Bitmap kidBitmap;

    ImageView usrImg, kidImg;
    TextView txt;

    private int sel=0;
    private String mCurrentPhotoPath;

    String pid, name;

    ShortLossChildInfo slc;
    LongLossChildInfo llc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_picture);

        usrImg = (ImageView) findViewById(R.id.img1);
        kidImg = (ImageView) findViewById(R.id.img2);
        txt = (TextView) findViewById(R.id.result);

        name = getIntent().getStringExtra("name");
        pid = getIntent().getStringExtra("id");
        int chk = getIntent().getIntExtra("type",0);

        if(chk == 0) {
            int functionType = 0;
            new JSONParse().execute();
        }
        else {
            int functionType = 1;
            new JSONParse().execute();
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                imageBitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));

                if (imageBitmap != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    Matrix matrix;
                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix = new Matrix();
                            matrix.postRotate(180);
                            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix = new Matrix();
                            matrix.postRotate(270);
                            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(),
                                    matrix, true);
                            break;
                    }
                }

                usrImg.setImageBitmap(imageBitmap);

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
                imageBitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
                // Matrix 객체 생성
                Matrix matrix = new Matrix();
                // 회전 각도 셋팅
                matrix.postRotate(exifDegree);
                // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
                imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
                //여기까지
                usrImg.setImageBitmap(imageBitmap);
            }
            functionType = 2;
            new JSONParse().execute();
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
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

    String res;
    boolean chk=false;
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(comparePicture.this);
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
                    slc = scm.getShortLossChild(pid, name);
                    kidBitmap = slc.getPic();
                    break;
                case 1:
                    llc = scm.getLongLossChild(pid, name);
                    kidBitmap = llc.getPic();
                    break;
                case 2:
                    res = scm.comparePic(imageBitmap, kidBitmap, pid);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            switch (functionType) {
                case 0:
                    kidImg.setImageBitmap(slc.getPic());
                    break;
                case 1:
                    kidImg.setImageBitmap(llc.getPic());
                    break;
                case 2:
                    txt.setText("두 아이는 " + res + "% 일치합니다.");
                    break;
            }
            super.onPostExecute(J);
        }

    };
}
