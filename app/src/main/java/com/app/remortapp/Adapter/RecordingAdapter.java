package com.app.remortapp.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.remortapp.Firebase.FirebaseDatabaseHelper;
import com.app.remortapp.Model.Recording;
import com.app.remortapp.R;

import java.util.List;
public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder> {

    private List<Recording> recordingList;

    public RecordingAdapter(List<Recording> recordingList) {
        this.recordingList = recordingList;
    }

    @NonNull
    @Override
    public RecordingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_item, parent, false);
        return new RecordingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingViewHolder holder, int position) {
        Recording recording = recordingList.get(position);
        holder.textRecordingName.setText(recording.getName());
        holder.textRecordingDate.setText(recording.getDate());

        holder.buttonDownload.setOnClickListener(v -> {
            FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
            dbHelper.downloadRecordingFile(recording.getFileName(), new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<Recording> recordings, List<String> keys) {}

                @Override
                public void DataIsInserted() {}

                @Override
                public void DataIsUpdated() {}

                @Override
                public void DataIsDeleted() {}
            });
        });
    }

    @Override
    public int getItemCount() {
        return recordingList.size();
    }

    public static class RecordingViewHolder extends RecyclerView.ViewHolder {
        ImageView iconRecording;
        TextView textRecordingName, textRecordingDate;
        Button buttonDownload;

        public RecordingViewHolder(@NonNull View itemView) {
            super(itemView);
            iconRecording = itemView.findViewById(R.id.iconRecording);
            textRecordingName = itemView.findViewById(R.id.textRecordingName);
            textRecordingDate = itemView.findViewById(R.id.textRecordingDate);
            buttonDownload = itemView.findViewById(R.id.buttonDownload);
        }
    }
}
