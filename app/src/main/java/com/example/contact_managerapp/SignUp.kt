package com.example.contact_managerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    companion object{
        const val KEY="com.example.contact_managerapp.SignUp.KEY"
    }
    lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val userName=findViewById<TextInputEditText>(R.id.textInputLayout)
        val passkey=findViewById<TextInputEditText>(R.id.textInputLayout2)
        val btn=findViewById<Button>(R.id.button2)
        btn.setOnClickListener{
            val uniqueId=userName.text.toString()
            val password=passkey.text.toString()
            val data=SignUpData(uniqueId,password)
            database=FirebaseDatabase.getInstance().getReference("Users")
            if(uniqueId.isNotEmpty() && password.isNotEmpty())
            {
                database.child(uniqueId).get().addOnSuccessListener {
                    if(it.exists())
                    {
                        Toast.makeText(this,"This UserName already exists.Enter a new one",Toast.LENGTH_SHORT).show()
                        userName.text?.clear()
                        passkey.text?.clear()
                    }
                    else
                    {
                        database.child(uniqueId).setValue(data).addOnSuccessListener {
                            Toast.makeText(this,"Signed Succesfull",Toast.LENGTH_SHORT).show()
                            //Go to main activity
                            intent= Intent(this,MainActivity::class.java)
                            intent.putExtra(KEY,uniqueId)
                            startActivity(intent)

                        }.addOnFailureListener{
                            Toast.makeText(this,"Error from our side.Please try again",Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Database Error from our side",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this,"Enter your data correctly",Toast.LENGTH_SHORT).show()
            }
        }

    }
}