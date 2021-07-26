package com.dmstechsolution.chatme.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import com.dmstechsolution.chatme.R
import com.google.firebase.auth.FirebaseAuth

class Splash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_splash, container, false)
        val perferance=requireActivity().getSharedPreferences("splash", Context.MODE_PRIVATE)
        val is_sign=perferance.getBoolean("spla",false)
        if (is_sign){
            findNavController().navigate(R.id.action_splash_to_login)
        }
        val root_layout=root.findViewById<MotionLayout>(R.id.rootlayout)
        root_layout.addTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    findNavController().navigate(R.id.action_splash_to_login)
                val perferance = requireActivity().getSharedPreferences("splash", Context.MODE_PRIVATE)
                val edit = perferance.edit()
                edit.putBoolean("spla", true).apply()
                }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })

        return root
    }



}