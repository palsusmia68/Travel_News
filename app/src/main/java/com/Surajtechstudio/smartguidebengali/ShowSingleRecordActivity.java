package com.Surajtechstudio.smartguidebengali;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowSingleRecordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
String ti;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mtoggle;
    FloatingActionButton floatingActionButton;
    TextView t,t1,t2;
    ImageView iv,iv1;
    ViewPager viewPager;
    CirclePageIndicator indicator;
    private String URLstring = "http://www.smartguidess.in/wp-json/wp/v2/posts?page=1&per_page=100";
    ArrayList<String> mylist=new ArrayList<>();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    String ca;
    String share;
    String s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);
        Intent intent=getIntent();
        ti=intent.getStringExtra("ListViewClickedItemValue");
        drawer=findViewById(R.id.drawer_layout);
        floatingActionButton=findViewById(R.id.fab);
        t=findViewById(R.id.tvv);
        t1=findViewById(R.id.tv2);
        t2=findViewById(R.id.date);
        //iv=findViewById(R.id.iv);
        iv1=findViewById(R.id.iv1);
        iv1.setVisibility(View.INVISIBLE);
        viewPager=findViewById(R.id.pager);
        indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        drawer=findViewById(R.id.drawer_layout);
        mtoggle=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        assert navigationView != null;
        t.setText(ti);
        fetchingJSON();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,share);
                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });
    }

    private void fetchingJSON() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                String ti1 = (String) jsonObject.get("post_title");
                                if (ti1.equals(ti)) {
                                    String d = (String) jsonObject.get("post_date");
                                    t2.setText(d);
                                    String co = (String) jsonObject.get("post_content");
                                    co = co.replace("\r\n","<br />");
                                    co = co.replace(" ","&nbsp;");
                                    t1.setText(Html.fromHtml(co));
                                    ca = (String) jsonObject.get("post_category_name");
                                    if (ca.equals("Travel Stories"))
                                        share="http://www.smartguides.in/category/travel-stories/";
                                    else
                                    share="http://www.smartguidess.in/category/"+ca+"/";
                                    JSONArray jsonArray1 = jsonObject.optJSONArray("post_content_images");
                                    if (jsonArray1 != null) {
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONArray jsonArray2 = jsonArray1.optJSONArray(j);
                                            for (int z = 0; z < jsonArray2.length(); z++) {
                                                String fv = jsonArray2.optString(z);
                                                String imgRegex = "<[iI][mM][gG][^>]+[sS][rR][cC]\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
                                                Pattern p = Pattern.compile(imgRegex);
                                                Matcher m = p.matcher(fv);

                                                if (m.find()) {
                                                    String imgSrc = m.group(1);
                                                    // String imgSrc = String.valueOf(m);
                                                    mylist.add(imgSrc);
                                                    // Toast.makeText(getApplicationContext(),""+imgSrc,Toast.LENGTH_LONG).show();
                                                }

                                            }

                                        }
                                    }

                                }
                            }
                            viewPager.setAdapter(new SlidingImage_Adapter(ShowSingleRecordActivity.this, mylist));
                            indicator.setViewPager(viewPager);
                            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                            indicator.setRadius(5 * density);

                            NUM_PAGES = mylist.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    viewPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 1000, 1000);

                            // Pager listener over indicator
                            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    currentPage = position;

                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {

                                }
                            });

                            // Toast.makeText(getApplicationContext(),"hello "+mylist.get(3),Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        t.setText(error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home: drawer.closeDrawers();
                break;
            case R.id.rate:Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" +ShowSingleRecordActivity.this.getPackageName()));
                startActivity(intent);
                break;
            case R.id.feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "learnwithsuraj1@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                this.startActivity(Intent.createChooser(emailIntent, null));
                break;
            case R.id.other:
                Intent intentr = new Intent(Intent.ACTION_VIEW);
                intentr.setData(Uri.parse("https://play.google.com/store/apps/developer?id=SURAJ+TECH+STUDIO"));
                startActivity(intentr);
                break;
            case R.id.nav_share:ShareCompat.IntentBuilder.from(ShowSingleRecordActivity.this)
                    .setType("text/plain")
                    .setChooserTitle("Send App")
                    .setText("http://play.google.com/store/apps/details?id=" + ShowSingleRecordActivity.this.getPackageName())
                    .startChooser();
                break;
            case R.id.privacy:
                Intent inte = new Intent(ShowSingleRecordActivity.this, Policy.class);
                startActivity(inte);
                break;
            case R.id.fvrt:
                Intent inteh = new Intent(ShowSingleRecordActivity.this, DetailsActivity.class);
                startActivity(inteh);
                break;
            case R.id.update:
                Intent intentv = new Intent(Intent.ACTION_VIEW);
                intentv.setData(Uri.parse("http://play.google.com/store/apps/details?id=" +ShowSingleRecordActivity.this.getPackageName()));
                startActivity(intentv);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
