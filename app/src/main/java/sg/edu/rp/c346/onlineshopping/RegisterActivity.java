package sg.edu.rp.c346.onlineshopping;

import Model.User;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText etusername, etphone, etpwd;
    Button createbtn;
    ProgressDialog loadingBar;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etusername = (EditText)findViewById(R.id.register_username_input);
        etphone = (EditText)findViewById(R.id.register_phone_number_input);
        etpwd = (EditText)findViewById(R.id.register_password_input);
        createbtn = (Button)findViewById(R.id.register_btn);
        loadingBar = new ProgressDialog(this);
        user = new User();
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String username = etusername.getText().toString();
        String phone = etphone.getText().toString();
        String password = etpwd.getText().toString();

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(username,phone,password);
        }else{
            Toast.makeText(RegisterActivity.this,"Please fill in all fields",Toast.LENGTH_LONG).show();
        }
    }

    private void ValidatePhoneNumber(final String username, final String phone, final String password) {
    final DatabaseReference rootRef;
    rootRef = FirebaseDatabase.getInstance().getReference();

    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if ((!dataSnapshot.child("Users").child(phone).exists())){
                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("name",username);
                userdataMap.put("phone",phone);
                userdataMap.put("password",password);


                rootRef.child("Users").child(phone).updateChildren(userdataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Account created",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(RegisterActivity.this, "Network Error: Please try again",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });



            }else{
                Toast.makeText(RegisterActivity.this, phone + " already exists",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                Toast.makeText(RegisterActivity.this, "Try again with another number",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}
