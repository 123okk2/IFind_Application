package com.example.ifind.compareFunction;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ifind.R;

import java.util.ArrayList;

public class comparePictureAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<compareSearchInfo> listViewItemList = new ArrayList<>() ;

    // ListViewAdapter의 생성자
    public comparePictureAdapter() {}

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
            //여기부터 수정
            convertView = inflater.inflate(R.layout.searchpic, parent, false);
        }

        //img name age textView12
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView res = (TextView) convertView.findViewById(R.id.textView12);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        compareSearchInfo listViewItem = listViewItemList.get(position);

        img.setImageBitmap(listViewItem.getPic());
        name.setText(listViewItem.getName());
        res.setText("일치율 : " + listViewItem.getPercentage());

        // 아이템 내 각 위젯에 데이터 반영
        //img.setImageBitmap(listViewItem.getPic());
        //oldPic.setImageBitmap(listViewItem.getOldPic());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    public compareSearchInfo getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int type, String pid, String name, Bitmap pic, String percentage) {
        compareSearchInfo item = new compareSearchInfo(type, pid, name, pic, percentage);

        listViewItemList.add(item);
    }
}
