package com.example.keepintouch.homeSection

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.keepintouch.R

class HomeRecyclerViewAdapter(private val values: List<ContactItem>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

    class PriorityTitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.priority_title)
        fun bind(item: ContactItem.PriorityName) {
            title.text = when(item.priority) {
                0 -> "Top"
                1 -> "Family"
                2 -> "Friends"
                else -> "Others"
            }
        }
    }

    class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_name)
        val lastContact: TextView = view.findViewById(R.id.last_contact_info)
        val whatsappButton: Button = view.findViewById(R.id.whatsapp_link)
        fun bind(item: ContactItem.PersonItem) {
            name.text = item.person.name
            lastContact.text = "Contacted Recently"
            if (item.person?.contact?.isNotEmpty()!!) {
                whatsappButton.setOnClickListener {
                    val phone = item.person.contact
                    val url = "https://api.whatsapp.com/send?phone=$phone"
                    val openWhatsappIntent = Intent(Intent.ACTION_VIEW)
                    openWhatsappIntent.data = Uri.parse(url)
                    startActivity(itemView.context, openWhatsappIntent, null)
                }
            } else {
                whatsappButton.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener {view: View? ->
                view?.findNavController()?.navigate(
                    R.id.action_menu_item_home_to_menu_item_add_person,
                    bundleOf("id" to item.person.id)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (values[position]) {
            is ContactItem.PriorityName -> ITEM_VIEW_TYPE_HEADER
            is ContactItem.PersonItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ITEM_VIEW_TYPE_HEADER -> PriorityTitleViewHolder(parent.inflate(R.layout.contact_priority_title))
        ITEM_VIEW_TYPE_ITEM -> PersonViewHolder(parent.inflate(R.layout.contact_item))
        else -> PersonViewHolder(parent.inflate(R.layout.contact_item))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = values[holder.adapterPosition]) {
            is ContactItem.PersonItem -> (holder as PersonViewHolder).bind(item)
            is ContactItem.PriorityName -> (holder as PriorityTitleViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = values.size
}

