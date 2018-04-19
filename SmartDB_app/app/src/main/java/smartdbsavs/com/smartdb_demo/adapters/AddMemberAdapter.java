package smartdbsavs.com.smartdb_demo.adapters;

import smartdbsavs.com.smartdb_demo.holders.AddMemberDTO;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import smartdbsavs.com.smartdb_demo.R;

import java.util.List;

/**
 * Created by hi on 05-03-2018.
 */

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.MyViewHolder> {

    private List<AddMemberDTO> memberList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView memberId,userName, emailId;

        public MyViewHolder(View view) {
            super(view);
            memberId = (TextView) view.findViewById(R.id.memid_l);
            userName = (TextView) view.findViewById(R.id.username_l);
            emailId = (TextView) view.findViewById(R.id.email_l);
        }
    }


    public AddMemberAdapter(List<AddMemberDTO> memberList) {
        this.memberList = memberList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_member_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AddMemberDTO addMemberDTO = memberList.get(position);
        //holder.memberId.setText(String.valueOf(addMemberDTO.getMemid_AM()));
        holder.userName.setText(addMemberDTO.getUserName_AM());
        holder.emailId.setText(addMemberDTO.getEmailId_AM());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

}