package com.example.ifind.lossChildFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ifind.R;
import com.example.ifind.serverFunction.ServerConnectionManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class LongLossList extends Fragment {
    ListView ls;

    LongLossChildAdapter sca;
    String cityName;
    ArrayList<LongLossChildInfo> al;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view design from file: res/layout/fragment_page1.xml
        View view = inflater.inflate(R.layout.activity_long_loss_list, container, false);

        ls = (ListView) view.findViewById(R.id.listViews);

        return view;
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
