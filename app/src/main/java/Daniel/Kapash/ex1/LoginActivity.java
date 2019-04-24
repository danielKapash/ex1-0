package Daniel.Kapash.ex1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    Executor executor = Executors.newSingleThreadExecutor();

    DocumentReference userDataDocRef = FirebaseFirestore.getInstance().collection("meta data").document("User Data");

    private static final String EMPTY_USER_NAME_ERROR = "Name is empty..";

    private EditText editText;
    private Button skipButton;
    private Button approveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // intentionally on main thread:
        userDataDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().getString("userName");
                    Log.d("Login", "user name from db: " + userName);

                    if (!userName.equals(""))
                        startMainActivityWithUserName(userName);
                    else
                        buildLoginUI();
                }
            }
        });
    }

    private void buildLoginUI() {
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.enter_name_edit_text);
        approveButton = findViewById(R.id.approve_button);
        skipButton = findViewById(R.id.skip_button);

        approveButton.setEnabled(false);

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnThisIsMyNameClick();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivityWithUserName("");
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    approveButton.setEnabled(false);
                else
                    approveButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void startMainActivityWithUserName(String userName) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    private void OnThisIsMyNameClick() {
        EditText editText = findViewById(R.id.enter_name_edit_text);
        String userName = editText.getText().toString();
        if (userName.equals("")) {
            Snackbar.make(editText, EMPTY_USER_NAME_ERROR, Snackbar.LENGTH_SHORT).show();
        } else {
            setUserNameInDB(userName);
            startMainActivityWithUserName(userName);
        }
    }

    private void setUserNameInDB(final String userName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> userNameMap = new HashMap<>();
                userNameMap.put("userName", userName);
                CollectionReference metaDataRef = FirebaseFirestore.getInstance().collection("meta data");
                metaDataRef.document("User Data").set(userNameMap)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("DB", "Failed to update user name");
                            }
                        });
            }
        });
    }


}
