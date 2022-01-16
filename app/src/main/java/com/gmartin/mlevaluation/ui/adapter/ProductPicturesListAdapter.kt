package com.gmartin.mlevaluation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.gmartin.mlevaluation.databinding.PictureItemBinding
import com.gmartin.mlevaluation.model.Picture

/**
 * A [RecyclerView.Adapter] adapter class for list of [Picture] type.
 *
 * @author Guillermo O. Martín
 */
class ProductPicturesListAdapter(
    private val mProductPicturesList: List<Picture>
) : RecyclerView.Adapter<ProductPicturesListAdapter.ViewHolder>() {
    /**
     * @see [RecyclerView.Adapter.onCreateViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = PictureItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    /**
     * @see [RecyclerView.Adapter.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mProductPicturesList[position])
    }

    /**
     * @see [RecyclerView.Adapter.getItemCount]
     */
    override fun getItemCount(): Int = mProductPicturesList.size

    /**
     * @see [RecyclerView.ViewHolder]
     */
    inner class ViewHolder(
        private val itemBinding: PictureItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Binds the [ViewHolder] with an item in the list.
         *
         * @param picture A [Picture] instance element.
         */
        fun bind(picture: Picture) {
            Glide.with(itemView.context)
                .load(picture.url)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(itemBinding.picture)
        }
    }
}
