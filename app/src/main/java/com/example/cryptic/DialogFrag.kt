package com.example.cryptic

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

class DialogFrag : DialogFragment() {

    interface DialogListener {
        fun moveData(name : String, count: Float)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.popup, null)

        val sharedData = ViewModelProvider(requireActivity()).get(ShareData::class.java)

        val addName: EditText = dialogView.findViewById(R.id.addName)
        val addCount: EditText = dialogView.findViewById(R.id.addCount)
        val btnDialogAdd: Button = dialogView.findViewById(R.id.btnDialogAdd)

        btnDialogAdd.setOnClickListener {
            var isCurrencyFound = false
            val curName = addName.text.toString()
            val curCount = addCount.text.toString().toFloatOrNull()

            if (curCount==null) {
                Toast.makeText(activity, "Invalid number format!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (data in sharedData.sharedList) {
                if (addName.text.toString()==data.name) {
                    isCurrencyFound = true
                    break
                }
            }
            if (isCurrencyFound && curCount>0) {
                (parentFragment as DialogListener).moveData(curName, curCount)
                dismiss()
            }
            else if (!isCurrencyFound) {
                Toast.makeText(activity, "Currency with this name is not found!", Toast.LENGTH_SHORT).show()
            }
            else if (curCount<=0) {
                Toast.makeText(activity, "Count must be greater than zero!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setView(dialogView)
        return builder.create()
    }
}