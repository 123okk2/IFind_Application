package com.example.ifind.replyFunction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ifind.R;

import java.util.ArrayList;

public class replyAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<replyInfo> listViewItemList = new ArrayList<replyInfo>() ;

    // ListViewAdapter의 생성자
    public replyAdapter() {}

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
            convertView = inflater.inflate(R.layout.replylist, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.imgReply) ;
        TextView reply= (TextView) convertView.findViewById(R.id.replyBox) ;
        TextView replyName = (TextView) convertView.findViewById(R.id.replyName);
        TextView dateBox = (TextView) convertView.findViewById(R.id.dateBox);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        replyInfo listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        img.setImageBitmap(listViewItem.getPic());
        reply.setText(listViewItem.getReply());
        replyName.setText(listViewItem.getUsrName());
        dateBox.setText(listViewItem.getDates());


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String id, String cid, Bitmap icon, String name, String reply, String dates, String usrName) {
        replyInfo item = new replyInfo(id, cid, icon, name, reply, dates, usrName);

        listViewItemList.add(item);
    }

}