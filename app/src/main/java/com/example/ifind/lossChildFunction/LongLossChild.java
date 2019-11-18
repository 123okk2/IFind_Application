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

public class LongLossChild extends Fragment {

    FragmentManager fm;
    FragmentTransaction ft;

    Button findMe, findHim;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view design from file: res/layout/fragment_page1.xml
        View view = inflater.inflate(R.layout.activity_long_loss_child, container, false);

        //버튼 및 온클릭 선언
        findMe = (Button) view.findViewById(R.id.findMe);
        findHim = (Button) view.findViewById(R.id.findHim);

        setOnClickListners();

        //초기 프래그멘트 설정
        //fm=getActivity().getSupportFragmentManager();
        fm=getChildFragmentManager();
        ft=fm.beginTransaction();

        Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
        bundle.putString("type", "FindHim"); // key , value

        LongLossList sll = new LongLossList();
        sll.setArguments(getActivity().getIntent().getExtras());
        sll.setArguments(bundle);

        ft.replace(R.id.perTypesFrame, sll);
        ft.commit();

        return view;
    }

    public void setOnClickListners() {
        findMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("type", "FindMe"); // key , value

                LongLossList sll = new LongLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm = getChildFragmentManager();
                ft = fm.beginTransaction();
                findHim.setBackgroundResource(R.drawable.graybox);
                findMe.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perTypesFrame, sll);
                ft.commit();
            }
        });
        findHim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                bundle.putString("type", "FindHim"); // key , value

                LongLossList sll = new LongLossList();
                sll.setArguments(getActivity().getIntent().getExtras());
                sll.setArguments(bundle);

                fm=getChildFragmentManager();
                ft=fm.beginTransaction();
                findMe.setBackgroundResource(R.drawable.graybox);
                findHim.setBackgroundResource(R.drawable.whiteboxselected);
                ft.replace(R.id.perTypesFrame, sll);
                ft.commit();
            }
        });
    }
}