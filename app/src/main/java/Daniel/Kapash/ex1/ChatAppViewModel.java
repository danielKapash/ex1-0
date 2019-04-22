package Daniel.Kapash.ex1;

import android.arch.lifecycle.*;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatAppViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Executor executor = Executors.newSingleThreadExecutor();

    private MutableLiveData<ArrayList<ChatMessage>> messages;

    public LiveData<ArrayList<ChatMessage>> getUsers() {
        if (messages == null) {
            messages = new MutableLiveData<ArrayList<ChatMessage>>();
            loadMessages();
        }
        return messages;
    }

    private void loadMessages() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ChatMessage> messagesFromDB = new ArrayList<>();
                db.collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot snapshot : task.getResult()) {
                                messagesFromDB.add(snapshot.toObject(ChatMessage.class));
                            }
                        }
                        messages.setValue(messagesFromDB);
                    }
                });
            }
        });
    }
}

