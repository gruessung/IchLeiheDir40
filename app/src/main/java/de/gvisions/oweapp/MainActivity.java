package de.gvisions.oweapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.gvisions.oweapp.cards.ItemInfo;
import de.gvisions.oweapp.cards.MainItemCard;
import de.gvisions.oweapp.services.DatabaseHelper;


public class MainActivity extends ActionBarActivity {

    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    RecyclerView oList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        connection = database.getWritableDatabase();



        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), NewItem.class);
                startActivity(i);

            }
        });

        oList = (RecyclerView) findViewById(R.id.cardList);
        oList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        oList.setLayoutManager(llm);

        MainItemCard mainItemCard = new MainItemCard(createList(), this);
        oList.setAdapter(mainItemCard);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (oList != null) {
            oList.removeAllViews();
            oList.setAdapter(new MainItemCard(createList(), this));
        }
    }

    private List<ItemInfo> createList()
    {
        List<ItemInfo> oReturn = new ArrayList<ItemInfo>();

        Cursor result = connection.rawQuery("SELECT sum(case when type = '0' then 1 else 0 end) geliehen, sum(case when type = '1' then 1 else 0 end) verliehen, fromTo, contacturi, type FROM owe GROUP BY fromTo;", null);
        while (result.moveToNext())
        {
            int anzahlGeliehen = result.getInt(0);

            int anzahlVerliehen = result.getInt(1);

            ItemInfo item = new ItemInfo();
            item.sTitle = result.getString(2);
            item.sContactUri = result.getString(3);
            item.sDescription = "";
            if (anzahlGeliehen > 0)
            {
                item.sDescription = "Du schuldest ihm/ihr noch " + String.valueOf(anzahlGeliehen) + " Sachen.\n";
            }
            if (anzahlVerliehen > 0)
            {
                item.sDescription = item.sDescription + "Du hast ihm/ihr " + String.valueOf(anzahlVerliehen) + " Sachen geliehen.";
            }
            item.sContact = result.getString(3);

            oReturn.add(item);
        }



        return oReturn;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
