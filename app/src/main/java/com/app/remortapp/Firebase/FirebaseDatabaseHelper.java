package com.app.remortapp.Firebase;
import com.app.remortapp.Model.Recording;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public interface DataStatus {
        void DataIsLoaded(List<Recording> recordings, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void readRecordings(final DataStatus dataStatus) {
        db.collection("Voice")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Recording> recordings = new ArrayList<>();
                        List<String> keys = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recording recording = document.toObject(Recording.class);
                            keys.add(document.getId());
                            recordings.add(recording);
                        }
                        dataStatus.DataIsLoaded(recordings, keys);
                    } else {
                        // Handle error
                    }
                });
    }

    public void downloadRecordingFile(String fileName, final DataStatus dataStatus) {
        StorageReference storageRef = storage.getReference().child("recordings/" + fileName);
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Do something with the download URL
        }).addOnFailureListener(exception -> {
            // Handle error
        });
    }
}

