package com.example.ifind.lossChildFunction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LongLossChildEdit extends AppCompatActivity {

    int errCode = 0;
    int functionType = 0;

    int type;
    String id;
    String name;
    Bitmap OldImg=null;
    Bitmap NewImg=null;
    private String mCurrentPhotoPath;

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

    Button dateB, timeB;

    ImageButton imb1, imb2;
    EditText nameBox;
    EditText ageBox;
    EditText characteristicBox;
    EditText missingDateBox;
    EditText missingTimeBox;
    Spinner bigCity, smallCity;
    EditText locateBox;

    LongLossChildInfo ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_loss_child_edit);

        type = getIntent().getIntExtra("type", 0);

        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        id = pref.getString("userID", "");

        imb1 = (ImageButton) findViewById(R.id.addOldImg);
        imb2 = (ImageButton) findViewById(R.id.addCurImg);
        nameBox = (EditText) findViewById(R.id.nameInput);
        ageBox = (EditText) findViewById(R.id.ageInput);
        dateB = (Button) findViewById(R.id.dateB);
        timeB = (Button) findViewById(R.id.timeB);
        characteristicBox = (EditText) findViewById(R.id.editText5);
        locateBox = (EditText)findViewById(R.id.addrInput);

        bigCity = (Spinner) findViewById(R.id.dos);
        smallCity = (Spinner) findViewById(R.id.si);

        functionType = 0;
        new JSONParse().execute();
    }

    protected Dialog onCreateDialog(int id) {
        Calendar oCalendar = Calendar.getInstance();
        switch(id){
            case 0 :
                DatePickerDialog dpd = new DatePickerDialog
                        (LongLossChildEdit.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear, int dayOfMonth) {
                                        dateB.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                oCalendar.get(Calendar.YEAR), (oCalendar.get(Calendar.MONTH) + 1), oCalendar.get(Calendar.DAY_OF_MONTH));
                return dpd;
            case 1 :
                TimePickerDialog tpd =
                        new TimePickerDialog(LongLossChildEdit.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {
                                        timeB.setText(hourOfDay + ":" + minute);
                                    }
                                }, // 값설정시 호출될 리스너 등록
                                oCalendar.get(Calendar.HOUR_OF_DAY), oCalendar.get(Calendar.MINUTE), false);
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return tpd;
        }


        return super.onCreateDialog(id);
    }


    int sel;

    public void uploadImage(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);

        // 제목셋팅
        alertDialogBuilder.setTitle("카메라 선택");

        switch (v.getId()) {
            case R.id.addOldImg :
                sel = 100;
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
                                        sel += 1;
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
                                        sel += 2;
                                        System.out.println(sel);
                                        dispatchTakePictureIntent();
                                    }
                                });
                break;
            case R.id.addCurImg :
                sel = 200;
                alertDialogBuilder
                        .setMessage("카메라를 선택해주세요")
                        .setCancelable(true)
                        .setPositiveButton("카메라에서 찍기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 프로그램을 종료한다
                                        System.out.println(sel);
                                        sel += 1;
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
                                        sel += 2;
                                        System.out.println(sel);
                                        dispatchTakePictureIntent();
                                    }
                                });
                break;

        }

        // 다이얼로그 생성
        AlertDialog alertDialog = alertDialogBuilder.create();

        // 다이얼로그 보여주기
        alertDialog.show();
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent;
        switch(sel) {
            case 101:
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
            case 102:
                takePictureIntent = new Intent(Intent.ACTION_PICK);
                takePictureIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(takePictureIntent, sel);
                break;
            case 201:
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
            case 202:
                takePictureIntent = new Intent(Intent.ACTION_PICK);
                takePictureIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(takePictureIntent, sel);
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 101 && resultCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                OldImg = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));

                if (OldImg != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    Matrix matrix;
                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            OldImg = Bitmap.createBitmap(OldImg, 0, 0, OldImg.getWidth(), OldImg.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix = new Matrix();
                            matrix.postRotate(180);
                            OldImg = Bitmap.createBitmap(OldImg, 0, 0, OldImg.getWidth(), OldImg.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix = new Matrix();
                            matrix.postRotate(270);
                            OldImg = Bitmap.createBitmap(OldImg, 0, 0, OldImg.getWidth(), OldImg.getHeight(),
                                    matrix, true);
                            break;
                    }
                }

                imb1.setImageBitmap(OldImg);
            } else if (requestCode == 102 && resultCode == RESULT_OK) {                //여기부터
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
                OldImg = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
                // Matrix 객체 생성
                Matrix matrix = new Matrix();
                // 회전 각도 셋팅
                matrix.postRotate(exifDegree);
                // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
                OldImg = Bitmap.createBitmap(OldImg, 0, 0, OldImg.getWidth(), OldImg.getHeight(), matrix, true);
                //여기까지
                imb1.setImageBitmap(OldImg);

            } else if (requestCode == 201 && requestCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                NewImg = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));

                if (NewImg != null) {
                    ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    Matrix matrix;
                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            NewImg = Bitmap.createBitmap(NewImg, 0, 0, NewImg.getWidth(), NewImg.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix = new Matrix();
                            matrix.postRotate(180);
                            NewImg = Bitmap.createBitmap(NewImg, 0, 0, NewImg.getWidth(), NewImg.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix = new Matrix();
                            matrix.postRotate(270);
                            NewImg = Bitmap.createBitmap(NewImg, 0, 0, NewImg.getWidth(), NewImg.getHeight(),
                                    matrix, true);
                            break;
                    }
                }

                imb2.setImageBitmap(NewImg);
            } else if (requestCode == 202 && resultCode == RESULT_OK) {               //여기부터
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
                NewImg = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
                // Matrix 객체 생성
                Matrix matrix = new Matrix();
                // 회전 각도 셋팅
                matrix.postRotate(exifDegree);
                // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
                NewImg = Bitmap.createBitmap(NewImg, 0, 0, NewImg.getWidth(), NewImg.getHeight(), matrix, true);
                //여기까지
                imb2.setImageBitmap(NewImg);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void onClickRegister(View v) {
        //id, type1, type2, photo1, photo2, missingDate, place, feature
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

    Context context = this;
    boolean chk=false;
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LongLossChildEdit.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            switch(functionType) {
                case 0:

                    name = getIntent().getStringExtra("name");
                    ll = scm.getLongLossChild(id, name);
                    errCode = 8;
                    break;
                case 2:
                    String names = nameBox.getText().toString();
                    int age = Integer.parseInt(ageBox.getText().toString());
                    int type1 = 1;
                    String missingDate = "";
                    missingDate += dateB.getText().toString() + " " + timeB.getText().toString();
                    String missingPlace = "";
                    missingPlace += bigCity.getSelectedItem() + " " + smallCity.getSelectedItem() + " " + locateBox.getText().toString();
                    String feature = characteristicBox.getText().toString();
                    if(OldImg==null && NewImg==null) {
                        errCode = 0;
                    }
                    else if (name.equals("") || ageBox.getText().toString().equals("") || missingDate == null || locateBox.getText().toString().equals("") || feature.equals("")) {
                        errCode = 1;
                    }
                    else {
                        if (OldImg==null) {
                            if (scm.editLossChild(id, name, names, 1, type, age, null, NewImg, missingDate, missingPlace, feature)) {
                                errCode = 2;
                            } else {
                                errCode = 3;
                            }
                        } else if (NewImg==null) {
                            if (scm.editLossChild(id, name, names, 1, type, age, OldImg, null, missingDate, missingPlace, feature)) {
                                errCode = 4;
                            } else {
                                errCode = 5;
                            }
                        } else {
                            if (scm.editLossChild(id, name, names, 1, type, age, OldImg, NewImg, missingDate, missingPlace, feature)) {
                                errCode = 6;
                            } else {
                                errCode = 7;
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
            switch(errCode) {
                case 0:
                    Toast.makeText(getApplication(), "최소 한 장의 사진을 등록해야 합니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplication(), "누락된 메시지가 존재합니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),
                            "수정이 완료되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(),
                            "수정이 완료되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(),
                            "수정이 완료되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(),
                            scm.getErrCode(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    imb1.setImageBitmap(ll.getOldPic());
                    imb2.setImageBitmap(ll.getPic());
                    nameBox.setText(ll.getName());
                    ageBox.setText(Integer.toString(ll.getAge()));
                    String[] date = ll.getLossDate().split(" ");
                    dateB.setText(date[0]);
                    timeB.setText(date[1]);
                    characteristicBox.setText(ll.getCharacter());
                    String[] location = ll.getSight().split(" ");
                    String loc = "";

                    for(int i=2;i<location.length; i++) loc += location[i];


                    bigCity.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, bigCities));

                    for(int i=0;i<bigCities.length;i++) {
                        if(location[0].equals(bigCities[i])) {
                            bigCity.setSelection(i);
                            smallCity.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, smallCities[i]));
                            for(int j=0;j<smallCities[i].length;j++) {
                                if(location[1].equals(smallCities[i][j])) {
                                    smallCity.setSelection(j);
                                    break;
                                }
                                break;
                            }
                        }
                    }

                    bigCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            smallCity.setAdapter(new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_dropdown_item, smallCities[position]));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    dateB.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            showDialog(0); // 날짜 설정 다이얼로그 띄우기
                        }
                    });
                    timeB.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            showDialog(1);
                        }
                    });
                    break;
            }
            super.onPostExecute(J);
        }

    };
}
