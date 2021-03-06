package com.extcode.project.stensaiapps.adapter.chat

import android.app.Activity
import android.util.DisplayMetrics
import com.extcode.project.stensaiapps.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_right_item.view.*


class ChatRightItem(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val displayMetrics = DisplayMetrics()
        (viewHolder.itemView.context as Activity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        val dpWidth = (displayMetrics.widthPixels * 0.8)

        viewHolder.itemView.rightChatText.maxWidth = dpWidth.toInt()
        viewHolder.itemView.rightChatText.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_right_item
    }
}