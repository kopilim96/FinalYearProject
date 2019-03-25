package kopilim.scs.prototyping;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    private Context context;
    private List<information> informationList;
    private OnItemChickListner onItemChickListner;

    public interface OnItemChickListner{
        void onItemClick(int position);
    }

    public void setOnItemChickListner(OnItemChickListner onItemChickListner){
        this.onItemChickListner = onItemChickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, count;
        private ImageView thumbnail;
        public View view;

        public MyViewHolder(View v){
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            count = (TextView) v.findViewById(R.id.count);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        }

    }//End of Class MyVIewHolder

    public CardAdapter(Context context, List<information> informationList){
        this.context = context;
        this.informationList = informationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_information, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        information information = informationList.get(position);
        holder.title.setText(information.getTitle());
        /*
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch(position){
                    case 0:
                        //Information about QR Code
                        Toast.makeText(view.getContext(), position+"", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //Information about Color QR Code
                        Toast.makeText(view.getContext(), position+"", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //Information about this project
                        Toast.makeText(view.getContext(), position+"", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }

        });
        */

        // loading information cover using Glide library
        Glide.with(context).load(information.getThumbnail()).into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return informationList.size();
    }
}