package com.dmstechsolution.chatme.ui

import android.app.ProgressDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.dmstechsolution.chatme.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : Fragment() {

    private lateinit var auth: FirebaseAuth

    lateinit var text_lay1: TextInputLayout
    lateinit var text_lay2: TextInputLayout
    lateinit var text_lay3: TextInputLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_signup, container, false)

        auth = FirebaseAuth.getInstance()

        val signup_button = root.findViewById<Button>(R.id.signup_button)
        text_lay1 = root.findViewById(R.id.textInputLayout4)
        text_lay2 = root.findViewById(R.id.textInputLayout3)
        text_lay3 = root.findViewById(R.id.textInputLayout6)

        val name = root.findViewById<TextInputEditText>(R.id.sign_name)
        val email = root.findViewById<TextInputEditText>(R.id.sign_email)
        val pass = root.findViewById<TextInputEditText>(R.id.sign_pass)

        val username=name.text
        val useremail=email.text
        val userpassword=pass.text

        signup_button.setOnClickListener {

            if (TextUtils.isEmpty(username) || username!!.length >= 20) {
                text_lay1.error = "Empty filed"
            } else if (TextUtils.isEmpty(useremail)) {
                text_lay2.error = "Empty filed "
            } else if (TextUtils.isEmpty(userpassword) || userpassword!!.length > 8) {
                text_lay3.error = "Empty filed"
            } else {
                text_lay1.isErrorEnabled = false
                text_lay2.isErrorEnabled = false
                text_lay3.isErrorEnabled = false
                val progressDialog=ProgressDialog(requireContext())
                progressDialog.setTitle("Signing up...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                auth.createUserWithEmailAndPassword(useremail.toString(), userpassword.toString()).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userid =user!!.uid
                        val database = FirebaseDatabase.getInstance()
                        val databaseReference = database.getReference("User").child(userid)
                        val hashMap: HashMap<String,String> = HashMap()
                        hashMap.put("userid",userid)
                        hashMap.put("username",username.toString())
                        hashMap.put("profileimage","")
                        databaseReference.setValue(hashMap).addOnCompleteListener() {
                            if (it.isSuccessful) {
                                Toast.makeText(requireContext(), "Signed up", Toast.LENGTH_SHORT).show()
                                val perferance = requireActivity().getSharedPreferences("signup", Context.MODE_PRIVATE)
                                val edit = perferance.edit()
                                edit.putBoolean("signed", true).apply()
                                progressDialog.dismiss()
                                findNavController().navigate(R.id.action_signup_to_login)
                            } else {
                                progressDialog.dismiss()
                                val mes=it.exception?.message
                                Toast.makeText(requireContext(),mes, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        progressDialog.dismiss()
                        val error=task.exception?.message
                        Toast.makeText(requireContext(),error, Toast.LENGTH_SHORT).show()

                    }
                }
                name.setText("")
                email.setText("")
                pass.setText("")
            }
        }
        root.findViewById<TextView>(R.id.sign_in).setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }

        return root
    }
}

