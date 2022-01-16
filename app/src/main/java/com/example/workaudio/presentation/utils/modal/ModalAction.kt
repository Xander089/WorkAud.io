package com.example.workaudio.presentation.utils.modal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class ModalAction(
    val uri:  String,
    val id: Int,
    val clickAction: (String,Int) -> Unit) : Parcelable {

        fun onClick() = clickAction(uri,id)

}