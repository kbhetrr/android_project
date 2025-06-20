package com.example.softwareproject.presentation.room.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.R
import com.example.softwareproject.data.remote.room.UiCsRoomItem
import android.view.LayoutInflater

class CsRoomAdapter(
    private var roomList: List<UiCsRoomItem>
) : RecyclerView.Adapter<CsRoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomTitle: TextView = itemView.findViewById(R.id.item_title)
        val topic: TextView = itemView.findViewById(R.id.room_topic)
        val difficulty: TextView = itemView.findViewById(R.id.room_difficulty)
        val githubName: TextView = itemView.findViewById(R.id.host_name)
        val description: TextView = itemView.findViewById(R.id.item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.battle_item_layout, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int = roomList.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.roomTitle.text = room.roomTitle
        holder.topic.text = "주제: ${room.topic}"
        holder.difficulty.text = "난이도: ${room.difficulty}"
        holder.githubName.text = room.githubName ?: "익명 호스트"
        holder.description.text = room.description ?: "설명 없음"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<UiCsRoomItem>) {
        roomList = newList
        notifyDataSetChanged()
    }
}