package com.app.remortapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.remortapp.Adapter.RecordingAdapter;
import com.app.remortapp.Model.Recording;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowRecActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecordingAdapter recordingsAdapter;
    private List<Recording> recordingList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_rec);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewRec);
        if (recyclerView == null) {
            Log.e("RecordingsActivity", "RecyclerView is null");
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            signInAnonymously();
        } else {
            fetchRecordingsFromFirebase();
        }

        // Initialize the list and adapter
        recordingList = new ArrayList<>();
        recordingsAdapter = new RecordingAdapter(recordingList);
        recyclerView.setAdapter(recordingsAdapter);

        // Fetch recordings from Firebase
        fetchRecordingsFromFirebase();
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in success
                fetchRecordingsFromFirebase();
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(ShowRecActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecordingsFromFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child("Voice");

        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    item.getMetadata().addOnSuccessListener(storageMetadata -> {
                        String name = storageMetadata.getName();
                        String date = storageMetadata.getUpdatedTimeMillis() + "";
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            String url = uri.toString();
                            Recording recording = new Recording(name, date, url);
                            recordingList.add(recording);
                            recordingsAdapter.notifyDataSetChanged();
                        });
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "Failed to list items", e);
            }
        });
    }
}