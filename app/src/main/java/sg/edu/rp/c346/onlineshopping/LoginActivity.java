package sg.edu.rp.c346.onlineshopping;

import Model.User;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText etphone, etpassword;
    Button loginbtn;
    ProgressDialog loadingBar;
    CheckBox cbRememberMe;
    TextView admin, notAdmin;

    private String parentDbName = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        admin = (TextView)findViewById(R.id.admin_panel_link);
        notAdmin = (TextView)findViewById(R.id.not_admin_panel_link);

        etphone = (EditText)findViewById(R.id.login_phone_input);
        etpassword = (EditText)findViewById(R.id.login_password_input);
        loginbtn = (Button)findViewById(R.id.login_btn);
        cbRememberMe = (CheckBox)findViewById(R.id.remember_me);
        loadingBar = new ProgressDialog(this);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("Admin Login");
                admin.setVisibility(View.INVISIBLE);
                notAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("Login");
                admin.setVisibility(View.VISIBLE);
                notAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });


        cbRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    SharedPreferences pref = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("remember","true");
                    editor.apply();

                }else if(!buttonView.isChecked()){
                    SharedPreferences pref = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("remember","false");
                    editor.apply();

                }

            }
        });


    }

    private void loginUser() {
        String phone = etphone.getText().toString();
        String password = etpassword.getText().toString();

        if(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {

            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateLogin(phone,password);

        }else{
            Toast.makeText(LoginActivity.this,"Please fill in all fields",Toast.LENGTH_LONG).show();
        }
    }

    private void validateLogin(final String phone, final String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){
                    User userdata = dataSnapshot.child(parentDbName).child(phone).getValue(User.class);

                    if (userdata.getPhone().equals(phone)){
                        if (userdata.getPassword().equals(password)){

                            SharedPreferences prefUser = getSharedPreferences("currentUser",MODE_PRIVATE);
                            SharedPreferences.Editor editorUser = prefUser.edit();
                            editorUser.putString("currentUser",userdata.getName());
                            editorUser.putString("currentUserPhone",userdata.getPhone());
                            editorUser.putString("currentUserAddress",userdata.getAddress());
                            editorUser.apply();

                            if (parentDbName.equals("Admins")){
                                Toast.makeText(LoginActivity.this,"Welcome admin, you logged in Successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);

                                SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("loginType","admin");
                                editor.putString("phone",phone);
                                editor.apply();

                            }else if (parentDbName.equals("Users")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);

                                SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("loginType","user");
                                editor.putString("phone",phone);
                                editor.apply();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this,"Password incorrect",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }


                }else{
                    Toast.makeText(LoginActivity.this,"Account with phone number " + phone +" does not exists",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
