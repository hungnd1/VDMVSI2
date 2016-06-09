package hao.bk.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.chat.ProductDetailActivity;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.models.DeliveryObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.ProductObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.vdmvsi.FragmentDelivery;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/23/2016.
 */
public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {

    ArrayList<ProductObj> listProducts;
    FragmentDelivery frmContainer;
    Context context;
    ToastUtil toastUtil;

    public ProductItemAdapter(FragmentDelivery frmContainer, ArrayList<ProductObj> listProducts) {
        this.listProducts = listProducts;
        this.frmContainer = frmContainer;
        this.context = frmContainer.getContext();
        toastUtil = frmContainer.toastUtil;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivProduct.setImageResource(R.drawable.nom);
        ProductObj obj = listProducts.get(position);
        holder.tvProductCompany.setText(obj.getCompany());
       // holder.tvProductName.setText(obj.getName());
        holder.index = position;
        //holder.tvDescription.setText(obj.getDescription());
    }

    @Override
    public int getItemCount() {
        if (listProducts != null)
            return listProducts.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvProductName;
        TextView tvProductCompany;
        ImageView ivProduct;
        int index;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_news);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    context.startActivity(intent);
                }
            });
            tvProductCompany = (TextView) itemView.findViewById(R.id.tv_product_company);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            ivProduct = (ImageView) itemView.findViewById(R.id.img_product);
        }
    }
}
