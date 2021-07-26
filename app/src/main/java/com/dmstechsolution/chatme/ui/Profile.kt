package com.dmstechsolution.chatme.ui

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dmstechsolution.chatme.R
import com.dmstechsolution.chatme.model.Users
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


class Profile : Fragment() {

    private var fileurl:Uri?=null
    private final val REQUEST_CODE:Int=1111

    private lateinit var image:CircleImageView
    private lateinit var image_user:CircleImageView
    private lateinit var change_photo:CircleImageView
    private lateinit var save_image:CircleImageView
    private lateinit var name:EditText
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_profile, container, false)

        name=root.findViewById(R.id.profile_name)
         image_user=root.findViewById(R.id.profile)
        image=root.findViewById(R.id.circleImageView)
        change_photo=root.findViewById(R.id.change_photo)
        save_image=root.findViewById(R.id.save)

        //storage firebase

        storage= FirebaseStorage.getInstance()
        storageReference=storage.reference

       val firebaseUser:FirebaseUser?=FirebaseAuth.getInstance().currentUser
        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(firebaseUser!!.uid)
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val user=snapshot.getValue(Users::class.java)
                name.setText(user?.username)
                if (user?.profileimage==fileurl.toString()){
                    image.setImageResource(R.drawable.profileimage)
                    image_user.setImageResource(R.drawable.profileimage)
                }else{
                    if (user != null) {
                        Glide.with(requireActivity()).load(user.profileimage).into(image)
                        Glide.with(requireActivity()).load(user.profileimage).into(image_user)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
              Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
            }
        })
        change_photo.setOnClickListener {
            getimage()
            change_photo.visibility=View.INVISIBLE
            save_image.visibility=View.VISIBLE

        }
        save_image.setOnClickListener {
            uploadImage()
        }
        root.findViewById<ImageView>(R.id.back).setOnClickListener {
            findNavController().navigate(R.id.action_profile2_to_userChat)
        }
        return root
    }

    fun getimage(){
        val intent:Intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"select image"),REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE){
            fileurl=data!!.data
            try {
                val cr=requireActivity().contentResolver
                val bitmap:Bitmap=MediaStore.Images.Media.getBitmap(cr,fileurl)
                image.setImageBitmap(bitmap)
                image_user.setImageBitmap(bitmap)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage(){
        if (fileurl!=null){
            val progressDialog=ProgressDialog(requireActivity())
            progressDialog.setTitle("Uploading...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val ref=storageReference.child("image/"+UUID.randomUUID().toString())
            ref.putFile(fileurl!!)
                .addOnSuccessListener {
                    val hashMap: HashMap<String,String> = HashMap()
                    hashMap.put("username",name.text.toString())
                    hashMap.put("profileimage",fileurl.toString())
                    databaseReference.updateChildren(hashMap as Map<String, Any>)
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"Uploaded",Toast.LENGTH_SHORT).show()

            }
                    .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(requireContext(),"Failed"+it.message,Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener {
                            val progress:Double=(100.0*it.bytesTransferred/it.totalByteCount)
                            progressDialog.setMessage("Uploading"+progress.toInt()+"%")

                    }
        }
    }
}