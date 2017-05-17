package com.mobilprog.tadam.mobilprog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mobilprog.tadam.mobilprog.Firebase.MyFirebaseDataBase;
import com.mobilprog.tadam.mobilprog.Model.Message;
import com.mobilprog.tadam.mobilprog.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private EditText messagesToSend;
    private ImageButton sendButton;
    private ListView messagesList;
    private String currentUserEmail;
    private MessageAdapter mMessageListAdapter;
    private User selectedUser;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);


        selectedUser = null;

        if (getIntent().getExtras() != null) {
            Bundle args = getIntent().getExtras();
            if (args.containsKey("selected_partner"))
                selectedUser = args.getParcelable("selected_partner");
        }

            if (selectedUser != null){

            toolbar.setTitle(selectedUser.getUsername());
        }




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.MESSAGE_DB);
        mFirebaseAuth = FirebaseAuth.getInstance();

        messagesList = (ListView) findViewById(R.id.messageListView);
        List<Message> messages = new ArrayList<>();
        mMessageListAdapter = new MessageAdapter(this, R.layout.one_message_item, messages);
        messagesList.setAdapter(mMessageListAdapter);
        messagesToSend = (EditText) findViewById(R.id.messageToSend);
        sendButton = (ImageButton) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message sendingMessage = new Message(null, mFirebaseAuth.getCurrentUser().getEmail(),
                        messagesToSend.getText().toString(), selectedUser.getEmail());
                mMessageDatabaseReference.push().setValue(sendingMessage);

                messagesToSend.setText("");
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    Message message = dataSnapshot.getValue(Message.class);

                    if (message.getReceiver().equals(selectedUser.getEmail()) &&
                            message.getSender().equals(mFirebaseAuth.getCurrentUser().getEmail())
                            ||
                            message.getReceiver().equals(mFirebaseAuth.getCurrentUser().getEmail()) &&
                            message.getSender().equals(selectedUser.getEmail()))
                        mMessageListAdapter.add(message);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mMessageDatabaseReference.addChildEventListener(mChildEventListener);
    }
}



