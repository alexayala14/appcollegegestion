package com.arz.chech.collegegestion.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.ChatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef;
    private CircleImageView mProfileImage;
    private String mCurrentUserId;
    //private ImageButton mChatAddBtn;
    private ImageButton btn_send;
    private EditText text_send;
    private RecyclerView mMessagesList;
    private final List<Messages> messagesList = new ArrayList<>();
    private ChatAdapter mAdapter;
    private static final int GALLERY_PICK = 1;
    private TextView displayName;
    private String userid;
    private String userName;
    private String userApellido;

    // Storage Firebase
    private StorageReference mImageStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //TOOLBAR
        mChatToolbar = (Toolbar) findViewById(R.id.toolbarMessages);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mChatToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMessagesList = findViewById(R.id.recyclerViewChat);
        mMessagesList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mAdapter = new ChatAdapter(ChatActivity.this, messagesList);
        mMessagesList.setLayoutManager(linearLayoutManager);
        mMessagesList.setAdapter(mAdapter);

        //
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrentUserId = Preferences.obtenerPreferenceString(ChatActivity.this, Preferences.PREFERENCE_TOKEN);
        btn_send = (ImageButton) findViewById(R.id.chat_send_btn);
        text_send = (EditText) findViewById(R.id.chat_message_view);
        displayName = (TextView) findViewById(R.id.display_name);
        Intent intent = getIntent();
        userid = intent.getStringExtra("user_id");
        userName = intent.getStringExtra("user_name");
        userApellido = intent.getStringExtra("user_apellido");
        //

        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mRootRef.child("Chat").child(mCurrentUserId).child(userid).child("seen").setValue(true);
        //mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);
        readMessages();
        displayName.setText(userName + " " + userApellido);
        mAdapter.enviarDatos(userName, userApellido);
        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 if(!dataSnapshot.hasChild(userid)){
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + userid, chatAddMap);
                    chatUserMap.put("Chat/" + userid + "/" + mCurrentUserId, chatAddMap);
                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null){
                                Log.d("CHAT_LOG", databaseError.getMessage().toString());
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        /*
        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });*/
    }

    private void readMessages(){
        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(userid);

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Messages message = snapshot.getValue(Messages.class);
                    messagesList.add(message);
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendMessage() {
        String message = text_send.getText().toString();
        if(!TextUtils.isEmpty(message)){
            String current_user_ref = "Messages/" + mCurrentUserId + "/" + userid;
            String chat_user_ref = "Messages/" + userid + "/" + mCurrentUserId;
            DatabaseReference user_message_push = mRootRef.child("Messages")
                    .child(mCurrentUserId).child(userid).push();
            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message.trim());
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);
            messageMap.put("receiver", userid);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            text_send.setText("");


            mRootRef.child("Chatlist").child(mCurrentUserId).child(userid).child("id").setValue(userid);
            mRootRef.child("Chatlist").child(mCurrentUserId).child(userid).child("timestamp").setValue(ServerValue.TIMESTAMP);
            mRootRef.child("Chat").child(mCurrentUserId).child(userid).child("seen").setValue(true);
            mRootRef.child("Chat").child(mCurrentUserId).child(userid).child("timestamp").setValue(ServerValue.TIMESTAMP);
            mRootRef.child("Chat").child(userid).child(mCurrentUserId).child("seen").setValue(false);
            mRootRef.child("Chat").child(userid).child(mCurrentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);
            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        Log.d("CHAT_LOG", databaseError.getMessage().toString());
                    }
                }
            });

        }else {
            Toast.makeText(ChatActivity.this, "No puede enviar un mensaje vacío", Toast.LENGTH_SHORT).show();
        }

    }
}
