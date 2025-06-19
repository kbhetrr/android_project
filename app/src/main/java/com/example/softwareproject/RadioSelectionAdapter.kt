package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.com.example.softwareproject.SelectableItem

class RadioSelectionAdapter(
    private val items: List<SelectableItem>,
    private val onItemSelected: (SelectableItem) -> Unit // 선택된 아이템을 전달하는 콜백
) : RecyclerView.Adapter<RadioSelectionAdapter.ViewHolder>() {

    // 현재 선택된 아이템의 position을 저장. 초기값은 선택되지 않음을 의미하는 -1.
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selectable_radio, parent, false) // 아이템 레이아웃
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name

        // 현재 아이템이 선택된 아이템인지 확인
        if (position == selectedPosition) {
            holder.selectionIndicator.setImageResource(R.drawable.check_mark_svgrepo_com) // 선택된 상태 아이콘
            // 필요하다면 배경색 등 다른 UI 변경
            // holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.selected_background))
        } else {
            holder.selectionIndicator.setImageResource(R.drawable.dot_single_svgrepo_com) // 선택 안 된 상태 아이콘
            // holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.transparent))
        }

        // 아이템 클릭 리스너
        holder.itemView.setOnClickListener {
            if (selectedPosition != holder.adapterPosition) { // 현재 선택된 아이템이 아닌 경우에만 업데이트
                val previouslySelectedPosition = selectedPosition
                selectedPosition = holder.adapterPosition // 새로운 아이템 선택

                // 이전에 선택된 아이템이 있었다면 해당 아이템 UI 갱신
                if (previouslySelectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previouslySelectedPosition)
                }
                // 새로 선택된 아이템 UI 갱신
                notifyItemChanged(selectedPosition)

                onItemSelected(items[selectedPosition]) // 콜백 호출
            }
        }
    }

    override fun getItemCount(): Int = items.size

    // 선택된 아이템을 외부에서 설정하거나 가져올 수 있는 메소드 (선택 사항)
    fun setSelectedItem(item: SelectableItem?) {
        val newPosition = items.indexOf(item)
        if (newPosition != selectedPosition) {
            val previouslySelectedPosition = selectedPosition
            selectedPosition = if (newPosition >= 0) newPosition else RecyclerView.NO_POSITION

            if (previouslySelectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previouslySelectedPosition)
            }
            if (selectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPosition)
            }
        }
    }

    fun getSelectedItem(): SelectableItem? {
        return if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < items.size) {
            items[selectedPosition]
        } else {
            null
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectionIndicator: ImageView = itemView.findViewById(R.id.iv_selection_indicator)
        val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
    }
}