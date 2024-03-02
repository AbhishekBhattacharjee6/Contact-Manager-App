package com.example.contact_managerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue

class SignIn : AppCompatActivity() {
    companion object{
        const val KEY="com.example.contact_managerapp.SignIn.KEY"
    }
    lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val username=findViewById<TextInputEditText>(R.id.textInputLayout)
        val passkey=findViewById<TextInputEditText>(R.id.textInputLayout2)
        val btn=findViewById<Button>(R.id.button2)
        val tv=findViewById<TextView>(R.id.textView)
        btn.setOnClickListener {
            val uniqueId=username.text.toString()
            val password=passkey.text.toString()
            database=FirebaseDatabase.getInstance().getReference("Users")
            if(uniqueId.isNotEmpty() && password.isNotEmpty())
            {
                database.child(uniqueId).get().addOnSuccessListener {
                    if(it.exists())
                    {
                        val storedpasskey=it.child("password").value.toString()
                        if(storedpasskey==password)
                        {
                             Toast.makeText(this,"Signed In",Toast.LENGTH_SHORT).show()
                            //Go to main page
                            intent=Intent(this,MainActivity::class.java)
                            intent.putExtra(KEY,uniqueId)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this,"Password ans Username didn't matched",Toast.LENGTH_SHORT).show()
                            passkey.text?.clear()
                        }
                    }
                    else
                    {
                        Toast.makeText(this,"Username doesn't exist",Toast.LENGTH_SHORT).show()
                        username.text?.clear()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Error from our side",Toast.LENGTH_SHORT).show()
                }
            }
        }
        tv.setOnClickListener {
            val intent=Intent(this,SignUp::class.java)
            startActivity(intent)
        }
    }
}