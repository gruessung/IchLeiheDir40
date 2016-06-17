package de.gvisions.oweapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.ZoomStyle;



import java.io.File;
import java.util.Calendar;
import java.util.EmptyStackException;

import de.gvisions.oweapp.MainActivity;
import de.gvisions.oweapp.R;
import de.gvisions.oweapp.services.DatabaseHelper;
import de.gvisions.oweapp.services.DatePickerFragment;
import de.madcyph3r.materialnavigationdrawer.MaterialNavigationDrawer;
import de.madcyph3r.materialnavigationdrawer.menu.item.section.MaterialItemSectionFragment;

/**
 * Created by alexa on 10.01.2016.
 */
public class NewItemFragment extends Fragment  {

    SQLiteOpenHelper database;
    SQLiteDatabase connection;

    View v = null;
    Context c = null;

    SharedPreferences oPreference;

    static final String IMAGE_PATH = Environment.getExternalStorageDirectory() + "/ichleihedir/";
    long curTime = System.currentTimeMillis();

    Boolean bFoto = false;



    //View Elemente
    Spinner oSpinner;
    Uri imageUri;
    EditText oTitel;
    EditText oKontakt;
    EditText oBeschreibung;
    EditText oDatum;
    Button oSpeichern;

    Switch oSwitch;

    SurfaceHolder surface;

    ImageButton oImageButtonDate;
    ImageView oKontaktBtn;
    ImageView oDatumBtn;
    ImageView oKameraBtn;

    Switch oKalender;

    ImageButton oImageButtonContact;

    Uri currentContactUri;
    private MaterialNavigationDrawer drawer;
    private static final int REQUEST_CODE = 1337;



    int month, year, day;

    static final int DATE_DIALOG_ID = 999;
    static final int CONTACT_PICKER_RESULT = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_new, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {



        super.onActivityCreated(savedInstanceState);
        //Check directory
        File path = new File(IMAGE_PATH);
        if (path.exists() == false)
        {
            path.mkdirs();

        }
        if (v == null) {
            v = getView();
        }
        if (c == null) {
            c = v.getContext();
        }

        Intent i = getActivity().getIntent();
        if (i.getAction() == "showContact") {
            // important, to handle the back click
            setHasOptionsMenu(true);

            drawer = (MaterialNavigationDrawer) getActivity();


            // show back button
            drawer.showActionBarMenuIcon(MaterialNavigationDrawer.ActionBarMenuItem.BACK);
            // close and lock the drawer
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


        database = new DatabaseHelper(v.getContext());
        connection = database.getWritableDatabase();

        oPreference = PreferenceManager.getDefaultSharedPreferences(c);

        /*
        oKalender = (Switch) v.findViewById(R.id.swKalender);
        oKalender.setChecked(false);
        if (oPreference.getBoolean("defaultKalender", false) == true) {
            oKalender.setChecked(true);
        }*/

        oSpinner = (Spinner) getView().findViewById(R.id.spinner);
        oTitel = (EditText) getView().findViewById(R.id.idTitel);
        oKontakt = (EditText) getView().findViewById(R.id.idKontakt);
        oBeschreibung = (EditText) getView().findViewById(R.id.idBeschreibung);
        oDatum = (EditText) v.findViewById(R.id.idDatum);
        oSpeichern = (Button) v.findViewById(R.id.btnSpeichern);

        oKontaktBtn = (ImageView) getView().findViewById(R.id.btnKontakt);
        oKontakt.setEnabled(false);
        oDatumBtn = (ImageView) getView().findViewById(R.id.btnDatum);

        oSwitch = (Switch) getView().findViewById(R.id.calSwitch);

        oKameraBtn = (ImageView) getView().findViewById(R.id.btnKamera);

        oKameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 /*               final File photo = new File(IMAGE_PATH,  "Image_"+curTime+".jpg");
                imageUri = Uri.fromFile(photo);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                startActivityForResult(intent, REQUEST_CODE);*/

                Intent i=new CameraActivity.IntentBuilder(getActivity())
                        .skipConfirm()
                        .to(new File(IMAGE_PATH, "Image_" + curTime + ".jpg"))
                        .debug()
                        .zoomStyle(ZoomStyle.PINCH)
                        .updateMediaStore()
                        .build();


                startActivityForResult(i, 1337);

            }
        });


        oDatum.setEnabled(false);

        oDatumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment picker = new DatePickerFragment();
                picker.setView(oDatum);
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        oKontaktBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, CONTACT_PICKER_RESULT);
            }
        });

        oSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                //Daten sammeln
                String sTitel = oTitel.getText().toString();
                String sBeschreibung = oBeschreibung.getText().toString();
                String sKontakt = oKontakt.getText().toString();

                String sDatum = oDatum.getText().toString();
                int iRichtung = oSpinner.getSelectedItemPosition();

                if (sTitel.isEmpty() || sKontakt.isEmpty() || sDatum.isEmpty()) {
                    Toast.makeText(v.getContext(), "Fülle bitte alle erforderlichen Felder aus :)", Toast.LENGTH_SHORT).show();
                } else {

                    String sKontaktUri = currentContactUri.toString();
                    sTitel.replace(",", ".");
                    sBeschreibung.replace(",", ".");

                    String sFoto = "";
                    if (bFoto == true) {
                            //"Image_"+curTime+".jpg"
                        sFoto = "Image_"+String.valueOf(curTime)+".jpg";
                    }


                    String sql = "INSERT INTO owe (contacturi, what, fromto, desc, type, deadline, foto) " +
                                 "VALUES ('"+sKontaktUri+"', '"+sTitel+"', '"+sKontakt+"', '"+sBeschreibung+"', "+iRichtung+", '"+sDatum+"', '"+sFoto+"')";

                    connection.execSQL(sql);

                    Toast.makeText(getView().getContext(), "Gespeichert!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);

                    if (oSwitch.isChecked()) {
                        Calendar cal = Calendar.getInstance();
                        Log.d("DATUMTEST", sDatum);
                        String[] datum = sDatum.split(".");
                        for(int i2 = 0; i2 < datum.length; i2++) {
                            Log.d("DATUMTEST "+i2,datum[i2]);
                        }
                        int jahr = Integer.parseInt(datum[2]);
                        int monat = Integer.parseInt(datum[1]);
                        int tag = Integer.parseInt(datum[0]);

                        String text = "";
                        if (iRichtung == 0) {
                            text = "Zurückgabe von " +sTitel+" an "+sKontakt;
                        } else {
                            text = "Du bekommst " +sTitel+" von "+sKontakt + " zurück";
                        }

                        cal.set(jahr, monat, tag);
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("beginTime", cal.getTimeInMillis());
                        intent.putExtra("allDay", true);
                        intent.putExtra("title", text);
                        startActivity(intent);
                    }




                }




            }
        });

    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {

        Log.d("RESULT", String.valueOf(requestCode));
        Log.d("RESULT", String.valueOf(resultCode));

        if (requestCode == REQUEST_CODE) {
            File imgFile = new  File(IMAGE_PATH+"Image_"+curTime+".jpg");
            Log.d("DATEIPFAD", IMAGE_PATH+"Image_"+curTime+".jpg");

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                int nh = (int) ( myBitmap.getHeight() * (1024.0 / myBitmap.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 1024, nh, true);




                oKameraBtn.setImageBitmap(scaled);
                bFoto = true;

            }
        }



        if (requestCode == CONTACT_PICKER_RESULT) {
            try {
                Uri result = data.getData();
                String id = "0";
                currentContactUri = data.getData();
                Log.d("CONTACT URI", data.getData().toString());
                Cursor c = getActivity().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone._ID + "=?",
                        new String[]{result.getLastPathSegment()}, null);
                if (c.getCount() >= 1 && c.moveToFirst()) {
                    final String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.d("CONTACT ID GET", String.valueOf(c.getInt(c.getColumnIndex(ContactsContract.Data.CONTACT_ID))));
                    oKontakt.setText(name);
                    id = String.valueOf(c.getInt(c.getColumnIndex(ContactsContract.Data.CONTACT_ID)));
                }

                c = getActivity().getContentResolver().query(result, null, null, null, null);
                String key = null;
                if (c != null && c.moveToFirst()) {
                    key = c.getString(c.getColumnIndex(ContactsContract.Data.LOOKUP_KEY));
                }
                String uri = "content://com.android.contacts/contacts/lookup/" + key + "/" + id;

                currentContactUri = Uri.parse(uri);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Kein Kontakt gewählt", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get item selected and deal with it
        switch (item.getItemId()) {
            case android.R.id.home:
                // your back action, here get the last section fragment called

                // show the menu button and unlock the drawer
                drawer.showActionBarMenuIcon(MaterialNavigationDrawer.ActionBarMenuItem.MENU);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                MaterialItemSectionFragment fragmentSection = drawer.getCurrentSectionFragment();

                // set the fragment from the selected section
                drawer.changeFragment(fragmentSection.getFragment(), fragmentSection.getFragmentTitle());
                // normally currentSection gets unselect on setCustomFragment call
                // in the next relase, i will add a new method without unselect
                drawer.getCurrentSectionFragment().select();

                // call on current git head. drawer.getCurrentSectionFragment().select(); is not needed
                // drawer.setCustomFragment(drawer.getCurrentSectionFragment().getTargetFragment(), drawer.getCurrentSectionFragment().getFragmentTitle(), true, false);

                // }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case 0:
                    rotate = 0;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

}
