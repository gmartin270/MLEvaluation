package com.gmartin.mlevaluation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmartin.mlevaluation.databinding.ProductItemBinding
import com.gmartin.mlevaluation.model.Product
import java.text.DecimalFormat

class ProductsListAdapter(
    private val mProductsList: List<Product>,
    private val mProductSelectedListener: ((Int) -> Unit)
) : RecyclerView.Adapter<ProductsListAdapter.ViewHolder>() {

    /**
     * @see [RecyclerView.Adapter.onCreateViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding, mProductSelectedListener)
    }

    /**
     * @see [RecyclerView.Adapter.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mProductsList[position])
    }

    /**
     * @see [RecyclerView.Adapter.getItemCount]
     */
    override fun getItemCount(): Int = mProductsList.size

    /**
     * @see [RecyclerView.ViewHolder]
     */
    inner class ViewHolder(
        private val itemBinding: ProductItemBinding,
        private val productSelectedListener: ((Int) -> Unit)
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * TODO
         */
        fun bind(product: Product) {
            itemBinding.productTitle.text = product.title
            itemBinding.productPrice.text = DecimalFormat("$ #.##").format(product.price)
            Glide.with(itemView.context).load(product.thumbnail).into(itemBinding.productThumbnail)

            itemBinding.root.setOnClickListener {
                productSelectedListener.invoke(layoutPosition)
            }
        }
    }
}
