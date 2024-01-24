package com.app.openinapp.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.openinapp.R
import com.app.openinapp.model.Links
import com.bumptech.glide.Glide
import javax.inject.Inject


class TopLinkAdapter @Inject constructor() : RecyclerView.Adapter<TopLinkAdapter.ViewHolder>()
{
    private var linklist : List<Links> = emptyList()

    fun setTopLinks(linklist : List<Links>)
    {
        this.linklist = linklist
        notifyDataSetChanged()
    }

     class ViewHolder (view: View): RecyclerView.ViewHolder(view) {

       val imgProfile: ImageView = view.findViewById(R.id.imgProfile)
       val txtLinkName:TextView = view.findViewById(R.id.txtLinkName)
       val txtdayago:TextView = view.findViewById(R.id.txtdayago)
       val txtclicks :TextView = view.findViewById(R.id.txtclicks)
         val txtLink :TextView = view.findViewById(R.id.txtLink)
         val copyurl :ImageView = view.findViewById(R.id.copyurl)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linkitem,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return linklist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val link = linklist[position]

        holder.txtLinkName.text = link.title
        holder.txtdayago.text = link.times_ago
        holder.txtclicks.text = link.total_clicks.toString()
        holder.txtLink.text = link.web_link

        if (link.original_image != null) {

            Glide.with(holder.imgProfile.context).load(link.original_image).into(holder.imgProfile)
        }else {

            Glide.with(holder.imgProfile.context)
                .load(R.drawable.defaultphoto)
                .into(holder.imgProfile);
        }

        // Set click listeners outside init block
        holder.txtLink.setOnClickListener {
            openWebsite(holder.txtLink.text.toString(), holder.itemView.context)
        }
        holder.copyurl.setOnClickListener {
            copyText(holder.txtLink.text.toString(), holder.itemView.context)
        }


    }

    private fun openWebsite(url: String, context: Context) {
        // Handle click on TextView to open the website
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    private fun copyText(text: String, context: Context) {
        // Handle click on ImageView to copy the text
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)

        // Optionally, provide feedback to the user (e.g., show a toast)
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }


}