package com.dmstechsolution.chatme.ui

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmstechsolution.chatme.R
import com.dmstechsolution.chatme.adapter.UserAdapter
import com.dmstechsolution.chatme.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class UserChat : Fragment() {

    private lateinit var profile_image:CircleImageView
   private var users=ArrayList<Users>()
    lateinit var recycler_view:RecyclerView
    lateinit var connectivityManager:ConnectivityManager
    lateinit var networkInfo: NetworkInfo
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_user_chat, container, false)

        profile_image=root.findViewById(R.id.profile)
        recycler_view=root.findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view.layoutManager= LinearLayoutManager(requireContext(),LinearLayout.VERTICAL,false)

        Getuser()

        root.findViewById<CircleImageView>(R.id.profile).setOnClickListener {
            findNavController().navigate(R.id.action_userChat_to_profile2)
        }
        return root
    }
    fun Getuser(){
        val firbase:FirebaseUser?=FirebaseAuth.getInstance().currentUser
        val databasereferanc:DatabaseReference=FirebaseDatabase.getInstance().getReference("User")
        databasereferanc.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                val currentuser=snapshot.getValue(Users::class.java)
                if (currentuser!!.profileimage==""){
                    profile_image.setImageResource(R.drawable.profileimage)
                }
                else{
                    Glide.with(requireActivity()).load(currentuser.profileimage).into(profile_image)
                }
                for (datasnapshot:DataSnapshot in snapshot.children ){
                    val user=datasnapshot.getValue(Users::class.java)
                    if (firbase != null) {
                        if (user!!.userid!=firbase.uid){
                            users.add(user)
                        }
                    }
                }
                val adapter=UserAdapter(requireContext(),users)
                recycler_view.adapter=adapter
            }
        })
    }
}