package de.gvisions.oweapp.cards;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.CardView;


import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.List;

import de.gvisions.oweapp.MainActivity;
import de.gvisions.oweapp.R;
import de.gvisions.oweapp.fragments.MainFragment;
import de.gvisions.oweapp.fragments.ShowContactFragment;

/**
 * Created by alexander on 25.10.15.
 */
public class MainItemCard extends RecyclerView.Adapter<MainItemCard.ItemViewHolder> {

    private List<ItemInfo> itemList;
    private Context ctx;
    private MainActivity main;

    public MainItemCard(List<ItemInfo> list, Context ctx)
    {
        this.itemList = list;
        this.ctx = ctx;
    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i)
    {
        ItemInfo item = itemList.get(i);
        itemViewHolder.cardTitle.setText(item.sTitle);
        itemViewHolder.cardDescription.setText(item.sDescription);

        Log.d("URI", item.sContactUri);

        Log.d("FOTO NAME", item.sTitle);

        Bitmap contactUri = getPhotoUri(item.sContactUri);
        if (contactUri != null)
        {
            itemViewHolder.cardImage.setImageBitmap(contactUri);
        }

        itemViewHolder.cardView.setTag(R.string.tag1, item.sContactUri);
        itemViewHolder.cardView.setTag(R.string.tag2, item.sTitle);



    }

    public Bitmap getPhotoUri(String id) {
        int iId = id.lastIndexOf("/");
        id = id.substring(iId+1, id.length());
        if (id.startsWith("ph")) {
            return null;
        }
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Integer.parseInt(id));
        Log.d("FOTO URI", uri.toString());
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(this.ctx.getContentResolver(), uri);
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View viewItem = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_main_item, viewGroup, false);




        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences localSharedPreferences = v.getContext().getSharedPreferences("de.gvisions.oweapp", 0);
                localSharedPreferences.edit().putString("aktuellerName", v.getTag(R.string.tag2).toString()).commit();
                localSharedPreferences.edit().putString("aktuelleURI", v.getTag(R.string.tag1).toString()).commit();


                ((MainActivity)v.getContext()).changeFragment(new ShowContactFragment(), "Kontakt: "+v.getTag(R.string.tag2).toString(), new MainFragment(), true);
                ((MainActivity)v.getContext()).getCurrentSectionFragment().select();


            }
        });

        return new ItemViewHolder(viewItem);
    }






    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView cardImage;
        protected TextView cardTitle;
        protected TextView cardDescription;
        protected CardView cardView;

        public ItemViewHolder(View v)
        {
            super(v);
            cardImage = (ImageView) v.findViewById(R.id.thumbnail);
            cardTitle = (TextView) v.findViewById(R.id.title);
            cardDescription = (TextView) v.findViewById(R.id.description);
            cardView = (CardView) v.findViewById(R.id.card_view);
        }


    }




}


