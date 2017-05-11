package com.mobilprog.tadam.mobilprog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobilprog.tadam.mobilprog.Firebase.MyFirebaseDataBase;
import com.mobilprog.tadam.mobilprog.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by T on 2017. 04. 30..
 */

public class AllPeopleListActivity extends AppCompatActivity {

    private String TAG = "Friends List Activity";

    private ListView mListView;
    private Toolbar mToolBar;
    private TextView usernameTextView;
    private TextView emailTextView;
    private SwitchCompat friendSwitch;
    private static Boolean isTouched = false;

    private FirebaseListAdapter mFriendListAdapter;
    private ValueEventListener mValueEventListener;

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

        mToolBar.setTitle("Find new friends");

        //showUserList();
    }

    private void initializeScreen() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mFirebaseAuth.getCurrentUser().getUid();
        //Eventually this list will filter out users that are already your friend
        mUserDatabaseReference = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.USER_DB);
        mCurrentUsersFriends = mFirebaseDatabase.getReference().child(MyFirebaseDataBase.FRIEND_DB
                + "/" + mFirebaseAuth.getCurrentUser().getUid());//mFirebaseAuth.getCurrentUser().getEmail());
        // userId alapján kellene friend adatbázist kezelni

        mListView = (ListView) findViewById(R.id.friendsListView);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);


        mFriendListAdapter = new FirebaseListAdapter<User>(this, User.class, R.layout.one_person_item, mUserDatabaseReference) {
            @Override
            protected void populateView(View view, final User model, final int position) {

                String userId = model.getUid();
                friendSwitch = (SwitchCompat) view.findViewById(R.id.friend_switch);
                //Check if this user is already your friend
                DatabaseReference friendsReference =
                        mFirebaseDatabase.getReference(MyFirebaseDataBase.FRIEND_DB
                                + "/" + mCurrentUserId);

                if (userId.equals(mCurrentUserId)) {
                    friendSwitch.setVisibility(View.GONE);
                }

                friendsReference.addValueEventListener(new ValueEventListener() {
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
                usernameTextView.setText(model.getUsername());
                emailTextView.setText(model.getEmail());
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

    private void addNewFriend(User newFriend) {
        //Get current user logged in by Uid
        String userLoggedIn = mFirebaseAuth.getCurrentUser().getUid();
        Log.e(TAG, "User logged in is: " + userLoggedIn);
        DatabaseReference friendsRefPartnerOne = mFirebaseDatabase.getReference(MyFirebaseDataBase.FRIEND_DB
                + "/" + userLoggedIn);
        //Add friends to current users friends list
        friendsRefPartnerOne.child(newFriend.getUid()).setValue(newFriend);

        // Add the currentUser to newFriend friends list
    }

    private void removeFriend(User newFriend) {
        //Get current user logged in by email
        String userLoggedIn = mFirebaseAuth.getCurrentUser().getUid();
        Log.e(TAG, "User logged in is: " + userLoggedIn);
        DatabaseReference friendsRef = mFirebaseDatabase.getReference(MyFirebaseDataBase.FRIEND_DB
                + "/" + userLoggedIn);
        friendsRef.child(newFriend.getUid()).removeValue();
    }

    private void showUserList() {
        mFriendListAdapter = new FirebaseListAdapter<User>(this, User.class, R.layout.one_person_item, mUserDatabaseReference) {
            @Override
            protected void populateView(final View view, User user, final int position) {
// TODO a név mindig null!!
    /*            TextView nameTextView = (TextView) view.findViewById(R.id.name);
                String name = user.getUsername();
                nameTextView.setText(name);

                    ImageView image = (ImageView) view.findViewById(R.id.image);

                    ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

                    int color1 = generator.getRandomColor();
                    TextDrawable drawable1 = TextDrawable.builder()
                            .beginConfig()
                            .withBorder(4)
                            .endConfig()
                            .buildRound(name.substring(0, 1), color1);

                    image.setImageDrawable(drawable1);
          */

                //Log.e("TAG", user.toString());
                final String email = user.getEmail();
                //Check if this user is already your friend
                final DatabaseReference friendRef =
                        mFirebaseDatabase.getReference(MyFirebaseDataBase.FRIEND_DB
                                + "/" + mCurrentUserId + "/" + email);

                if (email.equals(mCurrentUserId)) {
                    //view.findViewById(R.id.addFriend).setVisibility(View.GONE);
                    //view.findViewById(R.id.removeFriend).setVisibility(View.GONE);
                }

                friendRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Log.w(TAG, "User is friend");
                            //      view.findViewById(R.id.addFriend).setVisibility(View.GONE);
                            //      view.findViewById(R.id.removeFriend).setVisibility(View.VISIBLE);
                        } else {
                            Log.w(TAG, "User is not friend");
                            //    view.findViewById(R.id.removeFriend).setVisibility(View.GONE);
                            //    view.findViewById(R.id.addFriend).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ((TextView) view.findViewById(R.id.messageTextView)).setText(user.getUsername());
                ((TextView) view.findViewById(R.id.nameTextView)).setText(email);
/*                (view.findViewById(R.id.addFriend)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w(TAG, "Clicking row: " + position);
                        Log.w(TAG, "Clicking user: " + email);
                        //Add this user to your friends list, by email
                        addNewFriend(email);
                    }
                });
                (view.findViewById(R.id.removeFriend)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w(TAG, "Clicking row: " + position);
                        Log.w(TAG, "Clicking user: " + email);
                        //Add this user to your friends list, by email
                        removeFriend(email);
                    }
                });*/
            }
        };

        mListView.setAdapter(mFriendListAdapter);

        mValueEventListener = mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    finish();
                    return;
                }
                mFriendListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
