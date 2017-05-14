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
    private Toolbar mToolBar;
    private String currentUserEmail;
    private MessageAdapter mMessageListAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        User selectedUser = null;

        if (getIntent().getExtras() != null) {
            Bundle args = getIntent().getExtras();
            if (args.containsKey("selected_partner"))
                selectedUser = args.getParcelable("selected_partner");
        }

        if (selectedUser != null)
            setTitle(selectedUser.getUsername());


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

                Message sendingMessage = new Message(mFirebaseAuth.getCurrentUser().getEmail(), messagesToSend.getText().toString());
                mMessageDatabaseReference.push().setValue(sendingMessage);

                messagesToSend.setText("");
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    Message message = dataSnapshot.getValue(Message.class);
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


     /*   Intent intent = this.getIntent();
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
        addListeners();*/

 /*   public void addListeners() {
        messagesToSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
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

        String messageString = messagesToSend.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String timestamp = dateFormat.format(date);
        //Create message object with text/voice etc
        Message message = new Message(encodeEmail(mFirebaseAuth.getCurrentUser().getDisplayName()), messageString);
        //Create HashMap for Pushing
        HashMap<String, Object> messageItemMap = new HashMap<String, Object>();
        HashMap<String, Object> messageObj = (HashMap<String, Object>) new ObjectMapper()
                .convertValue(message, Map.class);
        messageItemMap.put("/" + pushKey, messageObj);
        mMessageDatabaseReference.updateChildren(messageItemMap)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        messagesToSend.setText("");
                    }
                });
    }


/*    private void showMessages() {
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
        messagesList.setAdapter(mMessageListAdapter);
    }*/

  /*  private void initializeScreen() {
        messagesList = (ListView) findViewById(R.id.messageListView);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        messagesToSend = (EditText) findViewById(R.id.messageToSend);
        sendButton = (ImageButton) findViewById(R.id.sendButton);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = encodeEmail(mFirebaseAuth.getCurrentUser().getEmail());
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.USER_DB);
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.MESSAGE_DB
                + "/");

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }/*
*/



