package sg.edu.rp.c346.onlineshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import Model.User;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public settingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static settingsFragment newInstance(String param1, String param2) {
        settingsFragment fragment = new settingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    String name, phone, address ;
    static final int GalleryPick = 1;
    CircleImageView profileImageView;
    EditText etFullname,etPhone,etAddress;
    TextView changeProfile;
    Button btnsave, btnLogout;
    Uri ImageUri;

    DatabaseReference ProductRef, rootRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);
        profileImageView = root.findViewById(R.id.settings_profile_image);
        etFullname = root.findViewById(R.id.settings_fullname);
        etPhone = root.findViewById(R.id.settings_phone_number);
        etAddress = root.findViewById(R.id.settings_address);
        changeProfile = root.findViewById(R.id.profile_image_change);


        btnsave = root.findViewById(R.id.settings_save);
        btnLogout = root.findViewById(R.id.settings_logout);
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Users");

        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone = pref.getString("phone","");
                User userdata = dataSnapshot.child("Users").child(phone).getValue(User.class);
                etPhone.setText(userdata.getPhone());
                etFullname.setText(userdata.getName());
                etAddress.setText(userdata.getAddress());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("loginType","user");
                editor.apply();

                SharedPreferences rpref = getActivity().getSharedPreferences("checkbox",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = rpref.edit();
                editor2.putString("remember","false");
                editor2.apply();

                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = etFullname.getText().toString();
                phone = etPhone.getText().toString();
                address = etAddress.getText().toString();

                if (TextUtils.isEmpty(name))
                {
                    Toast.makeText(getContext(), "Please write name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phone))
                {
                    Toast.makeText(getContext(), "Please write phone", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(address))
                {
                    Toast.makeText(getContext(), "Please write address", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    String phone = pref.getString("phone","");

                    rootRef.child("Users").child(phone).child("name").setValue(name);
                    rootRef.child("Users").child(phone).child("phone").setValue(phone);
                    rootRef.child("Users").child(phone).child("address").setValue(address);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name",name);
                    editor.putString("phone",phone);
                    editor.putString("address",address);
                    editor.apply();

                    Toast.makeText(getContext(), "User information updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(getContext(), "User information updated", Toast.LENGTH_SHORT).show();
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
        };

    });

        final FloatingActionButton fab = root.findViewById(R.id.button_add_address);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAddress.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            profileImageView.setImageURI(ImageUri);
        }
    }




}
