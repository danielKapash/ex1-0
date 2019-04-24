// tag: v2

package Daniel.Kapash.ex1;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

public class ChatApp extends Application {

    private final String SP_MESSAGES_JSON = "chat_messages";

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    private Long nextId;

    private CollectionReference messagesDbRef;

    private CollectionReference metaDataRef;

    Executor executor = Executors.newSingleThreadExecutor();


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());

        messagesDbRef = FirebaseFirestore.getInstance().collection("messages");
        metaDataRef = FirebaseFirestore.getInstance().collection("meta data");

        getNextIdFromDB();

        readMessagesFromSP();

        updateMessagesFromDB();

        Log.d("messages count", "count: " + messages.size());
    }


    private void getNextIdFromDB() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                metaDataRef.document("nextChatMessageID").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    nextId = documentSnapshot.getLong("next_id");
                                    Log.d("DB", "next id: " + nextId);
                                }
                            }
                        });
            }
        });
    }


    private void updateMessagesFromDB() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                messagesDbRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<ChatMessage> messagesFromDB = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot document : task.getResult()) {
                                messagesFromDB.add(document.toObject(ChatMessage.class));
                            }
                            messages = new ArrayList<>(messagesFromDB);
                            writeMessagesToSP();
                        }
                    }
                });
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                messagesDbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        ArrayList<ChatMessage> messagesFromDB = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            messagesFromDB.add(document.toObject(ChatMessage.class));
                        }
                        messages = new ArrayList<>(messagesFromDB);
                        writeMessagesToSP();
                    }
                });
            }
        });

    }


    public ArrayList<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    public Long getNextId(){
        return nextId;
    }



    public void deleteMessage(ChatMessage message) {
        messagesDbRef.document(message.getId().toString()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DB", "Message successfully deleted from DB!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DB", "Error deleting message from DB", e);
                    }
                });
    }


    public void addMessage(final ChatMessage message) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                messagesDbRef.document(message.getId().toString()).set(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateNextId();
                                Log.d("DB", "successfully added message to db.");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("DB", "Error: fail to add message to db.");
                        }
                });
            }
        });
    }


    private void updateNextId() {
        nextId += 1;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Long> nextIdMap = new HashMap<>();
                nextIdMap.put("next_id", nextId);
                metaDataRef.document("nextChatMessageID").set(nextIdMap)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("DB", "Failed to update next id field");
                            }
                        });
            }
        });
    }


    private void readMessagesFromSP() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String messagesJson = sp.getString(SP_MESSAGES_JSON, "");
        Gson gson = new Gson();
        Type chatMessageType = new TypeToken<ArrayList<ChatMessage>>(){}.getType();
        ArrayList<ChatMessage> tempMessages = gson.fromJson(messagesJson, chatMessageType);
        if (tempMessages != null)
            messages = tempMessages;
        else
            messages = new ArrayList<>();
    }


    private void writeMessagesToSP() {
        Gson gson = new Gson();
        String messagesJson = gson.toJson(messages);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_MESSAGES_JSON, messagesJson);
        editor.apply();
    }

}
