package com.arz.chech.collegegestion.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.arz.chech.collegegestion.adapters.ChatAdapterGroup;
import com.arz.chech.collegegestion.entidades.DatosUsuario;
import com.arz.chech.collegegestion.entidades.Grupo;
import com.arz.chech.collegegestion.entidades.Messages;
import com.arz.chech.collegegestion.fragments.APIService;
import com.arz.chech.collegegestion.notifications.Client;
import com.arz.chech.collegegestion.notifications.Data;
import com.arz.chech.collegegestion.notifications.MyResponse;
import com.arz.chech.collegegestion.notifications.Sender;
import com.arz.chech.collegegestion.notifications.Token;
import com.arz.chech.collegegestion.preferences.Preferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivityGroup extends AppCompatActivity {
    private ArrayList<DatosUsuario>datosUsuarios;
    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef, reference;
    private CircleImageView mProfileImage;
    private String mCurrentUserId;
    //private ImageButton mChatAddBtn;
    private ImageButton btn_send;
    private EditText text_send;
    private RecyclerView mMessagesList;
    private final List<Messages> messagesList = new ArrayList<>();
    private ChatAdapterGroup mAdapter;
    private static final int GALLERY_PICK = 1;
    private TextView displayName;
    private String userid;
    private String userName;
    private String userApellido;
    private APIService apiService;
    private boolean notify = false;
    private DatabaseReference userRef,groupNameRef,groupmessageKeyRef;
    Boolean banderaNot;
    Boolean bann;
    private String nombreGrupo;
    private FirebaseAuth mAuth;
    private String currentUserID,currentGroupName,currentUserName,currentDate,currentTime;
    private TextView displayTextMessage;

    public static final String prefGlobant="collegegestion.shared";
    public static final String prefbandera="collegegestion.bande";


    // Storage Firebase
    private StorageReference mImageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_group);

        //datosUsuarios=getIntent().getParcelableArrayListExtra("datosUsuariosList");
        //nombreGrupo=getIntent().getStringExtra("nombreGrupo");
        //System.out.println("el valor es:"+datosUsuarios.toString());
        currentGroupName=getIntent().getStringExtra("nombreGrupo");
        System.out.println("El token de grupo pasado por intent es: "+ currentGroupName);

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
        mMessagesList = (RecyclerView) findViewById(R.id.recyclerViewChatGroup);
        mMessagesList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mAdapter = new ChatAdapterGroup(ChatActivityGroup.this, messagesList);
        mMessagesList.setLayoutManager(linearLayoutManager);
        mMessagesList.setAdapter(mAdapter);

        //
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrentUserId = Preferences.obtenerPreferenceString(ChatActivityGroup.this, Preferences.PREFERENCE_TOKEN);
        //mAuth= FirebaseAuth.getInstance();
        //mCurrentUserId=mAuth.getCurrentUser().getUid();
        userRef=mRootRef.child("Users");
        groupNameRef=mRootRef.child("Groups").child(currentGroupName);
        btn_send = (ImageButton) findViewById(R.id.chat_send_btn);
        text_send = (EditText) findViewById(R.id.chat_message_view);
        displayName = (TextView) findViewById(R.id.display_name);
        userid="LTNMHPisqBPa-6T-TZt";
        /*Intent intent = getIntent();
        userid = intent.getStringExtra("user_id");*/

        //

        /*for (final DatosUsuario m:datosUsuarios){
            userid=m.getToken();
            System.out.println("El nombre es: "+m.getNombre());
            System.out.println("El apellido es: "+m.getApellido());*/


        //------- IMAGE STORAGE ---------
       // mImageStorage = FirebaseStorage.getInstance().getReference();
        //mRootRef.child("Chat").child(mCurrentUserId).child(userid).child("seen").setValue(true);
        //mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);


        reference = FirebaseDatabase.getInstance().getReference("Groups").child(currentGroupName);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Grupo grupo = dataSnapshot.getValue(Grupo.class);
                    assert grupo != null;
                    nombreGrupo = grupo.getName();
                    displayName.setText(nombreGrupo);
                    System.out.println("el nombre del grupo es:  "+nombreGrupo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        System.out.println("el nooooombre es: "+currentGroupName);
        System.out.println("el nombre del grupo es:  "+nombreGrupo);


        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayName.setText(nombreGrupo);
                for (final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                    reference.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            DatosUsuario datosUsuario = dataSnapshot1.getValue(DatosUsuario.class);
                                            assert datosUsuario != null;
                                            userName = datosUsuario.getNombre();
                                            userApellido = datosUsuario.getApellido();
                                            Log.e("datos", "" + dataSnapshot1.getValue());
                                            Log.e("nombre usuario", "" + userName + " " + userApellido);
                                            System.out.println("El nombre es: " + userName + " " + userApellido);
                                            mAdapter.enviarDatos(userName, userApellido);
                                            System.out.println("los nombres son: " + userName + " " + userApellido);





                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/






        //readMessages();

       /* mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
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
        });*/



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



        //displayName.setText(nombreGrupo);
        GetUserInfo();
        //readMessages();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageInfoToDatabase();



            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        groupNameRef.child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    displayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    displayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayMessage(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();
        messagesList.clear();
        while (iterator.hasNext()){
            //DatosUsuario datosUsuario = dataSnapshot.getValue(DatosUsuario.class);
            //assert datosUsuario != null;
           /* String userApellido =(String) ((DataSnapshot)iterator.next()).getValue();
            Boolean estaEliminado = (Boolean) ((DataSnapshot)iterator.next()).getValue();
            String userName = (String) ((DataSnapshot)iterator.next()).getValue();
            String perfil = (String) ((DataSnapshot)iterator.next()).getValue();
            String rut = (String) ((DataSnapshot)iterator.next()).getValue();
            String token = (String) ((DataSnapshot)iterator.next()).getValue();
            mAdapter.enviarDatos(userName, userApellido);
            System.out.println("desde display:"+userName+" "+userApellido);*/

            String messages =(String) ((DataSnapshot)iterator.next()).getValue();
            Boolean seen = (Boolean) ((DataSnapshot)iterator.next()).getValue();
            String type = (String) ((DataSnapshot)iterator.next()).getValue();
            String time = (String) ((DataSnapshot)iterator.next()).getValue();
            String from = (String) ((DataSnapshot)iterator.next()).getValue();
            String receiver = (String) ((DataSnapshot)iterator.next()).getValue();



            Messages message = dataSnapshot.getValue(Messages.class);
            assert message !=null;
            messagesList.add(message);



            mAdapter.notifyDataSetChanged();
            mMessagesList.scrollToPosition(messagesList.size()-1);


        }

        //mAdapter.notifyDataSetChanged();
        //mMessagesList.scrollToPosition(messagesList.size()-1);

    }

    private void readMessages(){

        final DatabaseReference messageRef = mRootRef.child("Groups");

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){

                    messageRef.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(final DataSnapshot snapshot1:dataSnapshot.getChildren()){

                                Messages message = snapshot1.getValue(Messages.class);
                                assert message !=null;
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
            banderaNot=true;





        }else {
            Toast.makeText(ChatActivityGroup.this, "No puede enviar un mensaje vacío", Toast.LENGTH_SHORT).show();
        }

        final String msg = message.trim();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatosUsuario user = dataSnapshot.getValue(DatosUsuario.class);
                if (notify) {
                    banderaNot=true;
                    SharedPreferences ban = getApplication().getSharedPreferences(prefGlobant,MODE_PRIVATE);
                    SharedPreferences.Editor editor=ban.edit();
                    editor.putBoolean(prefbandera,banderaNot);
                    editor.apply();
                    System.out.println("bandera antes del mensaje:"+ban.getBoolean(prefbandera,false));


                    sendNotification(userid, user.getNombre() + " " + user.getApellido(), msg,banderaNot);

                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMessagesList.scrollToPosition(messagesList.size()-1);
    }

    private void sendNotification(String receiver, final String username, final String message,final Boolean banderaNot){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(mCurrentUserId, R.mipmap.ic_launcher, username+": "+message, "Nuevo Mensaje!", userid,banderaNot);

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


    private void GetUserInfo(){
        userRef.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("nombre").getValue(String.class) +" "+ dataSnapshot.child("apellido").getValue(String.class);
                    System.out.println("el usuario es: "+currentUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void SaveMessageInfoToDatabase() {
        String message = text_send.getText().toString();
        DatabaseReference mMensaje = groupNameRef.child("message");
        String messageKey =mMensaje.push().getKey();
        if(TextUtils.isEmpty(message)){
            Toast.makeText(this,"Por favor ingrese un mensaje",Toast.LENGTH_LONG).show();
        }
        else {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, YY");
            currentDate =currentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentDateTime = new SimpleDateFormat("hh:mm: a");
            currentTime =currentDateTime.format(calForTime.getTime());


            HashMap<String,Object> groupMessageKey=new HashMap<>();
            mMensaje.updateChildren(groupMessageKey);


            groupmessageKeyRef = mMensaje.child(messageKey);

            HashMap<String,Object> messageInfoMap = new HashMap<>();
                messageInfoMap.put("message",message.trim());
                messageInfoMap.put("seen", false);
                messageInfoMap.put("type", "text");
                messageInfoMap.put("time", ServerValue.TIMESTAMP);
                messageInfoMap.put("from",mCurrentUserId);
                //messageInfoMap.put("nombre",currentUserName);
                messageInfoMap.put("receiver", userid);
                //messageInfoMap.put("date",currentDate);
                //messageInfoMap.put("time",currentTime);
             groupmessageKeyRef.updateChildren(messageInfoMap);
            }
        text_send.setText("");


        mMessagesList.scrollToPosition(messagesList.size()-1);


    }
}