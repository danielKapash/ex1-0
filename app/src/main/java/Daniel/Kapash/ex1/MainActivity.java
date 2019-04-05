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

    private final String EMPTY_MESSAGE_ERROR = "Can't send empty message..";
    private final String DELETE_MESSAGE_DIALOG_TEXT = "Delete message?";
    private final String DELETE_MESSAGE_DIALOG_CANCEL = "Cancel";
    private final String DELETE_MESSAGE_DIALOG_DELETE = "Delete";


    private ChatMessageRecyclerUtils.ChatMessageAdapter adapter
            = new ChatMessageRecyclerUtils.ChatMessageAdapter();

    EditText editText;
    Button button;

    AlertDialog deleteMessageDialog;

    ChatMessage longPressedMessageToDelete;

    ChatApp app;

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


        app = (ChatApp) getApplicationContext();

        adapter.submitList(app.getMessages());


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(DELETE_MESSAGE_DIALOG_TEXT);
        alertDialogBuilder.setNegativeButton(DELETE_MESSAGE_DIALOG_CANCEL,  this);
        alertDialogBuilder.setPositiveButton(DELETE_MESSAGE_DIALOG_DELETE,  this);
        deleteMessageDialog = alertDialogBuilder.create();

    }




    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.button:
                String messageText = editText.getText().toString();
                editText.setText("");
                Log.d("clicked", "button!!!!!");
                if (messageText.equals("")) {
                    Snackbar.make(editText, EMPTY_MESSAGE_ERROR, Snackbar.LENGTH_SHORT).show();
                } else {
                    app.addMessage(new ChatMessage(messageText));
                    adapter.submitList(app.getMessages());
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
            app.deleteMessage(longPressedMessageToDelete);
            longPressedMessageToDelete = null;
            adapter.submitList(app.getMessages());
        }
    }


}
