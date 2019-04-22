// tag: v2

package Daniel.Kapash.ex1;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.*;

public class ChatApp extends Application
        implements AsyncTasksManager.DBMessagesDownloadListener {

    private final String SP_MESSAGES_JSON = "chat_messages";

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    private AsyncTasksManager asyncTasksManager = new AsyncTasksManager();

    @Override
    public void onCreate() {
        super.onCreate();


        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        readMessagesFromSP();

        asyncTasksManager.getMessagesFromDB(this);

        Log.d("messages count", "count: " + messages.size());
    }


    @Override
    public void onMessagesDownloadedFromDb(boolean success, ArrayList<ChatMessage> messagesFromDB) {
        if (success && (messagesFromDB != messages)){
            messages = new ArrayList<>(messagesFromDB);
            writeMessagesToSP();
        }
    }

    public ArrayList<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }


    public void deleteMessage(ChatMessage message) {
        messages.remove(message);
        writeMessagesToSP();
    }


    public void addMessage(ChatMessage message) {
        messages.add(message);
        writeMessagesToSP();
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
