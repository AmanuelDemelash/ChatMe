package com.dmstechsolution.chatme.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmstechsolution.chatme.MainActivity
import com.dmstechsolution.chatme.R
import com.dmstechsolution.chatme.adapter.ChatAdapter
import com.dmstechsolution.chatme.adapter.UserAdapter
import com.dmstechsolution.chatme.model.Chats
import com.dmstechsolution.chatme.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView


class Chat : Fragment() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private var chatslist=ArrayList<Chats>()
    lateinit var chatrecycler_view:RecyclerView
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_chat, container, false)
        chatrecycler_view=root.findViewById<RecyclerView>(R.id.chatrecycler_view)
        chatrecycler_view.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayout.VERTICAL,false)

        val username=root.findViewById<TextView>(R.id.chat_username)
        val user_image=root.findViewById<CircleImageView>(R.id.profile)
        val mess=root.findViewById<EditText>(R.id.text_message)
        val back=root.findViewById<ImageView>(R.id.back).setOnClickListener {
           findNavController().navigate(R.id.action_chat_to_userChat)
        }
        val value=arguments?.getString("uid")


        firebaseUser= FirebaseAuth.getInstance().currentUser!!
        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(value.toString())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(Users::class.java)
                if (user != null) {
                    username.text= user.username
                }
                if (user != null) {
                    if (user.profileimage==""){
                        user_image.setImageResource(R.drawable.profileimage)
                    }else{
                        Glide.with(requireContext()).load(user.profileimage).into(user_image)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        val send=root.findViewById<ImageView>(R.id.send_mail).setOnClickListener {
            if (TextUtils.isEmpty(mess.text)){
                Toast.makeText(requireContext(),"no message yet", Toast.LENGTH_SHORT).show()
            }else{
                sendmessage(firebaseUser.uid, value.toString(),mess.text.toString())
                mess.setText("")
            }
        }
        readmessage(firebaseUser.uid,value.toString())


        return root
    }
    private fun sendmessage(senderid:String,reciverid:String,message:String){
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderid",senderid)
        hashMap.put("reciverid",reciverid)
        hashMap.put("message",message)
        databaseReference.child("Chat").push().setValue(hashMap)
    }
    private fun readmessage(senderid: String,reciverid: String){
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chat")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot:DataSnapshot in snapshot.children ){
                    val chat=datasnapshot.getValue(Chats::class.java)
                    if (chat!!.senderid.equals(senderid) && chat.reciverid.equals(reciverid) ||
                        chat!!.senderid.equals(reciverid) && chat.reciverid.equals(senderid)) {
                            chatslist.add(chat)
                        }
                }
                val adapter=ChatAdapter(requireContext(),chatslist)
                chatrecycler_view.adapter=adapter
                }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}