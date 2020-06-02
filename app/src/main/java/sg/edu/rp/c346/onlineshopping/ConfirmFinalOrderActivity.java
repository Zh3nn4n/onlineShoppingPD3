package sg.edu.rp.c346.onlineshopping;

import Model.Products;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import sg.edu.rp.c346.onlineshopping.ui.cart.CartFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText etname, etPhone, etAddress, etCity;
    private Button confirmBtn;

    private String totalAmt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmt = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price: $"+totalAmt, Toast.LENGTH_SHORT).show();

        confirmBtn = (Button)findViewById(R.id.confirm_final_order);
        etname = (EditText)findViewById(R.id.shipment_name);
        etPhone = (EditText)findViewById(R.id.shipment_phone_number);
        etAddress = (EditText)findViewById(R.id.shipment_address);
        etCity = (EditText)findViewById(R.id.shipment_city);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   check();
            }
        });
    }

    private void check() {
        if (TextUtils.isEmpty(etname.getText().toString())){
            Toast.makeText(this, "Please provide name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(etPhone.getText().toString())){
            Toast.makeText(this, "Please provide phone number", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(etAddress.getText().toString())){
            Toast.makeText(this, "Please provide address", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(etCity.getText().toString())){
            Toast.makeText(this, "Please provide City name", Toast.LENGTH_SHORT).show();
        }else{
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        String saveCurrentTime, saveCurrentDate;

        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate =  currentDate.format(date.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =  currentTime.format(date.getTime());


        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String phone = pref.getString("phone","");
        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(phone).child(saveCurrentTime);
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmt",totalAmt);
        ordersMap.put("name",etname.getText().toString());
        ordersMap.put("phone",etPhone.getText().toString());
        ordersMap.put("address",etAddress.getText().toString());
        ordersMap.put("city",etCity.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("order","not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(phone).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your order has been placed successfully \n An sms will be sent to u shortly", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
        startActivity(intent);

    }
}
