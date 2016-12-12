package de.gvisions.oweapp.cards;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.gvisions.oweapp.MainActivity;
import de.gvisions.oweapp.R;

/**
 * Created by alexander on 14.06.16.
 */
public class SingleItemCard extends RecyclerView.Adapter<SingleItemCard.ItemViewHolder> {



    static final String IMAGE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ichleihedir/";

    private List<SingleItem> itemList;
    private Context ctx;
    private MainActivity main;

    public SingleItemCard(List<SingleItem> list, Context ctx)
    {
        this.itemList = list;
        this.ctx = ctx;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View viewItem = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_one_item, viewGroup, false);


        return new ItemViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(SingleItemCard.ItemViewHolder itemViewHolder, int i) {

        SingleItem item = itemList.get(i);
        itemViewHolder.item = item;
        itemViewHolder.cardTitle.setText(item.sTitle);
        itemViewHolder.cardDescription.setText(item.sDescription);
        itemViewHolder.cardDatum.setText(item.sDatum);

        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
        String inputString1 = item.sDatum;

        Calendar c = Calendar.getInstance();

        String monat2 = "";
        int monat = c.get(Calendar.MONTH) +1;
        if (monat < 10) {
            monat2 = "0"+monat;
        } else {
            monat2 = String.valueOf(monat);
        }

        String inputString2 = c.get(Calendar.DAY_OF_MONTH)+"."+monat2+"."+c.get(Calendar.YEAR);

        Log.d("DATUM1", inputString1);
        Log.d("DATUM2", inputString2);

        itemViewHolder.imageRichtung.setImageResource(R.drawable.debug_step_out);


Log.d("TYPE", item.sType);

        if (item.sType.contains("0")) {
            itemViewHolder.imageRichtung.setImageResource(R.drawable.debug_step_into);

        }
        long iTage = 0;
        try {
            Date date2 = myFormat.parse(inputString1);
            Date date1 = myFormat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            iTage = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("TAGEDIGG", String.valueOf(iTage));
        Log.d("TEST", "test");

        if (iTage > 4) {
            itemViewHolder.cardColor.setBackgroundColor(Color.GREEN);
        } else if (iTage  < 4 && iTage > 1) {
            itemViewHolder.cardColor.setBackgroundColor(Color.YELLOW);
        } else if (iTage  < 2) {
            itemViewHolder.cardColor.setBackgroundColor(Color.RED);
        }

        if (item.sDatum.isEmpty()) {
            itemViewHolder.cardColor.setBackgroundColor(Color.WHITE);
        }


        Log.d("FOTODATEI", "file://"+IMAGE_PATH+item.sFoto);
        Picasso.with(ctx)
                .load("file://"+IMAGE_PATH+item.sFoto) //URL/FILE
                .error(R.drawable.avatar_2x)
                .placeholder(R.drawable.access_point)
                .into((ImageView) itemViewHolder.cardImage);


        if (item.sFoto == "0" || item.sFoto.isEmpty() || item.sFoto == IMAGE_PATH) {
            itemViewHolder.cardImage.setVisibility(View.GONE);
        }




    }

    @Override
    public int getItemCount() {
        return itemList.size();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView cardImage;
        protected TextView cardTitle;
        protected TextView cardDescription;
        protected TextView cardDatum;
        protected CardView cardView;
        protected LinearLayout cardColor;
        protected ImageView imageRichtung;

        protected SingleItem item;


        public ItemViewHolder(View v)
        {
            super(v);
            cardImage = (ImageView) v.findViewById(R.id.thumbnail);
            imageRichtung = (ImageView) v.findViewById(R.id.btnType);
            cardTitle = (TextView) v.findViewById(R.id.title);
            cardDescription = (TextView) v.findViewById(R.id.description);
            cardView = (CardView) v.findViewById(R.id.card_view);
            cardDatum = (TextView) v.findViewById(R.id.datum);
            cardColor = (LinearLayout) v.findViewById(R.id.cardColor);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = new ImageView(v.getContext());

                    Picasso.with(v.getContext())
                            .load("file://"+IMAGE_PATH+item.sFoto) //URL/FILE
                            .error(R.drawable.avatar_2x)
                            .placeholder(R.drawable.access_point)
                            .into(imageView);

                    if (item.sFoto.isEmpty() == false) {

                        AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                                .setView(imageView)
                                .setTitle(item.sTitle)
                                .setPositiveButton("Alles klar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();


                        dialog.show();
                    }

                }
            });

        }






    }

}

