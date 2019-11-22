package com.example.ifind.lossChildFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifind.MainActivity;
import com.example.ifind.R;
import com.example.ifind.compareFunction.ComparePictureList;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShortLossList extends Fragment {

    String cityName;
    ArrayList<ShortLossChildInfo> al;
    ListView ls;
    int errType = 2;

    String[] cities = {"seoul", "gyungki", "incheon", "gangwon", "choongnam", "daegeon", "choongbuk", "jeonbuk", "jeonnam", "gwangju", "gyungbuk", "daegu", "gyungnam", "woolsan", "busan", "jeju", "saejong"};

    ShortLossChildAdapter sca;
    ShortLossChildAdapter sca4Search;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view design from file: res/layout/fragment_page1.xml
        View view = inflater.inflate(R.layout.activity_short_loss_list, container, false);

        ls=(ListView) view.findViewById(R.id.seoulListView);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);


        ((MainActivity) getActivity()).getSupportActionBar().setTitle("IFind");

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)mSearch.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //query가 입력값
                System.out.println("dsadasdadasdasd");
                System.out.println(query);
                searchKids(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchKids("");
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_setting) {
            i=new Intent(getActivity().getApplicationContext(), SettingMain.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_camera) {
            i=new Intent(getActivity().getApplicationContext(), ComparePictureList.class);
            startActivity(i);
            return true;
        }
        return false;
    }

    public void searchKids(String character) {
        System.out.println("asdasdasdasd");
        System.out.println(character);
        if(character.equals("")) {
            ls.setAdapter(sca);
            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity().getApplicationContext(), ShortLossChildDI.class);
                    i.putExtra("kidID", ((ShortLossChildInfo) parent.getItemAtPosition(position)).getName());
                    i.putExtra("writerID", ((ShortLossChildInfo) parent.getItemAtPosition(position)).getPid());
                    startActivity(i);
                }
            });
        }
        else {
            sca4Search = new ShortLossChildAdapter();
            for(int i=0; i<al.size(); i++) {
                if (al.get(i).getName().indexOf(character) > -1) {
                    sca4Search.addItem(al.get(i).getPid(), al.get(i).getPic(), al.get(i).getName(), al.get(i).getAge(), al.get(i).getSight(), al.get(i).getTelNum(), al.get(i).getCharacter(), al.get(i).getLossDate());
                }
            }
            ls.setAdapter(sca4Search);
            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity().getApplicationContext(), ShortLossChildDI.class);
                    i.putExtra("kidID", ((ShortLossChildInfo) parent.getItemAtPosition(position)).getName());
                    i.putExtra("writerID", ((ShortLossChildInfo) parent.getItemAtPosition(position)).getPid());
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        cityName = getArguments().getString("cityInfo");
        new JSONParse().execute();
    }

    boolean chk=false;
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        ServerConnectionManager scm = new ServerConnectionManager();
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(getActivity());
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("잠시만 기다려주세요.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if(!getActivity().isFinishing()) pDialog.show();
            sca = new ShortLossChildAdapter();
            ls.setAdapter(sca);
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            if(cityName.equals(cities[0])) { //서울
                al = scm.getLossChildList("서울특별시");
            }
            else if(cityName.equals(cities[1])) { //경기
                al = scm.getLossChildList("경기도");
            }
            else if(cityName.equals(cities[2])) { //인천
                al = scm.getLossChildList("인천광역시");
            }
            else if(cityName.equals(cities[3])) {
                al = scm.getLossChildList("강원도");
            }
            else if(cityName.equals(cities[4])) {
                al = scm.getLossChildList("충청남도");
            }
            else if(cityName.equals(cities[5])) {
                al = scm.getLossChildList("대전광역시");
            }
            else if(cityName.equals(cities[6])) {
                al = scm.getLossChildList("충청북도");
            }
            else if(cityName.equals(cities[7])) {
                al = scm.getLossChildList("전라북도");
            }
            else if(cityName.equals(cities[8])) {
                al = scm.getLossChildList("전라남도");
            }
            else if(cityName.equals(cities[9])) {
                al = scm.getLossChildList("광주광역시");
            }
            else if(cityName.equals(cities[10])) {
                al = scm.getLossChildList("경상북도");
            }
            else if(cityName.equals(cities[11])) {
                al = scm.getLossChildList("대구광역시");
            }
            else if(cityName.equals(cities[12])) {
                al = scm.getLossChildList("경상남도");
            }
            else if(cityName.equals(cities[13])) {
                al = scm.getLossChildList("울산광역시");
            }
            else if(cityName.equals(cities[14])) {
                al = scm.getLossChildList("부산광역시");
            }
            else if(cityName.equals(cities[15])) {
                al = scm.getLossChildList("제주특별자치도");
            }
            else {
                al = scm.getLossChildList("세종특별자치시");
            }
            errType = 2;

            if (al.size() == 0) errType = 0;
            else {
                if (al.get(0).getName().equals("errorOccure")) errType = 1;
                else {
                    for (int i = 0; i < al.size(); i++)
                        sca.addItem(al.get(i).getPid(), al.get(i).getPic(), al.get(i).getName(), al.get(i).getAge(), al.get(i).getSight(), al.get(i).getTelNum(), al.get(i).getCharacter(), al.get(i).getLossDate());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();

            switch (errType) {
                case 0:
                    Toast.makeText(getActivity().getApplicationContext(), "게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getActivity().getApplicationContext(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    ls.setAdapter(sca);
                    ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(getActivity().getApplicationContext(), ShortLossChildDI.class);
                            i.putExtra("kidID", ((ShortLossChildInfo) parent.getItemAtPosition(position)).getName());
                            i.putExtra("writerID", ((ShortLossChildInfo) parent.getItemAtPosition(position)).getPid());
                            startActivity(i);
                        }
                    });
                    break;

            }


            super.onPostExecute(J);
        }

    };
}
