package smartdbsavs.com.smartdb_demo.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.content.Intent;
import android.net.Uri;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smartdbsavs.com.smartdb_demo.MainActivity;
import smartdbsavs.com.smartdb_demo.R;
import smartdbsavs.com.smartdb_demo.adapters.AddMemberAdapter;
import smartdbsavs.com.smartdb_demo.DbController;
import smartdbsavs.com.smartdb_demo.holders.AddMemberDTO;
import smartdbsavs.com.smartdb_demo.utils.PrefHelper;

/**
 * Created by hi on 03-03-2018.
 */

public class AlertDialogFragment extends Fragment implements  View.OnClickListener {

    View rootView;
    private static AlertDialogFragment alertDialogFragment;
    Context mContext;
    FloatingActionButton fab;
    EditText textView1,textView2;
    Button btn1,btn2;
    AddMemberFragment addMemberFragment;
    private static String MESSAGE_EMAIL;
    public static int counterId=0;
    String usernameDetail=null,emailDetail=null;
    FirebaseDatabase database1;
    DatabaseReference myRef1 ;

    public static AlertDialogFragment getInstance() {
        if (alertDialogFragment == null) {
            alertDialogFragment = new AlertDialogFragment();
        }
        return alertDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (RelativeLayout) inflater.inflate(R.layout.alert_dialog_layout, container, false);
        mContext = getActivity();
        textView1=(EditText) rootView.findViewById(R.id.username_d);
        textView2=(EditText) rootView.findViewById(R.id.email_d);
        btn1=(Button) rootView.findViewById(R.id.add_dialog);
        btn2=(Button) rootView.findViewById(R.id.cancel_dialog);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        database1 = FirebaseDatabase.getInstance();
        myRef1 = database1.getReference("AddMember_Details");
        return rootView;
    }

    @Override
    public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_dialog:
                        if(textView1!=null && !textView1.getText().toString().trim().equals("") && textView2!=null && !textView2.getText().toString().trim().equals("")) {
                            Toast.makeText(mContext, "Pressed Add", Toast.LENGTH_SHORT).show();
                            sendEmail();
                            FragmentChange();
                        }
                        else
                        {
                            textView1.setError("Required field");
                            textView2.setError("Required field");

                        }
                        break;
                    case R.id.cancel_dialog:
                        FragmentChange();
                        Toast.makeText(mContext, "Pressed Cancel", Toast.LENGTH_SHORT).show();
                        break;
                }
        }

    public void FragmentChange()
    {
        addMemberFragment=new AddMemberFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.add_mem_layout, addMemberFragment);
        transaction.commit();
      /*  if (getFragmentManager().getBackStackEntryCount() > 0){
            boolean done = getFragmentManager().popBackStackImmediate();
        }*/
    }

    private void sendEmail()
    {
        MESSAGE_EMAIL= "Hi! You are now a Member of SmartDB application. Through below link you can install the app and smartly secure your home. Link : ";
        emailDetail = textView2.getText().toString();
        String subject = "Welcome to SmartDb : Member";

        String app = "SmartDB_App_DownloadLink";
        String link_val = "https://drive.google.com/file/d/1zu1kKJwGZWRcXmdmZfwDgmw02t75GRSD/view?usp=sharing";


        String body = "<a href=\"" + link_val + "\">" + app + "</a>";
        String message = MESSAGE_EMAIL;

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("Mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailDetail});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(
                Intent.EXTRA_TEXT,
                Html.fromHtml(new StringBuilder()
                        .append("<p><b>Hi! You are now a Member of SmartDB application. Through below link you can install the app and smartly secure your home.</b></p>")
                        .append("<p><b>App Download Link : <b><p>")
                        .append("<a>https://drive.google.com/file/d/1zu1kKJwGZWRcXmdmZfwDgmw02t75GRSD/view?usp=sharing</a>")
                        .append("<small><p>Thanks for joining SmartDB</p></small>")
                        .toString())
        );

        //need this to prompts email client only
        email.setType("text/html");

        setDataToList();

        try {
            getActivity().startActivity(Intent.createChooser(email, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToList() {
        usernameDetail=textView1.getText().toString();
        emailDetail=textView2.getText().toString();
        System.out.println(usernameDetail+" "+emailDetail);

        final String id= myRef1.push().getKey();
        System.out.println("Id of AddMember is - "+id);

        AddMemberDTO addMemberDTO= new AddMemberDTO(id, usernameDetail, emailDetail);
        DbController.getInstance(mContext).addMember(addMemberDTO);
        myRef1.child(id).setValue(addMemberDTO);
    }



}



