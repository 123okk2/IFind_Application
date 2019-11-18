package com.example.ifind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.ifind.compareFunction.ComparePictureList;
import com.example.ifind.lossChildFunction.LongLossChild;
import com.example.ifind.lossChildFunction.ShortLossChild;
import com.example.ifind.settingFunction.SettingMain;
import com.example.ifind.userInfoFunction.RegisterClass;
import com.example.ifind.userInfoFunction.login;

public class MainActivity extends AppCompatActivity {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    Button shortLossChild;
    Button longLossChild;

    LongLossChild llc;
    ShortLossChild slc;

    boolean state;
    boolean hideOrShow;

    View vew;
    Button b;

    FragmentManager fm;
    FragmentTransaction ft;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(getApplicationContext(), login.class);
        startActivity(i);

        b = (Button) findViewById(R.id.hideBtn);
        state = true;
        hideOrShow = true;

        //각 프레그멘트 선언
        llc = new LongLossChild();
        slc = new ShortLossChild();

        longLossChild = (Button) findViewById(R.id.longLoss);
        shortLossChild = (Button) findViewById(R.id.shortLoss);

        //초기 프래그멘트 설정
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.perTermFrame, slc);
        ft.commit();
    }
    //        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.example.ifind", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }


    /*
    @Override
    protected void onResume() {
        super.onResume();

        if(state) {

        }
        else {

        }
    }
    */


    public void selectTerm(View v) {
        //매번 초기화 안해주면 터짐
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();

        //누른 버튼에 따라 프래그멘트 변환
        switch (v.getId()) {
            case R.id.shortLoss :
                shortLossChild.setBackgroundResource(R.drawable.whitebox4main);
                shortLossChild.setTextColor(getResources().getColor(R.color.bigTitle));
                longLossChild.setBackgroundResource(R.drawable.blackbox);
                longLossChild.setTextColor(getResources().getColor(R.color.bigTitleSelected));
                ft.replace(R.id.perTermFrame, slc);
                hideOrShow = true;
                b.setText("▲");
                b.setBackgroundColor(getResources().getColor(R.color.smallTitle));
                b.setTextColor(getResources().getColor(R.color.bigTitle));
                state=true;
                break;
            case R.id.longLoss :
                shortLossChild.setBackgroundResource(R.drawable.blackbox);
                shortLossChild.setTextColor(getResources().getColor(R.color.bigTitleSelected));
                longLossChild.setBackgroundResource(R.drawable.whitebox4main);
                longLossChild.setTextColor(getResources().getColor(R.color.bigTitle));
                ft.replace(R.id.perTermFrame, llc);
                hideOrShow = true;
                b.setText("▲");
                b.setBackgroundColor(getResources().getColor(R.color.smallTitle));
                b.setTextColor(getResources().getColor(R.color.bigTitle));
                state=false;
                break;
        }

        //설정 실행
        ft.commit();
    }

    public void hideOrShowBtn(View v) {
        //목록 감추기 & 보이기

        if(state) {
            ScrollView ll = (ScrollView) findViewById(R.id.cityArray);
            if (hideOrShow) {
                b.setText("▼");
                b.setBackgroundColor(getResources().getColor(R.color.bigTitle));
                b.setTextColor(getResources().getColor(R.color.bigTitleSelected));
                ll.setVisibility(v.GONE);
                hideOrShow = false;
            } else {
                b.setText("▲");
                b.setBackgroundColor(getResources().getColor(R.color.smallTitle));
                b.setTextColor(getResources().getColor(R.color.bigTitle));
                ll.setVisibility(v.VISIBLE);
                hideOrShow = true;
            }
        }

        else {
            LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayouts);
            if (hideOrShow) {
                b.setText("▼");
                b.setBackgroundColor(getResources().getColor(R.color.bigTitle));
                b.setTextColor(getResources().getColor(R.color.bigTitleSelected));
                ll.setVisibility(v.GONE);
                hideOrShow = false;
            } else {
                b.setText("▲");
                b.setBackgroundColor(getResources().getColor(R.color.smallTitle));
                b.setTextColor(getResources().getColor(R.color.bigTitle));
                ll.setVisibility(v.VISIBLE);
                hideOrShow = true;
            }
        }
    }

    //액션바
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

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

    public void onRegisterClicked(View v) {
        Intent i = new Intent(getApplicationContext(), RegisterClass.class);
        startActivity(i);
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(this);
            System.runFinalizersOnExit(true);
            System.exit(0);
            toast.cancel();
        }
    }

    private void showGuide() {
        Toast.makeText(getApplicationContext(),
                "뒤로 버튼을 한번 더 터치하시면 종료됩니다.",
                Toast.LENGTH_SHORT).show();
    }

}
