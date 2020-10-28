package com.Surajtechstudio.smartguidebengali;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    ListAdapter listAdapter ;
    ListView LISTVIEW;

    ArrayList<String> ID_Array;
    ArrayList<String> NAME_Array;
    ArrayList<String> PHONE_NUMBER_Array;
    String SQLiteDataBaseQueryHolder ;
    ArrayList<String> ListViewClickItemArray = new ArrayList<String>();
    String TempHolder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        LISTVIEW = (ListView) findViewById(R.id.listView1);

        ID_Array = new ArrayList<String>();

        NAME_Array = new ArrayList<String>();

        PHONE_NUMBER_Array = new ArrayList<String>();

        sqLiteHelper = new SQLiteHelper(this);

        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(getApplicationContext(),ShowSingleRecordActivity.class);

                intent.putExtra("ListViewClickedItemValue", NAME_Array.get(position));
              //  Toast.makeText(getApplicationContext(),""+NAME_Array.get(position),Toast.LENGTH_LONG).show();

                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }

    private void ShowSQLiteDBdata() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_NAME+"", null);
        ID_Array.clear();
        NAME_Array.clear();
        PHONE_NUMBER_Array.clear();

        if (cursor.moveToFirst()) {
            do {

                ID_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID)));

                //Inserting Column ID into Array to Use at ListView Click Listener Method.
                ListViewClickItemArray.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID)));

                NAME_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Name)));

                PHONE_NUMBER_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2_PhoneNumber)));


            } while (cursor.moveToNext());
        }

       // listAdapter = new ListAdapter(DetailsActivity.this, ID_Array,NAME_Array,PHONE_NUMBER_Array);
        listAdapter=new ListAdapter(DetailsActivity.this,ID_Array,NAME_Array,PHONE_NUMBER_Array);

        LISTVIEW.setAdapter(listAdapter);

        cursor.close();
    }
    public class ListAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> ID;
        ArrayList<String> Name;
        ArrayList<String> PhoneNumber;


        public ListAdapter(
                Context context2,
                ArrayList<String> id,
                ArrayList<String> name,
                ArrayList<String> phone
        )
        {

            this.context = context2;
            this.ID = id;
            this.Name = name;
            this.PhoneNumber = phone;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return ID.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View child, ViewGroup parent) {

            ListAdapter.Holder holder;

            LayoutInflater layoutInflater;

            if (child == null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                child = layoutInflater.inflate(R.layout.items, null);

                holder = new ListAdapter.Holder();

                holder.ID_TextView = child.findViewById(R.id.textView2);
                holder.Name_TextView = (TextView) child.findViewById(R.id.textViewNAME);
               // holder.PhoneNumberTextView = (TextView) child.findViewById(R.id.textViewPHONE_NUMBER);

                child.setTag(holder);

            } else {

                holder = (ListAdapter.Holder) child.getTag();
            }
           // holder.ID_TextView.setText(ID.get(position));
            holder.Name_TextView.setText(Name.get(position));
          //  holder.PhoneNumberTextView.setText(PhoneNumber.get(position));
            holder.ID_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  Toast.makeText(getApplicationContext(),"id"+position,Toast.LENGTH_LONG).show();
                    sqLiteDatabase = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
                    SQLiteDataBaseQueryHolder = "DELETE FROM "+SQLiteHelper.TABLE_NAME+" WHERE id = "+ID.get(position)+"";
                    //  SQLiteDataBaseQueryHolder="DELETE FROM " +SQLiteHelper.TABLE_NAME+ " WHERE name = " + Name.get(position) + "'";
                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                    sqLiteDatabase.close();

                    // finish();
                    ShowSQLiteDBdata();
                }
            });

            return child;
        }

        public class Holder {

            TextView Name_TextView;
            TextView PhoneNumberTextView;
            ImageView ID_TextView;
        }

    }

}
