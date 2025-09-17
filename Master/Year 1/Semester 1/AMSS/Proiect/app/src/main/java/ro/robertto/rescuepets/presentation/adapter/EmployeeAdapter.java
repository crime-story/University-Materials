package ro.robertto.rescuepets.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.databinding.ItemProfileBinding;
import ro.robertto.rescuepets.presentation.activities.ChatActivity;

public class EmployeeAdapter extends RecyclerView.Adapter< EmployeeAdapter.UserViewHolder> {
    private final Context context;
    private ArrayList<Employee> employeeArrayList;

    public EmployeeAdapter( Context context, ArrayList< Employee > employeeArrayList) {
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        final ItemProfileBinding binding;

        public UserViewHolder(View itemView) {
            super(itemView);
            binding = ItemProfileBinding.bind(itemView);
        }
    }

    public void setFilteredList(ArrayList<Employee> employeeArrayList) {
        this.employeeArrayList = employeeArrayList;
        notifyDataSetChanged();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Employee employee = employeeArrayList.get(position);
        holder.binding.username.setText(employee.getName());
        Glide.with(context)
                .load(employee.getProfileImage())
                .placeholder(R.drawable.profile_pic)
                .into(holder.binding.profile);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("name", employee.getName());
            intent.putExtra("image", employee.getProfileImage());
            intent.putExtra("uid", employee.getUid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size();
    }
}
