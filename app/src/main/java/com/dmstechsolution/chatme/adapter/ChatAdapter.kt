package com.dmstechsolution.chatme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmstechsolution.chatme.R
import com.dmstechsolution.chatme.model.Chats
import com.dmstechsolution.chatme.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val context: Context, private var chatlist:ArrayList<Chats>):
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT=0
    private val MESSAGE_TYPE_Right=1
    var firebaseUser:FirebaseUser?=null

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val message=view.findViewById<TextView>(R.id.tmessage)
        val profile_image:CircleImageView=view.findViewById(R.id.profile)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        if (viewType==MESSAGE_TYPE_Right){
        val view= LayoutInflater.from(parent.context).inflate(R.layout.chat_right,parent,false)
        return ViewHolder(view)
    }else{
            val view= LayoutInflater.from(parent.context).inflate(R.layout.chat_left,parent,false)
            return ViewHolder(view)
        }
        }
    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {

        val chat=chatlist[position]
        holder.message.text=chat.message
            //photo using glide and cheek the user id and get from firebase

    }
    override fun getItemCount(): Int {
        return chatlist.size
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser=FirebaseAuth.getInstance().currentUser
        if (chatlist[position].senderid==firebaseUser!!.uid){
            return MESSAGE_TYPE_Right
        }
        else{
            return MESSAGE_TYPE_LEFT
        }
    }
}
