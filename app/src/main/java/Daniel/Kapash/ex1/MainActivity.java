package Daniel.Kapash.ex1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final String TEXT_VIEW_KEY = "1";
    TextView textView;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = findViewById(R.id.textView);
        this.editText = findViewById(R.id.editText);
        this.button = findViewById(R.id.button);

        textView.setOnClickListener(this);
        button.setOnClickListener(this);
        editText.setOnClickListener(this);

        textView.setMovementMethod(new ScrollingMovementMethod());

        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
        }
    }


    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.button:
                String message = editText.getText().toString();
                editText.setText("");
                Log.d("send Message Debug", "button!!!!!");
                if (!message.equals("")) {
                    String feed = textView.getText().toString();
                    feed = feed + "\n" + message;
                    textView.setText(feed);
                }
                break;

            case R.id.editText:
                Log.d("clicked", "editText!!");
                break;

            case R.id.textView:
                Log.d("clicked", "textView!!");
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TEXT_VIEW_KEY, textView.getText().toString());
        super.onSaveInstanceState(outState);
    }

}
