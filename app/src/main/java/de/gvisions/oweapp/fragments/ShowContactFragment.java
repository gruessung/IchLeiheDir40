package de.gvisions.oweapp.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.gvisions.oweapp.R;
import de.gvisions.oweapp.cards.SingleItemCard;
import de.gvisions.oweapp.services.DatabaseHelper;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

/**
 * Created by alexa on 09.01.2016.
 */
public class ShowContactFragment extends Fragment {

    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    RecyclerView oList;

    Uri uriConctact;
    String sName;

    static final String IMAGE_PATH = Environment.getExternalStorageDirectory() + "/ichleihedir/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_showcontact, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getView();

        database = new DatabaseHelper(v.getContext());
        connection = database.getWritableDatabase();

        SharedPreferences localSharedPreferences = v.getContext().getSharedPreferences("de.gvisions.oweapp", 0);

        uriConctact = Uri.parse(localSharedPreferences.getString("aktuelleURI", "nix uri"));
        sName = localSharedPreferences.getString("aktuellerName", "nix name");

        CardArrayRecyclerViewAdapter mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getActivity(), createList());

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) getActivity().findViewById(R.id.cardList2);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCardArrayAdapter);

    }

    public ArrayList<Card> createList() {
        ArrayList<Card> cards = new ArrayList<Card>();


        Log.d("URI ZUM SUCHEN", uriConctact.toString());
       final Cursor oResults = connection.rawQuery("SELECT contacturi, what, fromto, desc, type, deadline, foto FROM owe WHERE contacturi = '" + uriConctact.toString() + "'", null);
        while(oResults.moveToNext()) {
            Log.d("NEUE KARTE", oResults.getString(1));

            MaterialLargeImageCard card =
                    MaterialLargeImageCard.with(getActivity())
                            .setTextOverImage(oResults.getString(1))
                            .setTitle(oResults.getString(5))
                            .setSubTitle(oResults.getString(3))
                            .useDrawableId(R.drawable.ild_header)
                            .build();
            cards.add(card);

        }

        /*.useDrawableExternal(new MaterialLargeImageCard.DrawableExternal() {
                                @Override
                                public void setupInnerViewElements(ViewGroup parent, View viewImage) {

                                    Picasso.with(getActivity())
                                            .load(IMAGE_PATH+oResults.getString(6)) //URL/FILE
                                            .into( (ImageView) viewImage);
                                }
                            })*/


        return cards;
    }

}
