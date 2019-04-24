package Daniel.Kapash.ex1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DeleteMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_message);

        Intent intent = getIntent();
        String messageContent = intent.getStringExtra("text");
        String phoneInstanceID = "Phone Instance ID: " + intent.getStringExtra("instanceID");
        String timeStamp = intent.getStringExtra("timeStamp");
        ((TextView)findViewById(R.id.message_text_textView)).setText(messageContent);
        ((TextView)findViewById(R.id.instanceId_textView)).setText(phoneInstanceID);
        ((TextView)findViewById(R.id.timeStamp_textView)).setText(timeStamp);

        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("delete", true);
                setResult(RESULT_OK, data);
                finish();
            }
        });


    }
}
