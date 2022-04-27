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
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

import java.util.List;

public class UserModelAdapter extends RecyclerView.Adapter<UserModelAdapter.ViewHolder> {

    private List<UserModel> usersList;
    private Context context;


    public UserModelAdapter (List<UserModel> usersList, Context context){
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_model_card, parent, false);
        return new UserModelAdapter.ViewHolder(view);
    } // end onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull UserModelAdapter.ViewHolder holder, int position) {
        holder.txtRoomNumberCard.setText(String.valueOf(usersList.get(position).getUser()));
        holder.txtNameCard.setText(usersList.get(position).getName());
        holder.txtDepartmentCard.setText(usersList.get(position).getDepartment());
    } // onBindViewHolder


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView txtRoomNumberCard;
        private TextView txtNameCard;
        private TextView txtDepartmentCard;

        public ViewHolder(View view) {
            super(view);

            view.setOnLongClickListener(this);

            txtRoomNumberCard = view.findViewById(R.id.txtRoomNumberCard);
            txtNameCard = view.findViewById(R.id.txtNameCard);
            txtDepartmentCard = view.findViewById(R.id.txtUserDepartmentCard);

        }


        @Override
        public boolean onLongClick(View view) {

            final CharSequence[] itemsContextMenu = {"Borrar", "Modificar"};

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            builder.setTitle("Usuario " + txtRoomNumberCard.getText().toString());
            builder.setItems(itemsContextMenu, (dialog, item) -> {
                if (item == 0) {
                    UserModel userToDelete = new UserModel(txtRoomNumberCard.getText().toString());
                    Thread deleteUser = new Thread( new ClientNet(userToDelete, "DELETE_USER", view)) ;
                    deleteUser.start();
                } else if (item == 1) {
                    UserModel userToModify = new UserModel(txtRoomNumberCard.getText().toString());
                    Thread modifyUser = new Thread( new ClientNet(userToModify, "GET_USER", view)) ;
                    modifyUser.start();
                }


            });
            builder.show();

            return true;
        } // end onLongClick

    } // end ViewHolder class
}
