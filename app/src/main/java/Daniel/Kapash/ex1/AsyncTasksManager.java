package Daniel.Kapash.ex1;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.support.constraint.Constraints.TAG;

public class AsyncTasksManager {
    interface DBMessagesDownloadListener {
        void onMessagesDownloadedFromDb(boolean success, ArrayList<ChatMessage> messagesFromDB);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Executor executor = Executors.newSingleThreadExecutor();
    DBMessagesDownloadListener listener;

    synchronized public void getMessagesFromDB(DBMessagesDownloadListener listener){
        this.listener = listener;
        downloadMessagesFromDB();
    }

    private void downloadMessagesFromDB() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<ChatMessage> messages = new ArrayList<>();
                boolean status = true;

                db.collection("messages").document("h").set(messages.get(index));


                // Add a new document with a generated ID
                db.collection("messages")
                        .add(messagesDict)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                listener.onMessagesDownloadedFromDb(status, messages);
            }
        });
    }
}
