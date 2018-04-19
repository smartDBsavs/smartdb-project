package smartdbsavs.com.smartdb_demo.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import smartdbsavs.com.smartdb_demo.DbController;
import smartdbsavs.com.smartdb_demo.MainActivity;
import smartdbsavs.com.smartdb_demo.R;
import smartdbsavs.com.smartdb_demo.fragment.LogFragment;
import smartdbsavs.com.smartdb_demo.holders.LogsDTO;
import smartdbsavs.com.smartdb_demo.utils.PrefHelper;



/**
 * Created by hi on 16-03-2018.
 */


public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogsViewHolder> {


    private Context mContext;
    private List<LogsDTO> logList;



    public LogsAdapter(Context mCtx, List<LogsDTO> logList) {
        this.mContext = mCtx;
        this.logList = logList;
    }


    class LogsViewHolder extends RecyclerView.ViewHolder {

        TextView title, label, detail;
        ImageView imageView;
        Button btn1;
        public LogsViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textTitle);
            label = itemView.findViewById(R.id.textLabel);
            detail = itemView.findViewById(R.id.textDetail);
            imageView = itemView.findViewById(R.id.imageView);
            btn1 = itemView.findViewById(R.id.buttonAuth);


        }
    }

    @Override
    public LogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_layout, null);
        return new LogsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LogsViewHolder holder, int position) {
        //getting the product of the specified position
        final LogsDTO logsDTO = logList.get(position);


        //binding the data with the viewholder views
        holder.title.setText(logsDTO.getTitleMessage());
        holder.label.setText(logsDTO.getLabel());
        holder.detail.setText(String.valueOf(logsDTO.getDateTime()));
        holder.btn1.setText(logsDTO.getBtn_status());

        final Uri myUri = Uri.parse(logsDTO.getImageUrl());
        //holder.imageView.setImageURI(logsDTO.getUri());
        Picasso.get()
                //.load(logsDTO.getUri())
                .load(myUri)
                .placeholder(R.mipmap.bell_ic_launcher)   // optional
                .error(R.drawable.error)      // optional
                .resize(400,400)
                .centerCrop()// optional
                .into(holder.imageView);


                holder.btn1.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        View dialogView = inflater.inflate(R.layout.auth_layout, null);
                        builder.setView(dialogView);
                        ImageView imagecheck = (ImageView) dialogView.findViewById(R.id.image_auth);

                        builder.setCancelable(true);
                        Picasso.get()
                                .load(myUri)
                                .placeholder(R.mipmap.bell_ic_launcher)   // optional
                                .error(R.drawable.error)      // optional
                                .resize(400, 400)
                                .centerCrop()// optional
                                .into(imagecheck);
                        builder.setTitle("SmartDB");
                        builder.setInverseBackgroundForced(true);

                        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "Door opens", Toast.LENGTH_SHORT).show();
                                logsDTO.setStatus("1");
                                holder.btn1.setTextColor(mContext.getResources().getColor(R.color.green));
                                logsDTO.setBtn_status("Allowed");
                                holder.btn1.setEnabled(false);
                                DbController.getInstance(mContext).updateLogs(logsDTO);
                                LogFragment.getInstance().updateLog(logsDTO.getId(), logsDTO);

                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("DisAllow", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "Door not opens", Toast.LENGTH_SHORT).show();
                                logsDTO.setStatus("2");
                                holder.btn1.setTextColor(mContext.getResources().getColor(R.color.red));
                                logsDTO.setBtn_status("DisAllowed");
                                holder.btn1.setEnabled(false);
                                DbController.getInstance(mContext).updateLogs(logsDTO);
                                LogFragment.getInstance().updateLog(logsDTO.getId(), logsDTO);
                                dialog.dismiss();
                            }
                        });
                        if(holder.btn1.isEnabled()) {
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else
                        {
                            Toast.makeText(mContext, "This log has been answered", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        if(!logsDTO.getStatus().equals("0")){
            System.out.println(logsDTO.getStatus());
            holder.btn1.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

}