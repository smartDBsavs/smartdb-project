package smartdbsavs.com.smartdb_demo.fragment;

/**
 * Created by hi on 19-02-2018.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import smartdbsavs.com.smartdb_demo.R;


public class StatusFragment extends Fragment implements  View.OnClickListener {
    View rootView;


    private static StatusFragment statusFragment;
    Context mContext;

    public static StatusFragment getInstance() {
       if (statusFragment == null) {
        statusFragment = new StatusFragment();
       }
        return statusFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (RelativeLayout) inflater.inflate(R.layout.status_layout, container, false);
        mContext = getActivity();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
