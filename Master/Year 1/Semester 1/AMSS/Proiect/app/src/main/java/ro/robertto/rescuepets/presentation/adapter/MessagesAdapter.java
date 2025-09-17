package ro.robertto.rescuepets.presentation.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.databinding.DeleteLayoutBinding;
import ro.robertto.rescuepets.databinding.ReceiveMsgBinding;
import ro.robertto.rescuepets.databinding.SendMsgBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import ro.robertto.rescuepets.presentation.model.Message;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Message> messages;
    private String senderRoom;
    private String receiverRoom;
    private final int itemSent = 1;
    private final int itemReceive = 2;

    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == itemSent) {
            View view = LayoutInflater.from(context).inflate(R.layout.send_msg, parent, false);
            return new SentMsgHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receive_msg, parent, false);
            return new ReceiveMsgHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return itemSent;
        } else {
            return itemReceive;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder instanceof SentMsgHolder) {
            SentMsgHolder viewHolder = (SentMsgHolder) holder;
            if ("photo".equals(message.getMessage())) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                viewHolder.binding.mLinear.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.image);
            }
            viewHolder.binding.message.setText(message.getMessage());

            viewHolder.itemView.setOnLongClickListener(view -> {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_layout, null);
                DeleteLayoutBinding binding = DeleteLayoutBinding.bind(dialogView);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setView(binding.getRoot())
                        .create();

                binding.everyone.setOnClickListener(v -> {
                    message.setMessage("This message is removed.");
                    String messageId = message.getMessageId();
                    if (messageId != null) {
                        FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                                .getReference()
                                .child("Chats")
                                .child(senderRoom)
                                .child("Messages")
                                .child(messageId)
                                .setValue(message);
                        FirebaseDatabase.getInstance(RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                                .getReference()
                                .child("Chats")
                                .child(receiverRoom)
                                .child("Messages")
                                .child(messageId)
                                .setValue(message);
                    }
                    dialog.dismiss();
                });

                binding.delete.setOnClickListener(v -> {
                    String messageId = message.getMessageId();
                    if (messageId != null) {
                        FirebaseDatabase.getInstance(RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                                .getReference()
                                .child("Chats")
                                .child(senderRoom)
                                .child("Messages")
                                .child(messageId)
                                .setValue(null);
                    }
                    dialog.dismiss();
                });

                binding.cancel.setOnClickListener(v -> dialog.dismiss());

                dialog.show();
                return false;
            });
        } else if (holder instanceof ReceiveMsgHolder) {
            ReceiveMsgHolder viewHolder = (ReceiveMsgHolder) holder;
            if ("photo".equals(message.getMessage())) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                viewHolder.binding.mLinear.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.image);
            }
            viewHolder.binding.message.setText(message.getMessage());

            viewHolder.itemView.setOnLongClickListener(view -> {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_layout, null);
                DeleteLayoutBinding binding = DeleteLayoutBinding.bind(dialogView);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setView(binding.getRoot())
                        .create();

                binding.everyone.setOnClickListener(v -> {
                    message.setMessage("This message is removed.");
                    String messageId = message.getMessageId();
                    if (messageId != null) {
                        FirebaseDatabase.getInstance(RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                                .getReference()
                                .child("Chats")
                                .child(senderRoom)
                                .child("Messages")
                                .child(messageId)
                                .setValue(message);
                        FirebaseDatabase.getInstance(RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                                .getReference()
                                .child("Chats")
                                .child(receiverRoom)
                                .child("Messages")
                                .child(messageId)
                                .setValue(message);
                    }
                    dialog.dismiss();
                });

                binding.delete.setOnClickListener(v -> {
                    String messageId = message.getMessageId();
                    if (messageId != null) {
                        FirebaseDatabase.getInstance(RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                                .getReference()
                                .child("Chats")
                                .child(senderRoom)
                                .child("Messages")
                                .child(messageId)
                                .setValue(null);
                    }
                    dialog.dismiss();
                });

                binding.cancel.setOnClickListener(v -> dialog.dismiss());

                dialog.show();
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentMsgHolder extends RecyclerView.ViewHolder {
        SendMsgBinding binding;

        public SentMsgHolder(View itemView) {
            super(itemView);
            binding = SendMsgBinding.bind(itemView);
        }
    }

    public class ReceiveMsgHolder extends RecyclerView.ViewHolder {
        ReceiveMsgBinding binding;

        public ReceiveMsgHolder(View itemView) {
            super(itemView);
            binding = ReceiveMsgBinding.bind(itemView);
        }
    }
}
