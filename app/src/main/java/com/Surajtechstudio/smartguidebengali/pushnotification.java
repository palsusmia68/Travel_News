package com.Surajtechstudio.smartguidebengali;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class pushnotification  {
    private final Object Context;
    String u;
    private ProgressDialog progressDialog;
    String refreshedToken;
    public pushnotification(String urlRegisterDevice, Context context) {
       this.u= urlRegisterDevice;
        this.Context = context.getApplicationContext();
    }
   // progressDialog = new ProgressDialog(this);
     //   progressDialog.setMessage("Registering Device...");
       // progressDialog.show();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, u,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                       // Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //progressDialog.dismiss();
                    //textViewToken.setText(error.getMessage());
                    //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("os", "Android");
            params.put("token",refreshedToken);
            return params;
        }
    };
   // RequestQueue requestQueue = Volley.newRequestQueue(this);
     //   requestQueue.add(stringRequest);
}
