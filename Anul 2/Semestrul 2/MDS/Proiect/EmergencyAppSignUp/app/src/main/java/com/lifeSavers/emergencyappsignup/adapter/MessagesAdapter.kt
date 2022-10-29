package com.lifeSavers.emergencyappsignup.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.lifeSavers.emergencyappsignup.R
import com.lifeSavers.emergencyappsignup.databinding.DeleteLayoutBinding
import com.lifeSavers.emergencyappsignup.databinding.ReceiveMsgBinding
import com.lifeSavers.emergencyappsignup.databinding.SendMsgBinding
import com.lifeSavers.emergencyappsignup.model.Message

class MessagesAdapter(
    var context: Context,
    messages: ArrayList<Message>?,
    senderRoom: String,
    receiverRoom: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    lateinit var messages: ArrayList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    var senderRoom: String
    var receiverRoom: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg, parent, false)
            SentMsgHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.receive_msg, parent, false)
            SentMsgHolder(view)
            ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message: Message = messages[position]
        return if (FirebaseAuth.getInstance().uid == message.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder.javaClass == SentMsgHolder::class.java) {
            val viewHolder = holder as SentMsgHolder
            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.message.text = message.message
            viewHolder.itemView.setOnLongClickListener {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.delete_layout, null)

                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener(View.OnClickListener {
                    message.message = "This message is removed."
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                            .reference.child("Chats")
                            .child(senderRoom)
                            .child("Messages")
                            .child(it1).setValue(message)
                    }
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                            .reference.child("Chats")
                            .child(receiverRoom)
                            .child("Messages")
                            .child(it1).setValue(message)
                    }
                    dialog.dismiss()
                })
                binding.delete.setOnClickListener(View.OnClickListener {
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                            .reference.child("Chats")
                            .child(senderRoom)
                            .child("Messages")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()
                })
                binding.cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
                dialog.show()
                false
            }
        } else {
            val viewHolder = holder as ReceiveMsgHolder
            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.message.text = message.message
            viewHolder.itemView.setOnLongClickListener {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.delete_layout, null)

                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener {
                    message.message = "This message is removed."
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                            .reference.child("Chats")
                            .child(senderRoom)
                            .child("Messages")
                            .child(it1).setValue(message)
                    }
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                            .reference.child("Chats")
                            .child(receiverRoom)
                            .child("Messages")
                            .child(it1!!).setValue(message)
                    }
                    dialog.dismiss()
                }
                binding.delete.setOnClickListener {
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                            .reference.child("Chats")
                            .child(senderRoom)
                            .child("Messages")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
                dialog.show()
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class SentMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SendMsgBinding

        init {
            binding = SendMsgBinding.bind(itemView)
        }
    }

    inner class ReceiveMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ReceiveMsgBinding

        init {
            binding = ReceiveMsgBinding.bind(itemView)
        }
    }

    init {
        if (messages != null) {
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }

}