package com.example.ifind.lossChildFunction;

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

public class ShortLossChildAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ShortLossChildInfo> listViewItemList = new ArrayList<ShortLossChildInfo>() ;

    // ListViewAdapter의 생성자
    public ShortLossChildAdapter() {}

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shortlosschilds, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.img) ;
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView age = (TextView) convertView.findViewById(R.id.age) ;
        TextView sight = (TextView) convertView.findViewById(R.id.sight) ;
        TextView lossdate= (TextView) convertView.findViewById(R.id.lossDate) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ShortLossChildInfo listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        img.setImageBitmap(listViewItem.getPic());
        //img.setImageResource(R.drawable.abc);
        name.setText(listViewItem.getName());
        age.setText(Integer.toString(listViewItem.getAge())+"세");
        sight.setText(listViewItem.getSight());
        lossdate.setText(listViewItem.getLossDate());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public ShortLossChildInfo getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String pid, Bitmap icon, String name, int age, String sight, String telNum, String character, String lossDate) {
        ShortLossChildInfo item = new ShortLossChildInfo(pid, icon, name, age, sight, telNum, character, lossDate);

        listViewItemList.add(item);
    }
}