package com.dmstechsolution.chatme.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dmstechsolution.chatme.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : Fragment() {

    private lateinit var auth:FirebaseAuth
    private  var firebaseUser: FirebaseUser?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_login, container, false)

        auth= FirebaseAuth.getInstance()
        firebaseUser= auth.currentUser
        if (firebaseUser!=null) {
            findNavController().navigate(R.id.action_login_to_userChat)
        }
        var signup=root.findViewById<TextView>(R.id.signup_text).setOnClickListener {
            val perferance=requireActivity().getSharedPreferences("signup", Context.MODE_PRIVATE)
            val is_sign=perferance.getBoolean("signed",false)
            if (is_sign){
                Toast.makeText(requireContext(),"You are Signed up",Toast.LENGTH_SHORT).show()
            }
            else{
                root.findNavController().navigate(R.id.action_login_to_signup)
            }
        }

        val login_button=root.findViewById<Button>(R.id.login)

        login_button.setOnClickListener {
            val emale=root.findViewById<TextInputEditText>(R.id.email).text.toString()
            val pass=root.findViewById<TextInputEditText>(R.id.password).text.toString()
            if (TextUtils.isEmpty(emale) || TextUtils.isEmpty(pass)){
                Toast.makeText(requireContext(),"Email or password is empity", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(emale,pass).addOnCompleteListener(){
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(),"Login Successful", Toast.LENGTH_SHORT).show()
                        root.findNavController().navigate(R.id.action_login_to_userChat)
                    }
                    else{
                        val error=it.exception!!.message
                        Toast.makeText(requireContext(),error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return root
    }

}