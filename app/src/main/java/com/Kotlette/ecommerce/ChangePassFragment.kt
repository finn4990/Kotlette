package com.Kotlette.ecommerce

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Kotlette.ecommerce.databinding.FragmentChangePassBinding
import com.Kotlette.ecommerce.databinding.FragmentProfileBinding


class ChangePassFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChangePassBinding.inflate(layoutInflater)
        val view = binding.root

        binding.buttonChange.setOnClickListener {
            val old_password = binding.editTextCurrentPsw.text.toString()
            val new_password = binding.editTextNewPsw.text.toString()
            val confirm_password = binding.editText2psw.text.toString()
            val email = " " //SERVIREBBE UN ISTANZA UTENTE IN CUI SALVARE ALMENO L'EMAIL TODO

            if(old_password.isNotBlank() && new_password.isNotBlank() && confirm_password.isNotBlank()){
                if(new_password == confirm_password){

                    val dbHelper = DatabaseHelper(requireContext())
                    val db = dbHelper.writableDatabase

                    val passCorrect = dbHelper.isPassCorrect(email, old_password)

                    if (passCorrect) {
                        val values = ContentValues()
                        values.put("password", new_password)
                        val whereClause = "email = ?"
                        val whereArgs = arrayOf(email)

                        db.update("user", values, whereClause, whereArgs)
                        Toast.makeText(requireContext(), "Password aggiornata", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "La current password non corrisponde a quella utilizzata attualmente", Toast.LENGTH_SHORT).show()
                    }
                    db.close()
                }else{
                    Toast.makeText(requireContext(), "Confirm password error", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(requireContext(), "Fill all the text", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }


}