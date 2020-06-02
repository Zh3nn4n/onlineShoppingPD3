package sg.edu.rp.c346.onlineshopping.ui.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import Model.Cart;
import ViewHolder.CartViewHolder;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.edu.rp.c346.onlineshopping.ConfirmFinalOrderActivity;
import sg.edu.rp.c346.onlineshopping.DetailsActivity;
import sg.edu.rp.c346.onlineshopping.HomeActivity;
import sg.edu.rp.c346.onlineshopping.R;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private RecyclerView recylerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;
    private TextView tvAmt;
    private int allTotalPrice = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel =
                ViewModelProviders.of(this).get(CartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        recylerView = root.findViewById(R.id.cart_list);
        tvAmt = root.findViewById(R.id.tvprice);
        recylerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recylerView.setLayoutManager(layoutManager);

        nextBtn = (Button)root.findViewById(R.id.next_process_btn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(allTotalPrice));
                startActivity(intent);
                getActivity().finish();
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone = pref.getString("phone","");

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(phone).child("Products"),
                        Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.tvProductQty.setText("Quantity: " + model.getQuantity());
                holder.tvProductPrice.setText("Price: "+model.getPrice());
                holder.tvProductName.setText(model.getPname());

                int oneProductTotalPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                allTotalPrice = allTotalPrice + oneProductTotalPrice;

                tvAmt.setText("Total Price: $" + String.valueOf(allTotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                            "Edit","Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if (which == 1){
                                    SharedPreferences pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                                    String phone = pref.getString("phone","");

                                    cartListRef.child("User View").child(phone)
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(getContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                            getActivity().recreate();
                                                        }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recylerView.setAdapter(adapter);
        adapter.startListening();

    }

}
