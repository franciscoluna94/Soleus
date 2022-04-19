package com.soleus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soleus.R;
import com.soleus.models.RoomRequest;

import java.util.List;

public class RoomRequestAdapter extends RecyclerView.Adapter<RoomRequestAdapter.ViewHolder>  {

    private List<RoomRequest> roomRequestList;
    private Context context;

    public RoomRequestAdapter (List<RoomRequest> roomRequestList, Context context){
        this.context = context;
        this.roomRequestList = roomRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_request_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtRoomCard.setText(roomRequestList.get(position).getClientRoom());
        holder.txtItemCard.setText(roomRequestList.get(position).getRequestItem());
        holder.txtDescriptionCard.setText(roomRequestList.get(position).getRequestDescription());
    }

    @Override
    public int getItemCount() {
        return roomRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtRoomCard;
        private TextView txtItemCard;
        private TextView txtDescriptionCard;


        public ViewHolder(View itemView) {
            super(itemView);

            txtRoomCard = itemView.findViewById(R.id.txtRoomCard);
            txtItemCard = itemView.findViewById(R.id.txtItemCard);
            txtDescriptionCard = itemView.findViewById(R.id.txtDescriptionCard);

        }
    }
}
