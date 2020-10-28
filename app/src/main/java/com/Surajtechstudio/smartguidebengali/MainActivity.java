package com.Surajtechstudio.smartguidebengali;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    ImageView share1;
    AdView mad;
    InterstitialAd inad;
    int s=0;
    int counter;
    Context context;
    private static final String URL_REGISTER_DEVICE = "http://www.smartguidess.in/pnfw/register";
    private String[] pageTitle = {"All","Health", "Job News", "Life Style","Technology","Education","Travel Stories"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBar actionBar = getSupportActionBar();
        share1=(ImageView)findViewById(R.id.share1);



        String intertial=getString(R.string.intertial);
        inad = new InterstitialAd(this);
        inad.setAdUnitId(intertial);
        inad.loadAd(new AdRequest.Builder().build());

       /* public void callad()
        {
            inad.loadAd(new AdRequest.Builder().build());
        }*/

       /* inad.setAdListener(new AdListener() {
                               @Override
                               public void onAdClosed() {
                                   if(counter%2==0)
                                   {
                                       inad.loadAd(new AdRequest.Builder().build());
                                   }
                               }
                           }
        );*/

        String appid=getString(R.string.appid);
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

        );



        share1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder.from(MainActivity.this)
                        .setType("text/plain")
                        .setChooserTitle("Send App")
                        .setText("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())
                        .startChooser();

            }
        });
        actionBar.hide();
        // setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 7; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        if (isInternetAvailable.isInternetAvailable(MainActivity.this))
        {
            //set viewpager adapter
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);

            //change Tab selection when swipe ViewPager
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            //change ViewPager page when tab selected
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    if (inad.isLoaded()) {
                        inad.show();
                    }
                    if (tab.getPosition() % 2 == 0) {
                        callad();
                    }
                }

                private void callad() {
                    inad.loadAd(new AdRequest.Builder().build());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }

            });
        }
        else {
            new AlertDialog.Builder(this).setIcon(R.drawable.error).setTitle("No Internet!")
                    .setMessage("Please connect to the Internet and reopen the app ")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();


                        }
                    }).show();
        }

       // new pushnotification(URL_REGISTER_DEVICE,context);
        //////////push notification////////////


       /* if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("MyNotification","MyNotification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });*/
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home: drawer.closeDrawers();
                break;
            case R.id.rate:Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" +MainActivity.this.getPackageName()));
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
            case R.id.nav_share:ShareCompat.IntentBuilder.from(MainActivity.this)
                    .setType("text/plain")
                    .setChooserTitle("Send App")
                    .setText("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())
                    .startChooser();
                break;
            case R.id.privacy:
                Intent inte = new Intent(MainActivity.this, Policy.class);
                startActivity(inte);
                break;
            case R.id.fvrt:
                Intent inteh = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(inteh);
                break;
            case R.id.update:
                Intent intentv = new Intent(Intent.ACTION_VIEW);
                intentv.setData(Uri.parse("http://play.google.com/store/apps/details?id=" +MainActivity.this.getPackageName()));
                startActivity(intentv);
                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.header_icon).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                }).setNeutralButton("Rate 5 star", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName()));
                startActivity(intent);

            }
        }).setNegativeButton("Cancel", null).show();
    }
}
