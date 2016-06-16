package de.gvisions.oweapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import de.gvisions.oweapp.fragments.MainFragment;
import de.gvisions.oweapp.fragments.NewItem;
import de.gvisions.oweapp.fragments.NewItemFragment;
import de.gvisions.oweapp.fragments.SettingsFragment;
import de.gvisions.oweapp.fragments.ShowContactFragment;
import de.madcyph3r.materialnavigationdrawer.MaterialNavigationDrawer;
import de.madcyph3r.materialnavigationdrawer.activity.MaterialNavHeadItemActivity;
import de.madcyph3r.materialnavigationdrawer.head.MaterialHeadItem;
import de.madcyph3r.materialnavigationdrawer.listener.MaterialSectionChangeListener;
import de.madcyph3r.materialnavigationdrawer.menu.MaterialMenu;
import de.madcyph3r.materialnavigationdrawer.menu.item.section.MaterialItemSection;
import de.madcyph3r.materialnavigationdrawer.menu.item.section.MaterialItemSectionFragment;
import de.madcyph3r.materialnavigationdrawer.tools.RoundedCornersDrawable;


public class MainActivity extends MaterialNavHeadItemActivity {


    public MaterialNavigationDrawer drawer = null;
    private String newTitle = "Ich leihe dir";



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        int backCount = fragmentManager.getBackStackEntryCount();

        if(backCount > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "Sorry, Bildschirm drehen geht no", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected boolean finishActivityOnNewIntent() {
        return false;
    }

    @Override
    protected int getNewIntentRequestCode(Class clazz) {
        return 0;
    }

    @Override
    public void init(final Bundle savedInstanceState) {


        drawer = this;



        // create menu
        MaterialMenu menu = new MaterialMenu();
        menu.add(new MaterialItemSectionFragment(this, "Start", new MainFragment(), "Ich leihe dir"));
        menu.add(new MaterialItemSectionFragment(this, "Neuer Eintrag"/*, getResources().getDrawable(R.drawable.ic_action_content_add)*/, new NewItemFragment(), "Neuer Eintrag"));
        menu.add(new MaterialItemSectionFragment(this, "Geteilte Inhalte", new MainFragment(), "Geteilte Inhalte"));
        //menu.add(new MaterialItemSectionFragment(this, "Einstellungen", new SettingsFragment(), "Einstellungen"));
        menu.add(new MaterialItemSectionFragment(this, "Über", new MainFragment(), "Über"));

        drawer.setSectionChangeListener(new MaterialSectionChangeListener() {
            @Override
            public void onBeforeChangeSection(MaterialItemSection newSection) {

            }

            @Override
            public void onAfterChangeSection(MaterialItemSection newSection) {
                newTitle = newSection.getTitle();
                Log.d("NEUERTITLE", newTitle);
                if (newTitle == "Start") {
                    newTitle = "Ich leihe dir";
                    //getFragmentManager().popBackStack();
                    changeFragment(new MainFragment(), "Ich leihe dir", true);
                }
                afterInit(savedInstanceState);
            }
        });



        // create Head Item
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_2x);
        final RoundedCornersDrawable drawableAppIcon = new RoundedCornersDrawable(getResources(), bitmap);
        MaterialHeadItem headItem = new MaterialHeadItem(this, "Ich leihe dir", "Hallo!", drawableAppIcon, R.drawable.ild_header, menu);
        this.addHeadItem(headItem);

        this.setActionBarOverlay(false);

        // load menu
        this.loadMenu(getCurrentHeadItem().getMenu());




        // load the first MaterialItemSectionFragment from the menu
        this.loadStartFragmentFromMenu(getCurrentHeadItem().getMenu());

        Log.d("INIT", "INIT OK");

    }

    @Override
    public void afterInit(Bundle savedInstanceState) {
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF33B5E5")));
        actionBar.setTitle(newTitle);

        Intent i = getIntent();
        Log.d("AFTERINIT", "EXTRA: "+i.getStringExtra("start"));
        if (i.getStringExtra("start") == "neu") {
            changeFragment(new NewItem(), "Neuer Eintrag");
        }

/*        if(i.getAction() == "showContact") {
            changeFragment(new ShowContactFragment(), "Kontakt: "+i.getType(), new MainFragment(), true);
            getCurrentSectionFragment().select();
        }
*/
        Log.d("AFTERINIT", "AFTERINIT OK");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        afterInit(savedInstanceState);
    }

}
