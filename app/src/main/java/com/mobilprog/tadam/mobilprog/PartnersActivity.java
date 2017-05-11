package com.mobilprog.tadam.mobilprog;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobilprog.tadam.mobilprog.Firebase.MyFirebaseDataBase;
import com.mobilprog.tadam.mobilprog.Model.Chat;
import com.mobilprog.tadam.mobilprog.Model.Message;
import com.mobilprog.tadam.mobilprog.Model.User;

import java.util.ArrayList;
import java.util.List;

public class PartnersActivity extends AppCompatActivity {

    private String[] optionsTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mChatDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ListView mChatListView;
    private FirebaseListAdapter mChatAdapter;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        optionsTitles = getResources().getStringArray(R.array.options_array);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        activityTitle = getTitle().toString();


       //ez itt egy tabos tutorialból van, innen:
        // https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                PartnersActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, optionsTitles));
        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menü");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        //getPartnersAfterSigned(user);
        getFriends(user);

    }


    private void getFriends(FirebaseUser user) {
        DatabaseReference friendsFirebaseDatabase = mFirebaseDatabase.getReference()
                .child(MyFirebaseDataBase.FRIEND_DB
                + "/" + user.getUid());

        /*
        mChatListView = (ListView) findViewById(R.id.partners_listview);
        mChatAdapter = new FirebaseListAdapter<User>(this, User.class, R.layout.listitem_partners, friendsFirebaseDatabase) {

            @Override
            protected void populateView(View view, User user, int position) {
                TextView nameTextView = (TextView) view.findViewById(R.id.name);
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
            }
        };

        mChatListView.setAdapter(mChatAdapter);
        */
    }

    private void getPartnersAfterSigned(FirebaseUser user) {

  /*      mChatDatabaseReference = mFirebaseDatabase.getReference()
                .child(MyFirebaseDataBase.USER_DB
                        + "/" + User.encodeEmail(user.getEmail()) + "/"
                        + MyFirebaseDataBase.CHAT_DB );
        mUserDatabaseReference = mFirebaseDatabase.getReference()
                .child(MyFirebaseDataBase.USER_DB); */

        mChatDatabaseReference = mFirebaseDatabase.getReference()
                .child(MyFirebaseDataBase.FRIEND_DB);
        //Initialize screen variables
        /*
        mChatListView = (ListView) findViewById(R.id.partners_listview);

        mChatAdapter = new FirebaseListAdapter<Chat>(this, Chat.class, R.layout.listitem_partners, mChatDatabaseReference) {
            @Override
            protected void populateView(final View view, Chat chat, final int position) {
                //Log.e("TAG", "");
                //final Friend addFriend = new Friend(chat);
                TextView nameTextView = (TextView) view.findViewById(R.id.name);
                String name = chat.getChatName();
                nameTextView.setText(name);

                // TODO: latest message
                // final TextView latestMessage = (TextView)view.findViewById(R.id.nameTextView);

                ImageView image = (ImageView) view.findViewById(R.id.image);

                ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

                int color1 = generator.getRandomColor();
                TextDrawable drawable1 = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .buildRound(name.substring(0, 1), color1);

                image.setImageDrawable(drawable1);
            }
        };

        mChatListView.setAdapter(mChatAdapter);
        //Add on click listener to line items
        mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String messageLocation = mChatAdapter.getRef(position).toString();

                if (messageLocation != null) {
                    Intent intent = new Intent(view.getContext(), MessagesActivity.class);
                    String messageKey = mChatAdapter.getRef(position).getKey();
                    intent.putExtra(MyFirebaseDataBase.MESSAGE_ID, messageKey);
                    Chat chatItem = (Chat) mChatAdapter.getItem(position);
                    intent.putExtra(MyFirebaseDataBase.CHAT_ID, chatItem.getChatName());
                    startActivity(intent);
                }

                //Log.e("TAG", mChatAdapter.getRef(position).toString());
            }
        });

        mValueEventListener = mChatDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                //Check if any chats exists
                if (chat == null) {
                    //finish();
                    return;
                }
                mChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        */
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            Intent newScreen = null;
            switch (position) {
                case 0:
                    break;
                case 1:
                    newScreen = new Intent(PartnersActivity.this, PartnersActivity.class);
                    break;
                case 2:
                    newScreen = new Intent(PartnersActivity.this, AllPeopleListActivity.class);
                    break;
                case 3:
                    FirebaseAuth.getInstance().signOut();
                    newScreen = new Intent(PartnersActivity.this, MainActivity.class);
                    break;

            }

            if (newScreen != null)
                startActivity(newScreen);

            drawerList.setItemChecked(position, true);
            setTitle(optionsTitles[position]);
            drawerLayout.closeDrawer(drawerList);
        }

    }
}
