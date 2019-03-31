package Daniel.Kapash.ex1;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final String STRING_MESSAGES_KEY = "String Messages";
    final String EMPTY_MESSAGE_ERROR = "Can't send empty message..";

    private ChatMessageRecyclerUtils.ChatMessageAdapter adapter
            = new ChatMessageRecyclerUtils.ChatMessageAdapter();

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    EditText editText;
    Button button;

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

        button.setOnClickListener(this);
        editText.setOnClickListener(this);


        if (savedInstanceState != null) {
            ArrayList<String> strMessages = savedInstanceState.getStringArrayList(STRING_MESSAGES_KEY);
            for (int i=0; i < strMessages.size(); i++){
                messages.add(new ChatMessage(strMessages.get(i)));
            }
        }

        ArrayList<ChatMessage> messagesCopy = new ArrayList<>(this.messages);
        adapter.submitList(messagesCopy);

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
                    ArrayList<ChatMessage> messagesCopy = new ArrayList<>(this.messages);
                    adapter.submitList(messagesCopy);
                }
                break;

            case R.id.editText:
                Log.d("clicked", "editText!!");
                break;

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<String> strMessages = new ArrayList<>();
        for (int i=0; i < messages.size(); i++){
            strMessages.add(messages.get(i).getText());
        }
        outState.putStringArrayList(STRING_MESSAGES_KEY, strMessages);
        super.onSaveInstanceState(outState);
    }

}
