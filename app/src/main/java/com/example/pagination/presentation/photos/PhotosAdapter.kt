package com.example.pagination.presentation.photos

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pagination.R
import com.example.pagination.domain.photos.Photo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_photo.*

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {
    private var photos = listOf<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    fun update(photos: List<Photo>) {
        val oldSize = this.photos.size
        this.photos = photos
        //список у нас только дополняется, поэтому нет смысла использовать diff utils
        notifyItemRangeInserted(oldSize, photos.size)
    }

    inner class PhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(photo: Photo) {
            title.text = photo.title
            image.setImageBitmap(null)

            Glide.with(containerView)
                .load(photo.thumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(image)
        }

    }
}