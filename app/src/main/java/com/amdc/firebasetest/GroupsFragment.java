package com.amdc.firebasetest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GroupsFragment extends Fragment {
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    private DatabaseReference GroupRef;

    public GroupsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);

        ListView list_view = groupFragmentView.findViewById(R.id.list_view);
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups"); // list groups
        arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);
        RetrieveAndDisplayGroups();
        list_view.setOnItemClickListener((adapterView, view, position, l) -> {
            String currentGroupName = adapterView.getItemAtPosition(position).toString();
            Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
            groupChatIntent.putExtra("groupName" , currentGroupName);
            startActivity(groupChatIntent);
        });
        return groupFragmentView;
    }

    private void RetrieveAndDisplayGroups() {  // retrieve list off groups from base
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    set.add(snapshot.getKey());
                }
                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
