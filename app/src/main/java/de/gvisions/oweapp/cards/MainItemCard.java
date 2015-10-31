package de.gvisions.oweapp.cards;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
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


import java.lang.reflect.Array;
import java.util.List;

import de.gvisions.oweapp.R;

/**
 * Created by alexander on 25.10.15.
 */
public class MainItemCard extends RecyclerView.Adapter<MainItemCard.ItemViewHolder> {

    private List<ItemInfo> itemList;
    private Context ctx;

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

        Uri contactUri = getPhotoUri(item.sContactUri);
        if (contactUri != null)
        {
            itemViewHolder.cardImage.setImageURI(contactUri);
        }

        itemViewHolder.cardView.setTag(item.sTitle);




    }

    public Uri getPhotoUri(String id) {
        try {

            int iId = id.lastIndexOf("/");
            id = id.substring(iId+1, id.length());


            Cursor cur = this.ctx.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                .parseLong(id));
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
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

                Toast.makeText(v.getContext(), "geklickt auf Karte "+ v.getTag(), Toast.LENGTH_SHORT).show();

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


