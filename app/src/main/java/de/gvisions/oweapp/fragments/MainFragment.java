package de.gvisions.oweapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.gvisions.oweapp.MainActivity;
import de.gvisions.oweapp.R;
import de.gvisions.oweapp.cards.ItemInfo;
import de.gvisions.oweapp.cards.MainItemCard;
import de.gvisions.oweapp.services.DatabaseHelper;
import de.madcyph3r.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by alexa on 09.01.2016.
 */
public class MainFragment extends Fragment {

    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    RecyclerView oList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getView();

        database = new DatabaseHelper(v.getContext());
        connection = database.getWritableDatabase();

        oList = (RecyclerView) v.findViewById(R.id.cardList);
        oList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        oList.setLayoutManager(llm);

        MainItemCard mainItemCard = new MainItemCard(createList(), v.getContext());
        oList.setAdapter(mainItemCard);


    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (oList != null) {
            oList.removeAllViews();
            oList.setAdapter(new MainItemCard(createList(), getView().getContext()));
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




}
