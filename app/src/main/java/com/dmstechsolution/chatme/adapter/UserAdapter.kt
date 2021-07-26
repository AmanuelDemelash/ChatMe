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
import com.dmstechsolution.chatme.model.Users
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val context: Context,private var userlist:ArrayList<Users>):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val tusername=view.findViewById<TextView>(R.id.username)
        val desciption=view.findViewById<TextView>(R.id.desc)
        val profile_image:CircleImageView=view.findViewById(R.id.profile)
            val user_layout:LinearLayout=view.findViewById(R.id.userchat_item)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.rec_userchat,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user=userlist[position]
        holder.tusername.text=user.username
        Glide.with(context).load(user.profileimage).placeholder(R.drawable.profileimage).into(holder.profile_image)
        holder.user_layout.setOnClickListener{
            val bundle= bundleOf("uid" to user.userid)
            val activity=(context as AppCompatActivity)
            activity.supportFragmentManager.primaryNavigationFragment?.findNavController()
                ?.navigate(R.id.action_userChat_to_chat,bundle)
        }
    }
    override fun getItemCount(): Int {
        return userlist.size
    }
}
