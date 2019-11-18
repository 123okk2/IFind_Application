package com.example.ifind.userInfoFunction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ifind.R;

import java.util.ArrayList;

public class MyCommentsAdapter extends BaseAdapter {

        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<MyComments> listViewItemList = new ArrayList<MyComments>() ;

        // ListViewAdapter의 생성자
        public MyCommentsAdapter() {}

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        public int getCount() {
            return listViewItemList.size() ;
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.mycommentlist, parent, false);
            }

            TextView reply = (TextView) convertView.findViewById(R.id.textBox);
            TextView dates = (TextView) convertView.findViewById(R.id.dateBox) ;

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            MyComments listViewItem = listViewItemList.get(position);

            // 아이템 내 각 위젯에 데이터 반영
            reply.setText(listViewItem.getComments());
            dates.setText(listViewItem.getDate());

            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        public long getItemId(int position) {
            return position ;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        public MyComments getItem(int position) {
            return listViewItemList.get(position) ;
        }

        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(String id, String name, String comment, String dates, int type) {
            MyComments item = new MyComments(id,name,comment,dates,type);
            listViewItemList.add(item);
        }
}
