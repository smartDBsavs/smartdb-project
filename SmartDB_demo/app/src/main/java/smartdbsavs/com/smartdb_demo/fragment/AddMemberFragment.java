package smartdbsavs.com.smartdb_demo.fragment;

/**
 * Created by hi on 19-02-2018.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import smartdbsavs.com.smartdb_demo.DbController;
import smartdbsavs.com.smartdb_demo.R;
import smartdbsavs.com.smartdb_demo.adapters.AddMemberAdapter;
import smartdbsavs.com.smartdb_demo.adapters.LogsAdapter;
import smartdbsavs.com.smartdb_demo.holders.AddMemberDTO;
import smartdbsavs.com.smartdb_demo.holders.LogsDTO;
import smartdbsavs.com.smartdb_demo.utils.RecyclerTouchListener;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddMemberFragment extends Fragment implements  View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    View rootView;
    private static AddMemberFragment addMemberFragment;
    Context mContext;
    FloatingActionButton fab;
    AlertDialogFragment alertDialogFragment;
    EditText textView1,textView2;
    private final String REQUIRED_FIELD = "Required field";
    //for recycler view
    private List<AddMemberDTO> memList = new ArrayList<>();
    private List<AddMemberDTO> displayList;
    private RecyclerView recyclerView;
    private AddMemberAdapter mAdapter;
    private FirebaseDatabase database2;
    private DatabaseReference myRef2;
    SwipeRefreshLayout mSwipeRefreshLayout;



    public static AddMemberFragment getInstance() {
       if (addMemberFragment == null) {
        addMemberFragment = new AddMemberFragment();
       }
        return addMemberFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (RelativeLayout) inflater.inflate(R.layout.add_member_layout, container, false);
        mContext = getActivity();
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        database2 = FirebaseDatabase.getInstance();
        myRef2 = database2.getReference("AddMember_Details");
        textView1=(EditText) rootView.findViewById(R.id.username_d);
        textView1=(EditText) rootView.findViewById(R.id.email_d);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                onStart();
            }
        });


        mAdapter = new AddMemberAdapter(memList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);
        System.out.println("Adapter is set");
        //prepareMemberData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext.getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AddMemberDTO addMemberDTO = memList.get(position);
                //Toast.makeText(mContext.getApplicationContext(), addMemberDTO.getUserName_AM() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                final AddMemberDTO addMemberDTO = memList.get(position);
                final String refId1 = addMemberDTO.getMemid_AM();
                createPopupMenu(mContext, view, position, refId1);
            }
        }));
        fab.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                alertDialogFragment = new AlertDialogFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.add_mem_layout, alertDialogFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
                }
    }

    private void onShow(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.alert_dialog_layout, null))
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        Toast.makeText(getActivity(), "ADD Member Successfull", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Add member failed", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        // alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(1);
        alertDialog.show();
    }


   /* public void prepareMemberData() {
        // memberList.add(addMemberDTO);
        //System.out.println(memberList.size());
        Log.d("Reading: ", "Reading all contacts..");

        clear();
        displayList = DbController.getInstance(mContext).getAllMembers();
        for (AddMemberDTO addMemberDTO : displayList) {
            if (addMemberDTO != null) {
                memList.add(addMemberDTO);
            }
        }

        mAdapter.notifyDataSetChanged();
    }*/

    public void clear() {
            int size = memList.size();
            memList.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
    }

    @Override
    public void onStart() {
        super.onStart();
        //attaching value event listener
        mSwipeRefreshLayout.setRefreshing(true);
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    AddMemberDTO addMemberDTO = postSnapshot.getValue(AddMemberDTO.class);
               /*     System.out.println("LogFragment data : "+logsDTO.toString());
                    System.out.println("Hello Log");*/
                    memList.add(addMemberDTO);
                }
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean deleteMember(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("AddMember_Details").child(id);
        dR.removeValue();
        return true;
    }



    public void createPopupMenu(final Context mContext, View view, final Integer id, final String memId) {
        try {

            Context wrapper = new ContextThemeWrapper(mContext, R.style.MenuTheme);
            PopupMenu popupMenu = new PopupMenu(wrapper, view);


            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                }
            });

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    // TODO Auto-generated method stub
                    switch (item.getItemId()) {
                        case R.id.menu_delete:
                           /* long deletedRow=  DbController.getInstance(mContext).deleteSavedLocation(lat,longi);
                            updateAdapter();*/
                            deleteMember(memId);
                            return true;
                    }
                    return false;
                }
            });

            popupMenu.inflate(R.menu.more_menu);

            Object menuHelper;
            Class[] argTypes;
            try {
                Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                fMenuHelper.setAccessible(true);
                menuHelper = fMenuHelper.get(popupMenu);
                argTypes = new Class[]{boolean.class};
                menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
            } catch (Exception e) {
                popupMenu.show();
                return;
            }
            popupMenu.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        onStart();
    }
}
