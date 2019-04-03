package Daniel.Kapash.ex1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ChatMessageRecyclerUtils.MessageLongClickCallBack, DialogInterface.OnClickListener {

    private final String STRING_MESSAGES_KEY = "String Messages";
    private final String EMPTY_MESSAGE_ERROR = "Can't send empty message..";
    private final String SP_MESSAGES_JSON = "chat_messages";
    private final String DELETE_MESSAGE_DIALOG_TEXT = "Delete message?";
    private final String DELETE_MESSAGE_DIALOG_CANCEL = "Cancel";
    private final String DELETE_MESSAGE_DIALOG_DELETE = "Delete";


    private ChatMessageRecyclerUtils.ChatMessageAdapter adapter
            = new ChatMessageRecyclerUtils.ChatMessageAdapter();

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    EditText editText;
    Button button;

    AlertDialog deleteMessageDialog;

    ChatMessage longPressedMessageToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = findViewById(R.id.editText);
        this.button = findViewById(R.id.button);

        RecyclerView recyclerView = findViewById(R.id.chat_message_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        adapter.callback = this;

        button.setOnClickListener(this);
        editText.setOnClickListener(this);



//        if (savedInstanceState != null) {
//            ArrayList<String> strMessages = savedInstanceState.getStringArrayList(STRING_MESSAGES_KEY);
//            for (int i=0; i < strMessages.size(); i++){
//                messages.add(new ChatMessage(strMessages.get(i)));
//            }
//        }

        ArrayList<ChatMessage> tempMessages = getMessagesFromSP();
        if (tempMessages != null)
            messages = tempMessages;
        else
            messages = new ArrayList<>();

        Log.d("messages count", "count: " + messages.size());
        updateMessagesRecyclerViewAdapter(messages);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(DELETE_MESSAGE_DIALOG_TEXT);
        alertDialogBuilder.setNegativeButton(DELETE_MESSAGE_DIALOG_CANCEL,  this);
        alertDialogBuilder.setPositiveButton(DELETE_MESSAGE_DIALOG_DELETE,  this);
        deleteMessageDialog = alertDialogBuilder.create();

    }


    private void updateMessagesRecyclerViewAdapter(ArrayList<ChatMessage> messages) {

        ArrayList<ChatMessage> messagesCopy = new ArrayList<>(messages);
        adapter.submitList(messagesCopy);
    }


    private ArrayList<ChatMessage> getMessagesFromSP() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String messagesJson = sp.getString(SP_MESSAGES_JSON, "");
        Gson gson = new Gson();
        Type chatMessageType = new TypeToken<ArrayList<ChatMessage>>(){}.getType();
        return  gson.fromJson(messagesJson, chatMessageType);
    }


    private void saveMessagesToSP(ArrayList<ChatMessage> messages) {
        Gson gson = new Gson();
        String messagesJson = gson.toJson(messages);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_MESSAGES_JSON, messagesJson);
        editor.apply();
    }


    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.button:
                String message = editText.getText().toString();
                editText.setText("");
                Log.d("clicked", "button!!!!!");
                if (message.equals("")) {
                    Snackbar.make(editText, EMPTY_MESSAGE_ERROR, Snackbar.LENGTH_SHORT).show();
                } else {
                    messages.add(new ChatMessage(message));
                    saveMessagesToSP(messages);
                    updateMessagesRecyclerViewAdapter(messages);
                }
                break;

            case R.id.editText:
                Log.d("clicked", "editText!!");
                break;

        }
    }

    @Override
    public void onMessageLongClick(ChatMessage message) {
        longPressedMessageToDelete = message;
        deleteMessageDialog.show();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if ((which == DialogInterface.BUTTON_POSITIVE) && (longPressedMessageToDelete != null)) {
            messages.remove(longPressedMessageToDelete);
            longPressedMessageToDelete = null;
            saveMessagesToSP(messages);
            updateMessagesRecyclerViewAdapter(messages);
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        ArrayList<String> strMessages = new ArrayList<>();
//        for (int i=0; i < messages.size(); i++){
//            strMessages.add(messages.get(i).getText());
//        }
//        outState.putStringArrayList(STRING_MESSAGES_KEY, strMessages);
//        super.onSaveInstanceState(outState);
//    }


}
