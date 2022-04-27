package com.soleus.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soleus.R;
import com.soleus.models.RoomRequest;
import com.soleus.net.ClientNet;

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
    } // end onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtIdCard.setText(String.valueOf(roomRequestList.get(position).getRequestId()));
        holder.txtRoomCard.setText(roomRequestList.get(position).getClientRoom());
        holder.txtItemCard.setText(roomRequestList.get(position).getRequestItem());
        holder.txtDescriptionCard.setText(roomRequestList.get(position).getRequestDescription());
    } // onBindViewHolder







    @Override
    public int getItemCount() {
        return roomRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView txtRoomCard;
        private TextView txtItemCard;
        private TextView txtDescriptionCard;
        private TextView txtIdCard;

        public ViewHolder(View view) {
            super(view);

            view.setOnLongClickListener(this);

            txtRoomCard = view.findViewById(R.id.txtRoomCard);
            txtItemCard = view.findViewById(R.id.txtItemCard);
            txtDescriptionCard = view.findViewById(R.id.txtDescriptionCard);
            txtIdCard = view.findViewById(R.id.txtIdCard);

        }


        @Override
        public boolean onLongClick(View view) {

            final CharSequence[] itemsContextMenu = {"Marcar como finalizada"};

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            builder.setTitle("Petición de la habitación " + txtRoomCard.getText().toString());
            builder.setItems(itemsContextMenu, (dialog, item) -> {
                RoomRequest requestToEnd = new RoomRequest(Integer.parseInt(txtIdCard.getText().toString()));
                Thread endRequest = new Thread( new ClientNet(requestToEnd, "END_REQUEST")) ;
                endRequest.start();

            });
            builder.show();

            return true;
        } // end onLongClick

    } // end ViewHolder class
}
