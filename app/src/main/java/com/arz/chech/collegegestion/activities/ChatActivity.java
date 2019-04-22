package com.arz.chech.collegegestion.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.ChatAdapter;
import com.arz.chech.collegegestion.entidades.Bandera;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Messages;
import com.arz.chech.collegegestion.fragments.APIService;
import com.arz.chech.collegegestion.notifications.Client;
import com.arz.chech.collegegestion.notifications.Data;
import com.arz.chech.collegegestion.notifications.MyFirebaseMessaging;
import com.arz.chech.collegegestion.notifications.MyResponse;
import com.arz.chech.collegegestion.notifications.Sender;
import com.arz.chech.collegegestion.notifications.Token;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef, reference;
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
    private APIService apiService;
    private boolean notify = false;
    private String banderaNot;
    Boolean bann;
    private String urlimagen;

    public static final String prefGlobant="collegegestion.shared";
    public static final String prefbandera="collegegestion.bande";


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

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        mMessagesList = (RecyclerView) findViewById(R.id.recyclerViewChat);
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
        urlimagen=intent.getStringExtra("imagenurl");
        mProfileImage=findViewById(R.id.profile_imagen);

        //

        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mRootRef.child("Chat").child(mCurrentUserId).child(userid).child("seen").setValue(true);
        //mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);

        setImageView(urlimagen);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatosUsuario datosUsuario = dataSnapshot.getValue(DatosUsuario.class);
                userName = datosUsuario.getNombre();
                userApellido = datosUsuario.getApellido();
                displayName.setText(userName + " " + userApellido);
                mAdapter.enviarDatos(userName, userApellido);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        readMessages();

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
                notify = true;
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
                mMessagesList.scrollToPosition(messagesList.size()-1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void setImageView(String imagenurl){
        Glide
                .with(ChatActivity.this)
                .load(imagenurl)
                .fitCenter()
                .error(R.drawable.default_avatar)
                .into(mProfileImage);
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
            banderaNot="1";





        }else {
            Toast.makeText(ChatActivity.this, "No puede enviar un mensaje vacío", Toast.LENGTH_SHORT).show();
        }

        final String msg = message.trim();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatosUsuario user = dataSnapshot.getValue(DatosUsuario.class);
                if (notify) {
                    banderaNot="1";
                   /* SharedPreferences ban = getApplication().getSharedPreferences(prefGlobant,MODE_PRIVATE);
                    SharedPreferences.Editor editor=ban.edit();
                    editor.putBoolean(prefbandera,banderaNot);
                    editor.apply();
                    System.out.println("bandera antes del mensaje:"+ban.getBoolean(prefbandera,false));*/


                    sendNotification(userid, user.getNombre() + " " + user.getApellido(), msg,banderaNot,user.getImagenurl());

                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMessagesList.scrollToPosition(messagesList.size()-1);
    }

    private void sendNotification(String receiver, final String username, final String message,final String banderaNot,final String urlimagen){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(mCurrentUserId, R.mipmap.ic_launcher, username+": "+message, "AppCollegeGestion", userid,banderaNot,urlimagen);

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200){
                                if (response.body().success != 1){
                                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
