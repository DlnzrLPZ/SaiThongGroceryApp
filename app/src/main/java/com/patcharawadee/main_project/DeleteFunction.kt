package com.patcharawadee.main_project

import android.view.LayoutInflater
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.patcharawadee.main_project.databinding.DeleteFunctionBinding

/**
 * คลาส DeleteFunction: แยกส่วนคำสั่งการลบสินค้าพร้อมระบบเลิกทำ (Undo)
 */
class DeleteFunction(
    private val mainView: View,
    private val onDeleteConfirmed: () -> Unit = {},
    private val onUndoClicked: () -> Unit = {},
    private val onRefreshList: () -> Unit = {},
) {

    fun showModernDeleteDialog(productName: String, productCategory: String) {
        val context = mainView.context
        val dialogBinding = DeleteFunctionBinding.inflate(LayoutInflater.from(context))
        
        dialogBinding.tvDialogProductName.text = productName
        dialogBinding.tvDialogProductCategory.text = productCategory

        val alertDialog = MaterialAlertDialogBuilder(context)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.btnDialogCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnDialogConfirm.setOnClickListener {
            onDeleteConfirmed()
            alertDialog.dismiss()
            onRefreshList()

            Snackbar.make(
                mainView,
                context.getString(R.string.delete_success_msg, productName),
                Snackbar.LENGTH_LONG,
            ).setAction(R.string.undo) {
                onUndoClicked()
                onRefreshList()
            }.show()
        }

        alertDialog.show()
    }
}
