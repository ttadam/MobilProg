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
import com.mobilprog.tadam.mobilprog.Model.User;

/**
 * Created by thoma on 2017. 05. 14..
 */

public class FriendsFragment extends Fragment{

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private ListView mChatListView;
    private View view;
    private FirebaseListAdapter mChatAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_partners, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        getFriends(user);
    }

    private void getFriends(FirebaseUser user) {
        DatabaseReference friendsFirebaseDatabase = mFirebaseDatabase.getReference()
                .child(MyFirebaseDataBase.FRIEND_DB
                        + "/" + user.getUid());


        mChatListView = (ListView)view.findViewById(R.id.partners_listview);
        mChatAdapter = new FirebaseListAdapter<User>(getActivity(), User.class, R.layout.listitem_partners, friendsFirebaseDatabase) {

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

    }
}
