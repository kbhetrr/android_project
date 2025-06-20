package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.softwareproject.data.remote.room.UiCsRoomItem
import com.example.softwareproject.data.remote.room.UiPsRoomItem

class RoomRecyclerAdapter(
    private var csRoomList: List<UiCsRoomItem>,
    private var psRoomList: List<UiPsRoomItem>
) : RecyclerView.Adapter<RoomRecyclerAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomTitleTextView: TextView = itemView.findViewById(R.id.item_title)
        val topicTextView: TextView = itemView.findViewById(R.id.room_topic)
        val difficultyTextView: TextView = itemView.findViewById(R.id.room_difficulty)
        val githubNameTextView: TextView = itemView.findViewById(R.id.host_name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.battle_item_layout, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int = csRoomList.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = csRoomList[position]
        holder.roomTitleTextView.text = room.roomTitle
        holder.topicTextView.text = "주제: ${room.topic}"
        holder.difficultyTextView.text = "난이도: ${room.difficulty}" // ✅ 이미 있음
        holder.githubNameTextView.text = room.githubName ?: "익명 호스트"
        holder.descriptionTextView.text = room.description ?: "설명 없음" // ✅ 추가
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<UiCsRoomItem>) {
        csRoomList = newList
        notifyDataSetChanged()
    }
}


