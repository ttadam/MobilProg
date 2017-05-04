package com.mobilprog.tadam.mobilprog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.mobilprog.tadam.mobilprog.Firebase.MyFirebaseDataBase;
import com.mobilprog.tadam.mobilprog.Model.Message;
import com.mobilprog.tadam.mobilprog.Model.Partners;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {
    private String messageId;
    private TextView mMessageField;
    private ImageButton mSendButton;
    private String chatName;
    private ListView mMessageList;
    private Toolbar mToolBar;
    private String currentUserEmail;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseListAdapter<Message> mMessageListAdapter;
    private FirebaseAuth mFirebaseAuth;


    private ValueEventListener mValueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Intent intent = this.getIntent();
        //MessageID is the location of the messages for this specific chat
        messageId = intent.getStringExtra(MyFirebaseDataBase.MESSAGE_ID);
        chatName = intent.getStringExtra(MyFirebaseDataBase.CHAT_ID);

        if (messageId == null) {
            finish(); // replace this.. nav user back to home
            return;
        }
//korabbi---------------------------------------------
        Partners selectedPartner = getIntent().getExtras().getParcelable("selected_partner");
        if (selectedPartner != null)
            setTitle(selectedPartner.getName());
//korabbi---------------------------------------------
        initializeScreen();
        mToolBar.setTitle(chatName);
        showMessages();
        addListeners();
    }

    public void addListeners() {
        mMessageField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    public void sendMessage(View view) {
        //final DatabaseReference messageRef = mFirebaseDatabase.getReference(Constants.MESSAGE_LOCATION);
        final DatabaseReference pushRef = mMessageDatabaseReference.push();
        final String pushKey = pushRef.getKey();

        String messageString = mMessageField.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String timestamp = dateFormat.format(date);
        //Create message object with text/voice etc
        Message message = new Message(encodeEmail(mFirebaseAuth.getCurrentUser().getEmail()), messageString, timestamp);
        //Create HashMap for Pushing
        HashMap<String, Object> messageItemMap = new HashMap<String, Object>();
        HashMap<String, Object> messageObj = (HashMap<String, Object>) new ObjectMapper()
                .convertValue(message, Map.class);
        messageItemMap.put("/" + pushKey, messageObj);
        mMessageDatabaseReference.updateChildren(messageItemMap)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mMessageField.setText("");
                    }
                });
    }


    private void showMessages() {
        mMessageListAdapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.one_message_item, mMessageDatabaseReference) {
            @Override
            protected void populateView(final View view, final Message message, final int position) {
                LinearLayout messageLine = (LinearLayout) view.findViewById(R.id.messageLine);
                TextView messgaeText = (TextView) view.findViewById(R.id.messageTextView);
                TextView senderText = (TextView) view.findViewById(R.id.senderTextView);
                //TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
                final ImageView leftImage = (ImageView) view.findViewById(R.id.leftMessagePic);
                final ImageView rightImage = (ImageView) view.findViewById(R.id.rightMessagePic);
                LinearLayout individMessageLayout = (LinearLayout) view.findViewById(R.id.individMessageLayout);

                //set message and sender text
                messgaeText.setText(message.getMessage());
                senderText.setText(message.getSender());
                //If you sent this message, right align
                String mSender = message.getSender();

                if (mSender.equals(currentUserEmail)) {
                    //messgaeText.setGravity(Gravity.RIGHT);
                    //senderText.setGravity(Gravity.RIGHT);
                    messageLine.setGravity(Gravity.RIGHT);
                    leftImage.setVisibility(View.GONE);
                    rightImage.setVisibility(View.VISIBLE);

                    individMessageLayout.setBackgroundResource(R.drawable.message_bubble_get);
                    //messgaeText.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                    //       R.color.colorAccent, null));
                } else if (mSender.equals("System")) {
                    messageLine.setGravity(Gravity.CENTER_HORIZONTAL);
                    leftImage.setVisibility(View.GONE);
                    rightImage.setVisibility(View.GONE);
                } else {
                    //messgaeText.setGravity(Gravity.LEFT);
                    //senderText.setGravity(Gravity.LEFT);
                    messageLine.setGravity(Gravity.LEFT);
                    leftImage.setVisibility(View.VISIBLE);
                    rightImage.setVisibility(View.GONE);
                    individMessageLayout.setBackgroundResource(R.drawable.message_bubble);
                    //messgaeText.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                    //       R.color.colorPrimary, null));
                }
            }
        };
        mMessageList.setAdapter(mMessageListAdapter);
    }

    private void initializeScreen() {
        mMessageList = (ListView) findViewById(R.id.messageListView);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mMessageField = (TextView) findViewById(R.id.messageToSend);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = encodeEmail(mFirebaseAuth.getCurrentUser().getEmail());
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.USER_DB);
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.MESSAGE_DB
                + "/" + messageId);

        mToolBar.setTitle(chatName);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //TODO: Used in multiple places, should probably move to its own class
    public String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}



