package smartdbsavs.com.smartdb_demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smartdbsavs.com.smartdb_demo.fragment.AddMemberFragment;
import smartdbsavs.com.smartdb_demo.fragment.LogFragment;
import smartdbsavs.com.smartdb_demo.fragment.StatusFragment;
import smartdbsavs.com.smartdb_demo.holders.LogsDTO;
import smartdbsavs.com.smartdb_demo.holders.RegistrationDetailDTO;
import smartdbsavs.com.smartdb_demo.service.FirebaseIDService;
import smartdbsavs.com.smartdb_demo.utils.PrefHelper;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;

    private FirebaseIDService fis;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference1, mMessagesDatabaseReference2;
    public RegistrationDetailDTO registrationDetailDTO;
    private ChildEventListener mChildEventListener;


    private TextView textView1, textView2;
    private static String mUsername, mEmail;
    protected static boolean isVisible = false;
    public Bundle getBundle = null;
    String titleMessage, label, dateTime, imageUrl, btnState, status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();


            fis = new FirebaseIDService();
            fis.onTokenRefresh();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mMessagesDatabaseReference1 = mFirebaseDatabase.getReference("Registration_Details");
            mMessagesDatabaseReference2 = mFirebaseDatabase.getReference("Logs_Details");

            //FirebaseApp.initializeApp(mContext);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            if(isOnline()) {
            View hView = navigationView.getHeaderView(0);
            textView1 = (TextView) hView.findViewById(R.id.nav_txt1);
            textView2 = (TextView) hView.findViewById(R.id.nav_txt2);


            Bundle getBundle = getIntent().getBundleExtra("Notification Details");
            if (getBundle != null && getBundle.getString("action") != null && getBundle.getString("action").equalsIgnoreCase("notification")) {
                titleMessage = (String) getBundle.getString("title");
                label = (String) getBundle.getString("label");
                dateTime = (String) getBundle.getString("message");
                imageUrl = (String) getBundle.getString("url");
                status = (String) getBundle.getString("status");
                btnState = (String) getBundle.getString("btnStatus");
                Uri myUri = Uri.parse(imageUrl);

        /*    logsDTO = new LogsDTO(titleMessage,label,dateTime,imageUrl,SplashScreen.status);
            System.out.println(logsDTO.toString());*/
                new PushDataToFCM().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Log.d("PushFirebase", "not pushed");
            }


            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        onSignedInInitialize(user.getDisplayName(), user.getEmail());
                    } else {
                        onSignedOutCleanup();
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(
                                                Arrays.asList(
                                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                                        .setTheme(R.style.LoginTheme)
                                        .build(),
                                RC_SIGN_IN);
                    }
                }
            };
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null && isOnline()) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(isOnline())
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitialize(String username, String email) {
        mUsername = username;
        mEmail = email;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                SetRegIdToServer();
            }
        }, 10000);

    }

    private void onSignedOutCleanup() {
        //mUsername = ANONYMOUS;
        detachDatabaseReadListener();

    }

    private void SetRegIdToServer() {
        Boolean state = PrefHelper.getInstance(mContext).getStatusId();

        if (!state) {
            PrefHelper.getInstance(mContext).setRegistationId(fis.TOKEN);
            String regId = PrefHelper.getInstance(mContext).getRegistrationId();
            textView1.setText(mUsername);
            textView2.setText(mEmail);


            Log.d("Registration", regId);
            Log.d("Username", mUsername);
            Log.d("Email", mEmail);


            registrationDetailDTO = new RegistrationDetailDTO(mUsername, mEmail, regId);
            mMessagesDatabaseReference1.push().setValue(registrationDetailDTO);
            PrefHelper.getInstance(mContext).setStatus(true);
       }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START) && mContext == MainActivity.this) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_logs) {

        } else if (id == R.id.nav_member) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddMemberFragment(), "ADD MEMBER");
        adapter.addFragment(new LogFragment(), "LOGS");
        adapter.addFragment(new StatusFragment(), "STATUS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
        Log.d("IsVisible", String.valueOf(isVisible));
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
        Log.d("IsVisible", String.valueOf(isVisible));
    }


    private class PushDataToFCM extends  AsyncTask<String,String,String>{

        LogsDTO logsDTO= null;

        @Override
        protected String doInBackground(String... strings) {

            final int[] flag = {0};
            final String id= mMessagesDatabaseReference2.push().getKey();
            System.out.println("Id is - "+id);


            mMessagesDatabaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                  /*  LogsDTO logsDTO = dataSnapshot.getValue(LogsDTO.class);
                    System.out.println("MainActivity data : "+logsDTO.toString());*/
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        LogsDTO logsDTO = postSnapshot.getValue(LogsDTO.class);
                        System.out.println("MainActivity data : "+logsDTO.toString());
                        System.out.println("Hello Main");
                        System.out.println("Main Datetime - "+logsDTO.getDateTime()+ "Notif Datetime - "+ dateTime);
                        if(logsDTO.getDateTime().equals(dateTime))
                        {

                            flag[0] = 1;
                            System.out.println("Flag Changed "+ flag[0]);
                            break;
                        }
                    }
                    if(flag[0] == (int)(0)) {
                        System.out.println("updated flag "+ flag[0]);
                        logsDTO = new LogsDTO(id, titleMessage, label, dateTime, imageUrl, status, btnState);
                        mMessagesDatabaseReference2.child(id).setValue(logsDTO);
                        System.out.println(logsDTO.toString());
                        DbController.getInstance(mContext).addLogs(logsDTO);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    
                }
            });

            return null;
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference2.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();


        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity to load the content");
                alertDialog.setIcon(R.mipmap.bell_ic_launcher);

                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();


            Toast.makeText(mContext, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}


