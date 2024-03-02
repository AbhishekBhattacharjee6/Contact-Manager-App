package com.example.contact_managerapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var database:DatabaseReference
    lateinit var dialog:Dialog
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val country_code=findViewById<TextInputEditText>(R.id.countrycode)
        val number=findViewById<TextInputEditText>(R.id.number)
        val name=findViewById<TextInputEditText>(R.id.name)
        val btn=findViewById<Button>(R.id.button)
        dialog=Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)
        val tv1=dialog.findViewById<TextView>(R.id.textView2)
        tv1.paintFlags= Paint.UNDERLINE_TEXT_FLAG
        val radio_group=dialog.findViewById<RadioGroup>(R.id.radio)
        val uniqueId=intent.getStringExtra(SignIn.KEY)?:intent.getStringExtra(SignUp.KEY)
        val welcome=findViewById<TextView>(R.id.welcometext)
        welcome.text="Welcome @$uniqueId"
        var selectedCode:String="+91"
        country_code.setText(selectedCode)
        country_code.setOnClickListener {
            dialog.show()
            radio_group.setOnCheckedChangeListener{
                    _,checkedCode ->
               val selectedRadioButton=dialog.findViewById<RadioButton>(checkedCode)
                selectedCode=selectedRadioButton?.text.toString()
                country_code.setText(selectedCode)
            }

        }
        btn.setOnClickListener {
            val contact_name=name.text.toString()
            val contact_number=number.text.toString()
            if(contact_name.length>10)
            {
                Toast.makeText(this,"Invalid Number",Toast.LENGTH_SHORT).show()
                number.text?.clear()
            }
            val contact= "$selectedCode-$contact_number"
            database=FirebaseDatabase.getInstance().getReference("Data")
          if(contact_name.isNotEmpty() && contact_number.isNotEmpty())
          {
              val idref= uniqueId?.let { it1 -> database.child(it1) }
              idref?.child(contact)?.get()?.addOnSuccessListener {
                  if(it.exists()){
                      Toast.makeText(this,"The contact already exists",Toast.LENGTH_SHORT).show()
                  } else {
                      val ContactInfo=ContactData(contact_name,contact)
                      idref.child(contact).setValue(ContactInfo).addOnSuccessListener {
                          Toast.makeText(this,"Contact added successfully",Toast.LENGTH_SHORT).show()
                      }.addOnFailureListener {
                          Toast.makeText(this,"Fault from our side.Please try again",Toast.LENGTH_SHORT).show()
                      }
                  }
              }?.addOnFailureListener {
                  Toast.makeText(this,"Fault from our side",Toast.LENGTH_SHORT).show()
              }
          }
          else{

              Toast.makeText(this,"Fill the Country Code,Name and Number correctly",Toast.LENGTH_SHORT).show()
          }

        }
    }
}