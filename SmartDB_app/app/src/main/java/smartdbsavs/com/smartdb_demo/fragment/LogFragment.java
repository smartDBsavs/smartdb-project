package smartdbsavs.com.smartdb_demo.fragment;

/**
 * Created by hi on 19-02-2018.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import smartdbsavs.com.smartdb_demo.R;
import smartdbsavs.com.smartdb_demo.adapters.LogsAdapter;
import smartdbsavs.com.smartdb_demo.holders.LogsDTO;
import smartdbsavs.com.smartdb_demo.utils.RecyclerTouchListener;


public class LogFragment extends Fragment implements  View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    View rootView, rootView2;
    private static LogFragment logFragment;
    Context mContext;
    String notifTitle, notifMessage, notifUrl;
    ImageView imageAuth;
    View view;
    LayoutInflater factory;
    Button btn1;
    LinearLayout listLayout;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ChildEventListener mChildEventListener;
    LogsAdapter adapter;
    CardView cv;
    Button btn;
    SwipeRefreshLayout mSwipeRefreshLayout1;


    List<LogsDTO> logsList;
    RecyclerView recyclerView;

    public static LogFragment getInstance() {
        if (logFragment == null) {
            logFragment = new LogFragment();
        }
        return logFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (RelativeLayout) inflater.inflate(R.layout.logs_layout, container, false);
        mContext = getActivity();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Logs_Details");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        mSwipeRefreshLayout1 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container1);
        mSwipeRefreshLayout1.setOnRefreshListener(this);
        mSwipeRefreshLayout1.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout1.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout1.setRefreshing(true);
                onStart();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_card);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        logsList = new ArrayList<>();
        //attachDatabaseReadListener();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext.getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LogsDTO logsDTO = logsList.get(position);
                //Toast.makeText(mContext.getApplicationContext(), logsDTO.getDateTime() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                final LogsDTO logsDTO = logsList.get(position);
                Toast.makeText(mContext.getApplicationContext(), logsDTO.getId() + " is selected!", Toast.LENGTH_SHORT).show();
                final String refId = logsDTO.getId();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setIcon(R.mipmap.bell_ic_launcher);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLog(refId);
                        Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "No Deletion Occur", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public boolean updateLog(String id, LogsDTO logsDTO) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Logs_Details").child(id);
        //updating artist
        LogsDTO logsDTO1 = logsDTO;

        dR.setValue(logsDTO1);
        // Toast.makeText(getActivity().getApplicationContext(), "Log Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteLog(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Logs_Details").child(id);
        dR.removeValue();
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        //attaching value event listener
        mSwipeRefreshLayout1.setRefreshing(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                logsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    LogsDTO logsDTO = postSnapshot.getValue(LogsDTO.class);
               /*     System.out.println("LogFragment data : "+logsDTO.toString());
                    System.out.println("Hello Log");*/
                    logsList.add(logsDTO);
                }
                adapter = new LogsAdapter(mContext, logsList);
                recyclerView.setAdapter(adapter);
                mSwipeRefreshLayout1.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mSwipeRefreshLayout1.setRefreshing(false);

            }
        });
    }

    @Override
    public void onRefresh() {
        onStart();
    }


/*    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        LogsDTO logsDTO = postSnapshot.getValue(LogsDTO.class);
                        System.out.println("getStatus - " + logsDTO.getStatus());
                        if (logsDTO.getStatus() == "1") {

                        }
                    }
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            myRef.addChildEventListener(mChildEventListener);
        }
    }*/


}