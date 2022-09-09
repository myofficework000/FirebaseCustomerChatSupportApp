package com.learning.firebasedemo.realtimedb.whatsapp_chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.learning.firebasedemo.R
import com.learning.firebasedemo.databinding.ReceiverViewBinding
import com.learning.firebasedemo.databinding.SenderViewBinding

class ChatAdapter(private val context: Context, private val chats: MutableList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var bindingSender: SenderViewBinding
    private lateinit var bindingReceiver: ReceiverViewBinding


    /*
     This method needs to override to define the size of recycler view*/
    override fun getItemCount() = chats.size

    /*
    This method is used when you have more than one view type in recyclerview of same list*/
    override fun getItemViewType(position: Int): Int {
        return chats[position].viewType
    }

    /*
    This method will call only once when you create adapter for recycler view.
    Responsibility of this method is to create new views.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == SENDER_VIEW) {
            bindingSender = SenderViewBinding.inflate(layoutInflater, parent, false)
            SenderViewHolder(bindingSender.root)
        } else {
            bindingReceiver = ReceiverViewBinding.inflate(layoutInflater, parent, false)
            ReceiverViewHolder(bindingReceiver.root)
        }
    }

    /*
    This method will call any number of times when you scroll up or down the list
    Responsibility of this method is to bind the new data with position of list*/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (chats[position].viewType == SENDER_VIEW) {
            (holder as SenderViewHolder).bind(position)
        } else {
            (holder as ReceiverViewHolder).bind(position)
        }

        holder.itemView.setOnLongClickListener {

            val builder = AlertDialog.Builder(context).apply {
                setIcon(R.drawable.ic_baseline_delete_24)
                setTitle(context.getString(R.string.delete))
                setMessage(context.getString(R.string.confirm_delete))
                setPositiveButton("Confirm") { dialog, _ ->
                    dialog.dismiss()
                    try {
                        if (position == chats.size) {
                            chats.removeAt(position - 1)
                            notifyItemRemoved(position - 1)
                        } else {
                            chats.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    } catch (e: Exception) {
                    }
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            }

            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()

            true
        }
    }

    inner class SenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtSender: TextView = bindingSender.txtSender
        fun bind(position: Int) {
            txtSender.text = chats[position].text
        }
    }

    inner class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtReceiver: TextView = bindingReceiver.txtReceiver
        fun bind(position: Int) {
            txtReceiver.text = chats[position].text
        }
    }

    companion object {
        const val SENDER_VIEW = 1
    }
}