package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.R;

import java.util.List;

import Model.RVListItem;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private Context context;
    private List<RVListItem> listItems;

    public RVAdapter(Context context, List listitem){
        this.context=context;
        this.listItems=listitem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position) {
        RVListItem item=listItems.get(position);

        holder.rvItemName.setText(item.getType());
        holder.rvCompanyName.setText(item.getCompanyName());
        holder.rvEmail.setText(item.getEmail());

        if(item.getType().charAt(0)=='L'){
            holder.rvItemName.setTextColor(Color.RED);
        }
        else{
            holder.rvItemName.setTextColor(Color.parseColor("#006400"));
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void clear() {
        int size = listItems.size();
        listItems.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rvItemName;
        public TextView rvCompanyName;
        public TextView rvEmail;


        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;

            rvItemName=(TextView)itemView.findViewById(R.id.rvItemName);
            rvCompanyName=(TextView)itemView.findViewById(R.id.rvCompanyName);
            rvEmail=(TextView)itemView.findViewById(R.id.rvEmail);
        }
    }
}
