package com.pcandroiddev.newsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pcandroiddev.newsapp.R
import com.pcandroiddev.newsapp.models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {

            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = convertToDateAndTime(article.publishedAt)
            setOnClickListener {
            onItemClickListener?.let { it(article) }
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener : (Article) -> Unit){
        onItemClickListener = listener
    }

    @SuppressLint("NewApi")
    private fun convertToDateAndTime(timestamp: String?): String {
        val dateTime = LocalDateTime.parse(timestamp?.subSequence(0,19))
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        return dateTime.format(formatter)

    }

}