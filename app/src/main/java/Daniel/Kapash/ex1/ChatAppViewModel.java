package Daniel.Kapash.ex1;

import android.arch.lifecycle.*;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

public class ChatAppViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Executor executor = Executors.newSingleThreadExecutor();

    private MutableLiveData<ArrayList<ChatMessage>> messages;

    public LiveData<ArrayList<ChatMessage>> getUsers() {
        if (messages == null) {
            messages = new MutableLiveData<>();
            loadMessages();
        }
        return messages;
    }

    private void loadMessages() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<ChatMessage> messagesFromDB = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot document : task.getResult()) {
                                messagesFromDB.add(document.toObject(ChatMessage.class));
                            }
                            messages.setValue(messagesFromDB);
                        }
                    }
                });
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        ArrayList<ChatMessage> messagesFromDB = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            messagesFromDB.add(document.toObject(ChatMessage.class));
                        }
                        messages.setValue(messagesFromDB);
                    }
                });
            }
        });
    }
}

