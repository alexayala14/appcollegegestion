package com.arz.chech.collegegestion.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.fragments.GruposFragment;
import com.arz.chech.collegegestion.fragments.MessageFragment;
import com.arz.chech.collegegestion.fragments.DetalleFragment;
import com.arz.chech.collegegestion.fragments.MisPublicacionesFragment;
import com.arz.chech.collegegestion.fragments.PublicacionesFragment;
import com.arz.chech.collegegestion.activities.MensajesGruposActivity;
import com.arz.chech.collegegestion.preferences.Preferences;

public class MenuPrincipalActivity extends AppCompatActivity implements PublicacionesFragment.OnFragmentInteractionListener,MisPublicacionesFragment.OnFragmentInteractionListener,MessageFragment.OnFragmentInteractionListener,DetalleFragment.OnFragmentInteractionListener,GruposFragment.OnFragmentInteractionListener {
    PublicacionesFragment publicaciones;
    MisPublicacionesFragment misPublicaciones;
    MessageFragment mensajes;
    GruposFragment grupos;
    private int num;
    private  int numper;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        num= Preferences.obtenerPreferenceInt(MenuPrincipalActivity.this,Preferences.PREFERENCE_ESTADO_ID_PERFIL);
        if(num == 5 || num == 4){
            tabLayout.removeTabAt(2);
            tabLayout.removeTabAt(1);
        }


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principal, menu);
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
            Preferences.savePreferenceBoolean(MenuPrincipalActivity.this, false, Preferences.PREFERENCE_ESTADO_SESION);
            Preferences.savePreferenceString(MenuPrincipalActivity.this,null,Preferences.PREFERENCE_TOKEN);
            Preferences.savePreferenceInt(MenuPrincipalActivity.this,-1,Preferences.PREFERENCE_ESTADO_ID_PERFIL);
            Preferences.savePreferenceString(MenuPrincipalActivity.this,null,Preferences.PREFERENCE_USUARIO);
            Preferences.savePreferenceString(MenuPrincipalActivity.this, null, Preferences.PREFERENCE_NOMBRE);
            Preferences.savePreferenceString(MenuPrincipalActivity.this, null, Preferences.PREFERENCE_APELLIDO);
            NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            noti.cancelAll();
            Intent intent = new Intent(MenuPrincipalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(id==R.id.action_perfil){
            Intent intent = new Intent(MenuPrincipalActivity.this,PerfilUsuarioActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu_principal, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position){
                case 0:
                    publicaciones=new PublicacionesFragment();
                    return publicaciones;
                case 1:
                    misPublicaciones=new MisPublicacionesFragment();
                    return misPublicaciones;
                case 2:
                    mensajes = new MessageFragment();
                    return mensajes;
                case 3:
                    grupos = new GruposFragment();
                    return grupos;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

           /* try { numper=Integer.parseInt(Preferences.PREFERENCE_ESTADO_ID_PERFIL);} catch(NumberFormatException nfe) {}
            if(numper == 1){
                num=3;
            }
            if(numper == 2){
                num=3;
            }
            if(numper == 5){
                num=2;
            }*/

            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Publicaciones";
                case 1:
                    return "Mis Publicaciones";
                case 2:
                    return "Mensajes";
                case 3:
                    return "Grupos";
            }
            return null;
        }
    }
}
