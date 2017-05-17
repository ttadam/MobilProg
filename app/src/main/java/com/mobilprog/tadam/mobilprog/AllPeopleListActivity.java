package com.mobilprog.tadam.mobilprog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobilprog.tadam.mobilprog.Firebase.MyFirebaseDataBase;
import com.mobilprog.tadam.mobilprog.Model.ThumbnailGenerator;
import com.mobilprog.tadam.mobilprog.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by T on 2017. 04. 30..
 */

public class AllPeopleListActivity extends AppCompatActivity {

    private String TAG = "Friends List Activity";

    private ListView mListView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private ImageView photoImageView;
    private SwitchCompat friendSwitch;

    private FirebaseListAdapter mFriendListAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mCurrentUsersFriends;
    private FirebaseAuth mFirebaseAuth;

    private final List<String> mUsersFriends = new ArrayList<>();
    private String mCurrentUserId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_people_list_activity);
        initializeScreen();
        showAppUserList();








    }

    private void initializeScreen() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mFirebaseAuth.getCurrentUser().getUid();
        //Eventually this list will filter out users that are already your friend
        mUserDatabaseReference = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.USER_DB);
        mCurrentUsersFriends = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.FRIEND_DB
                + "/" + mCurrentUserId);//mFirebaseAuth.getCurrentUser().getEmail());

        mListView = (ListView) findViewById(R.id.friendsListView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setTitle("Find new friends");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showAppUserList() {

        mFriendListAdapter = new FirebaseListAdapter<User>(this, User.class, R.layout.one_person_item, mUserDatabaseReference) {
            @Override
            protected void populateView(View view, final User model, final int position) {

                String userId = model.getUid();
                friendSwitch = (SwitchCompat) view.findViewById(R.id.friend_switch);

                if (userId.equals(mCurrentUserId)) {
                    friendSwitch.setVisibility(View.GONE);
                }

              mCurrentUsersFriends.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Log.d(TAG, "User is friend");
                            friendSwitch.setChecked(true);
                        } else {
                            Log.d(TAG, "User is not friend");
                            friendSwitch.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                usernameTextView = (TextView) view.findViewById(R.id.messageTextView);
                emailTextView = (TextView) view.findViewById(R.id.nameTextView);
                photoImageView = (ImageView) view.findViewById(R.id.photoImageView);

                usernameTextView.setText(model.getUsername());
                emailTextView.setText(model.getEmail());
                TextDrawable drawable = ThumbnailGenerator.generateMaterial(model.getUsername());
                photoImageView.setImageDrawable(drawable);
                Log.d("AllPeopleListActivity", "Username: " + model.getUsername());
                Log.d("AllPeopleListActivity", "User email: " + model.getEmail());

                friendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            Log.d(TAG, "Clicking row: " + position);
                            Log.d(TAG, "Clicking user: " + model.getUsername());
                            //Add this user to your friends list, by email
                            addNewFriend(model);
                        } else {
                            removeFriend(model);
                        }
                    }
                });

            }
        };
        mListView.setAdapter(mFriendListAdapter);

    }

    private void addNewFriend(User newFriend) {
        //Get current user logged in by Uid
        Log.e(TAG, "User logged in is: " + mCurrentUserId);
        //Add friends to current users friends list
        mCurrentUsersFriends.child(newFriend.getUid()).setValue(newFriend);

        // Add the currentUser to newFriend friends list
    }

    private void removeFriend(User newFriend) {
        //Get current user logged in by email
        Log.e(TAG, "User logged in is: " + mCurrentUserId);
        mCurrentUsersFriends.child(newFriend.getUid()).removeValue();
    }

}
