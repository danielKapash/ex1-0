package Daniel.Kapash.ex1;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.arch.lifecycle.*;
import android.widget.TextView;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ChatMessageRecyclerUtils.MessageLongClickCallBack {

    private static final int DELETE_REQUEST_CODE = 12434;
    private final String EMPTY_MESSAGE_ERROR = "Can't send empty message..";



    private ChatMessageRecyclerUtils.ChatMessageAdapter adapter
            = new ChatMessageRecyclerUtils.ChatMessageAdapter();

    EditText editText;
    Button button;
    TextView userNameTextView;

    ChatMessage longPressedMessageToDelete;

    ChatApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        userNameTextView = findViewById(R.id.user_name_text_view);

        String userName = getIntent().getStringExtra("userName");

        if (userName.equals(""))
            userNameTextView.setText("");
        else
            userNameTextView.setText("Hello " + userName + "!");

        RecyclerView recyclerView = findViewById(R.id.chat_message_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        adapter.callback = this;

        button.setOnClickListener(this);
        editText.setOnClickListener(this);


        app = (ChatApp) getApplicationContext();

        adapter.submitList(app.getMessages());


        MainActivityViewModel viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getMessages().observe(this, new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ChatMessage> dbMessages) {
                adapter.submitList(new ArrayList<>(dbMessages));
            }
        });


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
                    app.addMessage(new ChatMessage(messageText,app.getNextId()));
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
        Intent intent = new Intent(this, DeleteMessageActivity.class);
        intent.putExtra("text", message.getText());
        intent.putExtra("instanceID", message.getInstanceID());
        intent.putExtra("timeStamp", message.getTimeStamp());
        startActivityForResult(intent, DELETE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == DELETE_REQUEST_CODE) && (resultCode == RESULT_OK)) {
            if (data.getBooleanExtra("delete", false)) {
                app.deleteMessage(longPressedMessageToDelete);
                longPressedMessageToDelete = null;
            }
        }
    }


}
