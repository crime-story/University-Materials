package ro.robertto.rescuepets.presentation.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.databinding.ActivityChatBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import ro.robertto.rescuepets.presentation.adapter.MessagesAdapter;
import ro.robertto.rescuepets.presentation.model.Message;
import ro.robertto.rescuepets.presentation.service.MyFirebaseMessagingService;

public class ChatActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;

    private ActivityChatBinding binding;
    private MessagesAdapter adapter;
    private ArrayList<Message> messages;
    private String senderRoom;
    private String receiverRoom;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private ProgressDialog dialog;
    private String senderUid;
    private String receiverUid;
    private String photoPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 25) {
            if (data != null && data.getData() != null) {
                Uri selectedImage = data.getData();
                Calendar calendar = Calendar.getInstance();
                StorageReference reference = storage.getReference().child("Chats")
                        .child(calendar.getTimeInMillis() + "");
                dialog.show();
                reference.putFile(selectedImage)
                        .addOnCompleteListener(task -> {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String filePath = uri.toString();
                                    String messageTxt = binding.messageBox.getText().toString();
                                    Date date = new Date();
                                    Message message = new Message(messageTxt, senderUid);
                                    message.setMessage("photo");
                                    message.setImageUrl(filePath);
                                    binding.messageBox.setText("");
                                    String randomKey = database.getReference().push().getKey();
                                    HashMap<String, Object> lastMsgObj = new HashMap<>();
                                    lastMsgObj.put("lastMsg", message.getMessage());
                                    lastMsgObj.put("lastMsgTime", date.getTime());
                                    database.getReference().child("Chats")
                                            .child(senderRoom)
                                            .updateChildren(lastMsgObj);
                                    database.getReference().child("Chats")
                                            .child(receiverRoom)
                                            .updateChildren(lastMsgObj);
                                    database.getReference().child("Chats")
                                            .child(senderRoom)
                                            .child("Messages")
                                            .child(randomKey)
                                            .setValue(message).addOnSuccessListener(aVoid -> {
                                                database.getReference().child("Chats")
                                                        .child(receiverRoom)
                                                        .child("Messages")
                                                        .child(randomKey)
                                                        .setValue(message)
                                                        .addOnSuccessListener( new OnSuccessListener< Void >() {
                                                            @Override
                                                            public void onSuccess( Void aVoid1 ) {
                                                               /* MyFirebaseMessagingService.sendNotification(
                                                                        senderUid, receiverUid, "",
                                                                        ""
                                                                );*/
                                                            }
                                                        } );
                                            });
                                });
                            }
                        });
            }
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            Calendar calendar = Calendar.getInstance();
            StorageReference reference = storage.getReference().child("Chats")
                    .child(calendar.getTimeInMillis() + "");
            dialog.show();
            reference.putFile(Uri.fromFile(new File(photoPath)))
                    .addOnCompleteListener(task -> {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String filePath = uri.toString();
                                String messageTxt = binding.messageBox.getText().toString();
                                Date date = new Date();
                                Message message = new Message(messageTxt, senderUid);
                                message.setMessage("photo");
                                message.setImageUrl(filePath);
                                binding.messageBox.setText("");
                                String randomKey = database.getReference().push().getKey();
                                HashMap<String, Object> lastMsgObj = new HashMap<>();
                                lastMsgObj.put("lastMsg", message.getMessage());
                                lastMsgObj.put("lastMsgTime", date.getTime());
                                database.getReference().child("Chats")
                                        .child(senderRoom)
                                        .updateChildren(lastMsgObj);
                                database.getReference().child("Chats")
                                        .child(receiverRoom)
                                        .updateChildren(lastMsgObj);
                                database.getReference().child("Chats")
                                        .child(senderRoom)
                                        .child("Messages")
                                        .child(randomKey)
                                        .setValue(message).addOnSuccessListener(aVoid -> {
                                            database.getReference().child("Chats")
                                                    .child(receiverRoom)
                                                    .child("Messages")
                                                    .child(randomKey)
                                                    .setValue(message)
                                                    .addOnSuccessListener(aVoid1 -> { });
                                        });
                            });
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("Presence")
                .child(currentId)
                .setValue("Offline");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child( "Presence" )
                .child( currentId )
                .setValue( "Offline" );
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    private void checkUser() {
        // Check user is logged in or not
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            // user is null, user is not logged in, go to login activity
            startActivity( new Intent( this, LogInActivity.class ) );
            finish();
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        checkUser();
        binding = ActivityChatBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
        getSupportActionBar().setTitle( "Chat" );
        database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl );
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog( this );
        dialog.setMessage( "Uploading image..." );
        dialog.setCancelable( false );
        messages = new ArrayList<>();
        String name = getIntent().getStringExtra( "name" );
        String profile = getIntent().getStringExtra( "image" );
        binding.name.setText( name );
        Glide.with( this ).load( profile )
                .placeholder( R.drawable.profile_pic )
                .into( binding.profilePic01 );
        binding.imageView.setOnClickListener( v -> finish() );
        receiverUid = getIntent().getStringExtra( "uid" );
        senderUid = FirebaseAuth.getInstance().getUid();
        database.getReference().child("Presence").child(receiverUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String status = snapshot.getValue(String.class);
                            if (status != null && !status.isEmpty()) {
                                if (status.equals("Offline")) {
                                    binding.status.setVisibility(View.GONE);
                                } else {
                                    binding.status.setText(status);
                                    binding.status.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;
        adapter = new MessagesAdapter(this, messages, senderRoom, receiverRoom);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPositionWithOffset(0, 100);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        database.getReference().child("Chats")
                .child(senderRoom)
                .child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            if (message != null) {
                                message.setMessageId(snapshot1.getKey());
                                messages.add(message);
                            }
                        }
                        linearLayoutManager.smoothScrollToPosition(binding.recyclerView, null, adapter.getItemCount());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        binding.send.setOnClickListener(v -> {
            String messageTxt = binding.messageBox.getText().toString();
            if (messageTxt.isEmpty()) {
                return;
            }
            Date date = new Date();
            Message message = new Message(messageTxt, senderUid);

            binding.messageBox.setText("");
            String randomKey = database.getReference().push().getKey();
            HashMap<String, Object> lastMsgObj = new HashMap<>();
            lastMsgObj.put("lastMsg", message.getMessage());
            lastMsgObj.put("lastMsgTime", date.getTime());

            database.getReference().child("Chats").child(senderRoom)
                    .updateChildren(lastMsgObj);
            database.getReference().child("Chats").child(receiverRoom)
                    .updateChildren(lastMsgObj);
            database.getReference().child("Chats").child(senderRoom)
                    .child("Messages")
                    .child(randomKey)
                    .setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        database.getReference().child("Chats")
                                .child(receiverRoom)
                                .child("Messages")
                                .child(randomKey)
                                .setValue(message)
                                .addOnSuccessListener(aVoid1 -> {
                                    database.getReference().child("Users").child(senderUid)
                                            .child("name")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String senderName = dataSnapshot.getValue(String.class);
                                                    database.getReference().child("Users").child(senderUid)
                                                            .child("profileImage")
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    String senderProfilePic = snapshot.getValue(String.class);
                                                                    MyFirebaseMessagingService.sendNotification(
                                                                            senderUid, receiverUid, senderName,
                                                                            senderProfilePic
                                                                    );
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {}
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                                            });
                                });
                    });
        });

        binding.attachment.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 25);
        });

        binding.camera.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_TAKE_PHOTO
                );
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.provider",
                            photoFile
                    );
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            }
        });

        Handler handler = new Handler();
        binding.messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.send.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                database.getReference().child("Presence")
                        .child(senderUid)
                        .setValue("typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userStoppedTyping, 1000);
            }

            Runnable userStoppedTyping = () -> database.getReference().child("Presence")
                    .child(senderUid)
                    .setValue("Online");
        });
    }

    private File createImageFile() throws IOException {
        String fileName = "pic";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir
        );
        photoPath = image.getAbsolutePath();
        return image;
    }
}
