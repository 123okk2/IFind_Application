package com.example.ifind.lossChildFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ifind.MainActivity;
import com.example.ifind.R;
import com.example.ifind.compareFunction.ComparePictureList;
import com.example.ifind.serverFunction.ServerConnectionManager;
import com.example.ifind.settingFunction.SettingMain;

import org.json.JSONObject;

import java.util.ArrayList;

public class LongLossList extends Fragment {
    ListView ls;

    LongLossChildAdapter sca;
    LongLossChildAdapter sca4Search;
    String cityName;
    ArrayList<LongLossChildInfo> al;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view design from file: res/layout/fragment_page1.xml
        View view = inflater.inflate(R.layout.activity_long_loss_list, container, false);

        ls = (ListView) view.findViewById(R.id.listViews);
        setHasOptionsMenu(true);

        return view;
    }
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
                    Intent i = new Intent(getActivity().getApplicationContext(), LongLossChildDI.class);
                    i.putExtra("kidID", ((LongLossChildInfo) parent.getItemAtPosition(position)).getName());
                    i.putExtra("writerID", ((LongLossChildInfo) parent.getItemAtPosition(position)).getPid());
                    startActivity(i);
                }
            });
        }
        else {
            sca4Search = new LongLossChildAdapter();
            for(int i=0; i<al.size(); i++) {
                if (al.get(i).getName().indexOf(character) > -1) {
                    sca4Search.addItem(al.get(i).getPid(), al.get(i).getOldPic(), al.get(i).getPic(), al.get(i).getName(), al.get(i).getAge(), al.get(i).getSight(), al.get(i).getTelNum(), al.get(i).getCharacter(), al.get(i).getLossDate());
                }
            }
            ls.setAdapter(sca4Search);
            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity().getApplicationContext(), LongLossChildDI.class);
                    i.putExtra("kidID", ((LongLossChildInfo) parent.getItemAtPosition(position)).getName());
                    i.putExtra("writerID", ((LongLossChildInfo) parent.getItemAtPosition(position)).getPid());
                    startActivity(i);
                }
            });
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        al = new ArrayList<>();
        sca = new LongLossChildAdapter();
        ls.setAdapter(sca);
        cityName = getArguments().getString("type");
        new JSONParse().execute();
    }

    public void setChildList(String cityName) {

    }

    int errCode;
    boolean chk = false;

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
            if (!getActivity().isFinishing()) pDialog.show();

            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            if (cityName.equals("FindMe")) {
                al = scm.getLongLossChildList(1);
            } else if (cityName.equals("FindHim")) {
                al = scm.getLongLossChildList(0);
            }
            if (al.size() == 0) errCode = 1;
            else {
                if (al.get(0).getName().equals("errorOccure")) {
                    errCode = 2;
                } else {
                    errCode = 0;
                    for (int i = 0; i < al.size(); i++)
                        sca.addItem(al.get(i).getPid(), al.get(i).getOldPic(), al.get(i).getPic(), al.get(i).getName(), al.get(i).getAge(), al.get(i).getSight(), al.get(i).getTelNum(), al.get(i).getCharacter(), al.get(i).getLossDate());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject J) {
            pDialog.dismiss();
            switch (errCode) {
                case 0:
                    ls.setAdapter(sca);
                    ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(getActivity().getApplicationContext(), LongLossChildDI.class);
                            i.putExtra("kidID", ((LongLossChildInfo) parent.getItemAtPosition(position)).getName());
                            i.putExtra("writerID", ((LongLossChildInfo) parent.getItemAtPosition(position)).getPid());
                            startActivity(i);

                        }
                    });
                    break;
                case 1:
                    Toast.makeText(getActivity().getApplicationContext(), "게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity().getApplicationContext(), scm.getErrCode(), Toast.LENGTH_SHORT).show();
                    break;
            }

            super.onPostExecute(J);
        }

    }

    ;
}
