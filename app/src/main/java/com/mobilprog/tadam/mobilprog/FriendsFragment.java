package com.mobilprog.tadam.mobilprog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobilprog.tadam.mobilprog.Firebase.MyFirebaseDataBase;
import com.mobilprog.tadam.mobilprog.Model.ThumbnailGenerator;
import com.mobilprog.tadam.mobilprog.Model.User;

/**
 * Created by thoma on 2017. 05. 14..
 */

public class FriendsFragment extends Fragment{

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private ListView mChatListView;
    private FirebaseListAdapter mChatAdapter;

    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_partners, container, false);

        mChatListView = (ListView)rootView.findViewById(R.id.partners_listview);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        getFriends(user);
        return rootView;
    }


    private void getFriends(FirebaseUser user) {
        DatabaseReference friendsFirebaseDatabase = mFirebaseDatabase.getReference()
                .child(MyFirebaseDataBase.FRIEND_DB
                        + "/" + user.getUid());

        mChatAdapter = new FirebaseListAdapter<User>(getActivity(), User.class, R.layout.listitem_partners, friendsFirebaseDatabase) {

            @Override
            protected void populateView(View view, User user, int position) {
                TextView nameTextView = (TextView) view.findViewById(R.id.name);
                String name = user.getUsername();
                nameTextView.setText(name);

                ImageView image = (ImageView) view.findViewById(R.id.image);

                TextDrawable drawable = ThumbnailGenerator.generateMaterial(name);

                image.setImageDrawable(drawable);
            }
        };

        mChatListView.setAdapter(mChatAdapter);

    }
}
