package com.Surajtechstudio.smartguidebengali;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment1 extends Fragment {
    private String URLstring = "http://www.smartguidess.in/wp-json/wp/v2/posts?page=1&per_page=100";
    private static ProgressDialog mProgressDialog;
    ArrayList<DataModel> dataModelArrayList;
    private RvAdapter rvAdapter;
    private RecyclerView recyclerView;
    String y="1";
    String flag;
    String share="http://www.smartguidess.in/category/health/";
    ProgressDialog pDialog;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view1);
        new GetContacts().execute();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(getActivity(),desActivity.class);
                        String h=dataModelArrayList.get(position).getName();
                        String pn=dataModelArrayList.get(position).getImgURL();
                        String ti=dataModelArrayList.get(position).getCountry();
                        flag=dataModelArrayList.get(position).getDate();
                        i.putExtra("USER_NAME",h);
                        i.putExtra("position",pn);
                        i.putExtra("title",ti);
                        i.putExtra("share",share);
                        i.putExtra("date",flag);
                        startActivity(i);
                    }

                    public void onLongItemClick(View view, int position) {
                        // do whatever

                        // Toast.makeText(getContext(),"you clicked-"+position,Toast.LENGTH_LONG).show();
                    }
                })
        );
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URLstring);

            // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray=new JSONArray(jsonStr);
                    dataModelArrayList = new ArrayList<>();
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        DataModel playerModel = new DataModel();
                        jsonObject=jsonArray.getJSONObject(i);
                        String x= (String) jsonObject.get("post_category_id");
                        if (x.equals(y)) {
                            playerModel.setName(jsonObject.getString("post_content"));
                            playerModel.setCountry(jsonObject.getString("post_title"));
                            playerModel.setCity(jsonObject.getString("post_name"));
                            playerModel.setImgURL(jsonObject.getString("post_cover_image"));
                            playerModel.setDate(jsonObject.getString("post_date"));

                            dataModelArrayList.add(playerModel);
                        }
                    }

                    // setupRecycler();
                } catch (final JSONException e) {
                    // Log.e(TAG, "Json parsing error: " + e.getMessage());
                   getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                // Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            rvAdapter = new RvAdapter(getContext(),dataModelArrayList);
            recyclerView.setAdapter(rvAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }

    }

}
