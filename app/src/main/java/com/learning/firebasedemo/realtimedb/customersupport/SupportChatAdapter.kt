package com.learning.firebasedemo.realtimedb.customersupport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.learning.firebasedemo.R
import com.learning.firebasedemo.databinding.SenderViewBinding
import com.learning.firebasedemo.databinding.SupportChatViewBinding
import com.learning.firebasedemo.realtimedb.whatsapp_chat.Chat

class SupportChatAdapter(
    private val context: Context,
    private val chats: MutableList<Chat>,
    private val supportChatSenderData: SupportChatSenderData
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var supportChatViewBinding: SupportChatViewBinding
    private lateinit var bindingSender: SenderViewBinding

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
            supportChatViewBinding = SupportChatViewBinding.inflate(layoutInflater, parent, false)
            SenderViewHolder(supportChatViewBinding.root)
        } else {
            bindingSender = SenderViewBinding.inflate(layoutInflater, parent, false)
            ReceiverViewHolder(bindingSender.root)
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
        private val txtSender: TextView = supportChatViewBinding.txtSupport
        private val button1: Button = supportChatViewBinding.btnSupportMessage1
        private val button2: Button = supportChatViewBinding.btnSupportMessage2
        private val button3: Button = supportChatViewBinding.btnSupportMessage3

        fun bind(position: Int) {
            txtSender.text = chats[position].text
            button1.text = supportChatSenderData.button1
            button2.text = supportChatSenderData.button2
            button3.text = supportChatSenderData.button3
        }
    }

    inner class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtSender: TextView = bindingSender.txtSender
        fun bind(position: Int) {
            txtSender.text = chats[position].text
        }
    }

    companion object {
        const val SENDER_VIEW = 1
    }
}