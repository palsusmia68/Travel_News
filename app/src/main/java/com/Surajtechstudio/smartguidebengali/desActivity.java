package com.Surajtechstudio.smartguidebengali;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.google.android.gms.ads.AdView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class desActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mtoggle;
    FloatingActionButton floatingActionButton;
    private String URLstring = "http://www.smartguidess.in/wp-json/wp/v2/posts?page=1&per_page=100";
    String user_name,pna,title;
    int pname;
    int j=-1;
    TextView t,t1,t2;
    ImageView iv,iv1;
    String word;
    String s2,date,iu;
    SQLiteDatabase sqLiteDatabaseObj;
    ArrayList<String> mylist=new ArrayList<>();
    ViewPager viewPager;
    CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    String SQLiteDataBaseQueryHolder;
    AdView mad;
    int anInt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);
        drawer=findViewById(R.id.drawer_layout);
        floatingActionButton=findViewById(R.id.fab);
        t=findViewById(R.id.tvv);
        t1=findViewById(R.id.tv2);
        t2=findViewById(R.id.date);
        //iv=findViewById(R.id.iv);
        iv1=findViewById(R.id.iv1);
        viewPager=findViewById(R.id.pager);
        indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        drawer=findViewById(R.id.drawer_layout);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("USER_NAME");
        s2=intent.getStringExtra("share");
        date=intent.getStringExtra("date");
        iu=intent.getStringExtra("position");
        mylist.add(iu);
       // Toast.makeText(getApplicationContext(),""+iu,Toast.LENGTH_LONG).show();
        t2.setText(date);



        /*String appid=getString(R.string.appid);
        mad=(AdView)findViewById(R.id.add);
        MobileAds.initialize(this,appid);
        mad=(AdView)findViewById(R.id.add);
        mad.setVisibility(View.GONE);
        AdRequest adr=new AdRequest.Builder().build();
        mad.loadAd(adr);
        mad.setAdListener(new AdListener(){

                              @Override
                              public void onAdLoaded() {
                                  mad.setVisibility(View.VISIBLE);
                              }

                              @Override
                              public void onAdFailedToLoad(int error) {
                                  mad.setVisibility(View.GONE);
                              }
                          }

        );*/


        try {
            word = URLEncoder.encode(user_name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        pna=intent.getStringExtra("position");
        title=intent.getStringExtra("title");
          t.setText(title);
        user_name = user_name.replace("\r\n","<br />");
        user_name = user_name.replace(" ","&nbsp;");
        t1.setText(Html.fromHtml(user_name));
       // Glide.with(getApplicationContext()).load(pna).into(iv);
        mtoggle=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        assert navigationView != null;
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDataBaseBuild();
                SQLiteTableBuild();
                InsertDataIntoSQLiteDatabase();
            }

        });

      floatingActionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent shareIntent = new Intent(Intent.ACTION_SEND);
              shareIntent.setType("text/plain");
              shareIntent.putExtra(Intent.EXTRA_TEXT,s2);
              shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
              view.getContext().startActivity(Intent.createChooser(shareIntent, "Share..."));
          }
      });
        fetchingJSON();
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
                                String ti = (String) jsonObject.get("post_title");
                                if (ti.equals(title)) {
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
                            viewPager.setAdapter(new SlidingImage_Adapter(desActivity.this, mylist));
                            indicator.setViewPager(viewPager);
                            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                            indicator.setRadius(5 * density);

                            NUM_PAGES = mylist.size();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            /*final Runnable Update = new Runnable() {
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
                            }, 3000, 3000);*/

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
                intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" +desActivity.this.getPackageName()));
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
            case R.id.nav_share:ShareCompat.IntentBuilder.from(desActivity.this)
                    .setType("text/plain")
                    .setChooserTitle("Send App")
                    .setText("http://play.google.com/store/apps/details?id=" + desActivity.this.getPackageName())
                    .startChooser();
                break;
            case R.id.privacy:
                Intent inte = new Intent(desActivity.this, Policy.class);
                startActivity(inte);
                break;
            case R.id.fvrt:
                Intent inteh = new Intent(desActivity.this, DetailsActivity.class);
                startActivity(inteh);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild(){

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS "+SQLiteHelper.TABLE_NAME+"("+SQLiteHelper.Table_Column_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+SQLiteHelper.Table_Column_1_Name+" VARCHAR, "+SQLiteHelper.Table_Column_2_PhoneNumber+" VARCHAR);");

    }
    public void InsertDataIntoSQLiteDatabase(){
        String query = "select * from " + SQLiteHelper.TABLE_NAME + " where name=\""+ title +"\"";
        Cursor c = sqLiteDatabaseObj.rawQuery(query, null);
        if(c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    String word = c.getString(c.getColumnIndex(SQLiteHelper.Table_Column_1_Name));
                    if (word.equals(title)) {
                        anInt = 1;
                        Toast.makeText(desActivity.this, "! Data is Already in favourite list", Toast.LENGTH_LONG).show();
                    }
                } while (c.moveToNext());
            }
        }
     if (anInt==0) {
    SQLiteDataBaseQueryHolder = "INSERT INTO " + SQLiteHelper.TABLE_NAME + " (name,phone_number) VALUES('" + title + "', '" + title + "');";

    sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

    sqLiteDatabaseObj.close();

    Toast.makeText(desActivity.this,"Add to Favourite List", Toast.LENGTH_LONG).show();

     }
    }

}
