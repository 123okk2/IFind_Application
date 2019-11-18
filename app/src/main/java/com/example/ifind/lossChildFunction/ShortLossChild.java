package com.example.ifind.lossChildFunction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ifind.R;

public class ShortLossChild extends Fragment {

    FragmentManager fm;
    FragmentTransaction ft;

    Button seoul, gyungki, incheon, gangwon, choongnam, daegeon, saJong, choongbuk, jeonbuk, jeonnam, gwangju, gyungbuk, daegu, gyungnam, woolsan, busan, jeju;

    Button[] btns;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view design from file: res/layout/fragment_page1.xml
        View view = inflater.inflate(R.layout.activity_short_loss_child, container, false);

        //버튼 및 온클릭 선언
        seoul = (Button) view.findViewById(R.id.seoulBtn);
        gyungki = (Button) view.findViewById(R.id.gyungKi);
        incheon = (Button) view.findViewById(R.id.incheon);
        gangwon = (Button) view.findViewById(R.id.gangWon);
        choongnam = (Button) view.findViewById(R.id.choongNam);
        daegeon = (Button) view.findViewById(R.id.daeGeon);
        saJong = (Button) view.findViewById(R.id.SaeJong);
        choongbuk = (Button) view.findViewById(R.id.choongBuk);
        jeonbuk = (Button) view.findViewById(R.id.jeonBuk);
        jeonnam = (Button) view.findViewById(R.id.jeonNam);
        gwangju = (Button) view.findViewById(R.id.gwangJu);
        gyungbuk = (Button) view.findViewById(R.id.gyungBuk);
        daegu = (Button) view.findViewById(R.id.daeGu);
        gyungnam = (Button) view.findViewById(R.id.gyungNam);
        woolsan = (Button) view.findViewById(R.id.woolSan);
        busan = (Button) view.findViewById(R.id.busan);
        jeju = (Button) view.findViewById(R.id.jeju);

        btns = new Button[] {seoul, gyungki, incheon, gangwon, choongnam, daegeon, saJong, choongbuk, jeonbuk, jeonnam, gwangju, gyungbuk, daegu, gyungnam, woolsan, busan, jeju};
        setOnClickListners();

        //초기 프래그멘트 설정
        //fm=getActivity().getSupportFragmentManager();
        fm=getChildFragmentManager();
        ft=fm.beginTransaction();

        Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
        bundle.putString("cityInfo", "seoul"); // key , value

        ShortLossList sll = new ShortLossList();
        sll.setArguments(getActivity().getIntent().getExtras());
        sll.setArguments(bundle);
        
        ft.replace(R.id.perCityFrame, sll);
        ft.commit();

        return view;
    }

    public void setOnClickListners() {
        seoul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                    bundle.putString("cityInfo", "seoul"); // key , value

                    ShortLossList sll = new ShortLossList();
                    sll.setArguments(getActivity().getIntent().getExtras());
                    sll.setArguments(bundle);

                    fm = getChildFragmentManager();
                    ft = fm.beginTransaction();
                    for (int i = 0; i < btns.length; i++) {
                        btns[i].setBackgroundResource(R.drawable.graybox);
                    }
                    seoul.setBackgroundResource(R.drawable.whiteboxselected);
                    ft.replace(R.id.perCityFrame, sll);
                    ft.commit();
                }
        });
        gyungki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "gyungki"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                gyungki.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        incheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "incheon"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                incheon.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        gangwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "gangwon"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                gangwon.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        choongnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "choongnam"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                choongnam.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        daegeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "daegeon"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                daegeon.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        saJong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "saejong"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                saJong.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        choongbuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "choongbuk"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                choongbuk.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        jeonbuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "jeonbuk"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                jeonbuk.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        jeonnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "jeonnam"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                jeonnam.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        gwangju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "gwangju"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                gwangju.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        gyungbuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "gyungbuk"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                gyungbuk.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        daegu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "daegu"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                daegu.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        gyungnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "gyungnam"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                gyungnam.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        woolsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "woolsan"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                woolsan.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        busan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "busan"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                busan.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
        jeju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("cityInfo", "jeju"); // key , value

                ShortLossList sll = new ShortLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                for(int i=0;i<btns.length;i++) {
                    btns[i].setBackgroundResource(R.drawable.graybox);
                }
                jeju.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perCityFrame, sll);
                ft.commit();
            }
        });
    }
}