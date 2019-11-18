package com.example.ifind.userInfoFunction;

import android.app.ProgressDialog;
import android.arch.core.util.Function;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.lossChildFunction.ShortLossChildDI;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyInfoEdit extends AppCompatActivity {

        boolean forCity=true;

        Bitmap bm = null;
        private String mCurrentPhotoPath;

        ImageButton pic;
        TextView idBox;
        EditText pwdBox, chkPwdBox, addressBox, nameBox, phoneBox;
        Spinner bigCity, smallCity;

        String[] bigCities = {
                "서울특별시","부산광역시","대구광역시","인천광역시","광주광역시","대전광역시","울산광역시","세종특별자치시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주특별자치도"
        };
        String[][] smallCities = {
                {"종로구","중구","용산구","성동구","광진구","동대문구","중랑구","성북구","강북구","도봉구","노원구","은평구","서대문구","마포구","양천구","강서구","구로구","금천구","영등포구","동작구","관악구","서초구","강남구","송파구","강동구"},
                {"중구","서구","동구","영도구","부산진구","동래구","남구","북구","강서구","해운대구","사하구","금정구","연제구","수영구","사상구","기장군"},
                {"중구","동구","서구","남구","북구","수성구","달서구","달성군"},
                {"중구","동구","미추홀구","연수구","남동구","부평구","계양구","서구","강화군","옹진군"},
                {"동구","서구","남구","북구","광산구"},
                {"서구","중구","동구","유성구","대덕구"},
                {"중구","남구","동구","북구", "울주군"},
                {},
                {"수원시", "성남시", "안양시", "안산시", "용인시", "광명시","평택시","과천시","오산시","시흥시","군포시","의왕시","하남시","이천시","안성시","김포시","화성시","광주시","여주시","부천시", "양평군", "덕양구","일산동구","일산서구", "의정부시","동두천시","구리시","남양주시","파주시","양주시","포천시", "연천군","가평군"},
                {"춘천시","원주시","강릉시","동해시","태백시","속초시","삼척시 홍천군","횡성군","영월군","평창군","정선군","철원군","화천군","양구군","인제군","고성군","양양군"},
                {"청주시", "충주시","제천시 보은군","옥천군","영동군","진천군","괴산군","음성군","단양군","증평군"},
                {"천안시", "공주시","보령시","아산시","서산시","논산시","계룡시","당진시 금산군","부여군","서천군","청양군","홍성군","예산군","태안군"},
                {"전주시", "군산시","익산시","정읍시","남원시","김제시 완주군","진안군","무주군","장수군","임실군","순창군","고창군","부안군"},
                {"목포시","여수시","순천시","나주시","광양시 담양군","곡성군","구례군","고흥군","보성군","화순군","장흥군","강진군","해남군","영암군","무안군","함평군","영광군","장성군","완도군","진도군","신안군"},
                {"포항시", "경주시","김천시","안동시","구미시","영주시","영천시","상주시","문경시","경산시 군위군","의성군","청송군","영양군","영덕군","청도군","고령군","성주군","칠곡군","예천군","봉화군","울진군","울릉군"},
                {"창원시", "진주시","통영시","사천시","김해시","밀양시","거제시","양산시 의령군","함안군","창녕군","고성군","남해군","하동군","산청군","함양군","거창군","합천군"},
                {"제주시","서귀포시"},
        };

        String pw;
        String id;
        Context context = this;

        UserInfo ui;
        int functionType = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_info_edit);

            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            id = pref.getString("userID", "");

            SharedPreferences pref2 = getSharedPreferences("pwdCheck", MODE_PRIVATE);
            SharedPreferences.Editor editor2 = pref2.edit();

            pw = pref2.getString("pw", "");

            bigCity = (Spinner) findViewById(R.id.bigCity);
            smallCity = (Spinner) findViewById(R.id.smallCity);
            idBox = (TextView) findViewById(R.id.idBox);
            pic = (ImageButton) findViewById(R.id.imageButton2);
            pwdBox = (EditText) findViewById(R.id.pwdBox);
            chkPwdBox = (EditText) findViewById(R.id.chkPwdBox);
            addressBox = (EditText) findViewById(R.id.addressBox);
            phoneBox = (EditText) findViewById(R.id.telBox);
            nameBox = (EditText) findViewById(R.id.nameBox);

            new JSONParse().execute();



        }

    int sel;
    public void uploadImage(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);

        // 제목셋팅
        alertDialogBuilder.setTitle("카메라 선택");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("카메라를 선택해주세요")
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
                bm = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));

                if (bm != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    Matrix matrix;
                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix = new Matrix();
                            matrix.postRotate(180);
                            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix = new Matrix();
                            matrix.postRotate(270);
                            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                                    matrix, true);
                            break;
                    }
                }

                pic.setImageBitmap(bm);
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
                bm = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
                // Matrix 객체 생성
                Matrix matrix = new Matrix();
                // 회전 각도 셋팅
                matrix.postRotate(exifDegree);
                // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                //여기까지
                pic.setImageBitmap(bm);
            }
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }


        public void registerBtnClick(View v) {

        functionType = 2;
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
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        ServerConnectionManager scm = new ServerConnectionManager();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MyInfoEdit.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        int errType=0;
        boolean chk=false;
        @Override
        protected JSONObject doInBackground(String... strings) {

            switch(functionType) {
                case 1:
                    ui = scm.getMemberInfo(id, pw);
                    break;
                case 2:
                    String id = idBox.getText().toString();
                    String pwd = pwdBox.getText().toString();
                    String chkPwd = chkPwdBox.getText().toString();
                    String address = bigCity.getSelectedItem().toString() + " " + smallCity.getSelectedItem().toString() + " " + addressBox.getText().toString();
                    String name = nameBox.getText().toString();
                    String phone = phoneBox.getText().toString();

                    if(id.equals("") || addressBox.getText().toString().equals("") || name.equals("") || phone.equals("")) {
                        errType = 1;
                    }
                    else {
                        if (pwd.equals("")) {
                            chk = scm.EditUserInfo(id, pw, "",  name, address, phone, bm);
                            if (!chk) {
                                errType = 2;
                            }
                        } else {
                            if (!pwd.equals(chkPwd))  {
                                errType = 3;
                            }
                        }
                    }
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            switch(functionType) {
                case 1:
                    if (ui.getId().equals("errorOccure")) {
                        Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    try {
                        bigCity.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, bigCities));

                        String[] add = ui.getAddr().split(" ");

                        //for(int i=0; i<3; i++) System.out.printf(add[i]);

                        for (int i = 0; i < bigCities.length; i++) {
                            if (add[0].equals(bigCities[i])) {
                                bigCity.setSelection(i);
                                smallCity.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, smallCities[i]));
                                for (int j = 0; j < smallCities[i].length; j++) {
                                    System.out.println(add[1] + " " + smallCities[i][j]);
                                    if (add[1].equals(smallCities[i][j])) {
                                        smallCity.setSelection(j);
                                        break;
                                    }
                                }
                            }
                        }

                        String addr = "";
                        for (int i = 2; i < add.length; i++) {
                            if (i == add.length - 1) addr += add[i];
                            else addr += add[i] + " ";
                        }

                        idBox.setText(ui.getId());
                        try {
                            if (ui.getPhoto() != null) pic.setImageBitmap(ui.getPhoto());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        addressBox.setText(addr);
                        nameBox.setText(ui.getName());
                        phoneBox.setText(ui.getPhone());
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplication(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                    bigCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (forCity) forCity = false;
                            else
                                smallCity.setAdapter(new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_dropdown_item, smallCities[position]));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
                case 2:
                    if(chk) {
                        if (chk) {
                            Toast.makeText(getApplication(), "수정 성공", Toast.LENGTH_SHORT).show();
                            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("userPwd", pw);
                            editor.putString("userID", id);
                            editor.commit();

                            SharedPreferences pref2 = getSharedPreferences("loginInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = pref2.edit();
                            editor2.putString("id", id);
                            editor2.putString("pwd", pw);
                            editor2.commit();

                            finish();
                        } else {
                            switch(errType) {
                                case 1:
                                    Toast.makeText(getApplication(), "누락된 정보가 존재합니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Toast.makeText(getApplication(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(getApplication(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                    break;
            }

            super.onPostExecute(J);
        }

    };
}
