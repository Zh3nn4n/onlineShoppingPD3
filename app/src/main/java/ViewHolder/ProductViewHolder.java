package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import Interface.ItemClickListener;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.edu.rp.c346.onlineshopping.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvProductname, tvProductdes, tvProductPrice;
    public ImageView imPoductimage;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imPoductimage = (ImageView) itemView.findViewById(R.id.product_image);
        tvProductname = (TextView) itemView.findViewById(R.id.product_name);
        tvProductdes = (TextView) itemView.findViewById(R.id.product_description);
        tvProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
