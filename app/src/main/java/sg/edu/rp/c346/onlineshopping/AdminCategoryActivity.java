package sg.edu.rp.c346.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    ImageView tShirts, sportsTshirts, femaleDresses, sweaters;
    ImageView glasses, hatsCaps, walletBagsPurses, shoes;
    ImageView headphonesHandFree, laptopsPc, watches, mobilePhones;
    Button logoutbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tShirts = (ImageView)findViewById(R.id.t_shirts);
        sportsTshirts = (ImageView)findViewById(R.id.sports_t_shirts);
        femaleDresses = (ImageView)findViewById(R.id.female_dresses);
        sweaters = (ImageView)findViewById(R.id.sweeathers);

        glasses = (ImageView)findViewById(R.id.glasses);
        hatsCaps = (ImageView)findViewById(R.id.hats_caps);
        walletBagsPurses = (ImageView)findViewById(R.id.purses_bags_wallets);
        shoes = (ImageView)findViewById(R.id.shoes);

        headphonesHandFree = (ImageView)findViewById(R.id.headphones_handfree);
        laptopsPc = (ImageView)findViewById(R.id.laptops_pc);
        watches = (ImageView)findViewById(R.id.watches);
        mobilePhones = (ImageView)findViewById(R.id.mobilesphones);

        logoutbtn= (Button)findViewById(R.id.adminlogout);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("loginType","user");
                editor.apply();

                Intent intent = new Intent(AdminCategoryActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);
            }
        });

        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Sports TShirts");
                startActivity(intent);
            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });

        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Hats Caps");
                startActivity(intent);
            }
        });

        walletBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Wallets Bags Purses");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });

        headphonesHandFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","HeadPhones HandFree");
                startActivity(intent);
            }
        });

        laptopsPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Laptops Pc");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });

        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductsActivity.class);
                intent.putExtra("category","Mobile Phones");
                startActivity(intent);
            }
        });


    }
}
