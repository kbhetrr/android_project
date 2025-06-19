package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.R // R 클래스 import 확인

// 데이터 클래스는 동일하게 사용
// data class SelectableItem(val id: String, val name: String)

class RadioSelectionAdapter(
    private val items: List<SelectableItem>,
    initialSelectedItemId: String? = null, // 초기에 선택될 아이템의 ID (선택 사항)
    private val onItemSelected: (SelectableItem) -> Unit
) : RecyclerView.Adapter<RadioSelectionAdapter.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    init {
        // 생성자에서 초기 선택된 아이템의 position을 찾아서 설정
        if (initialSelectedItemId != null) {
            selectedPosition = items.indexOfFirst { it.id == initialSelectedItemId }
            // 만약 ID에 해당하는 아이템이 없으면 selectedPosition은 -1 (NO_POSITION)이 됨
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selectable_radio, parent, false) // 아이템 레이아웃 XML
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name

        if (position == selectedPosition) {
            holder.selectionIndicator.setImageResource(R.drawable.check_mark_svgrepo_com) // 실제 드로어블 리소스 사용
            // 예시: 선택된 아이템 배경색 변경
            // holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.selected_item_background))
        } else {
            holder.selectionIndicator.setImageResource(R.drawable.dot_single_svgrepo_com) // 실제 드로어블 리소스 사용
            // 예시: 선택되지 않은 아이템 배경색 원래대로
            // holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.transparent))
        }

        holder.itemView.setOnClickListener {
            if (selectedPosition != holder.adapterPosition) {
                val previouslySelectedPosition = selectedPosition
                selectedPosition = holder.adapterPosition

                if (previouslySelectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previouslySelectedPosition)
                }
                notifyItemChanged(selectedPosition)

                onItemSelected(items[selectedPosition])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    // 외부에서 선택된 아이템을 변경하는 함수 (ID 기반)
    fun setSelectedItemId(itemId: String?) {
        val newPosition = if (itemId != null) {
            items.indexOfFirst { it.id == itemId }
        } else {
            RecyclerView.NO_POSITION
        }

        if (newPosition != selectedPosition) {
            val previouslySelectedPosition = selectedPosition
            selectedPosition = newPosition

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
        val selectionIndicator: ImageView = itemView.findViewById(R.id.iv_selection_indicator) // XML ID와 일치해야 함
        val itemName: TextView = itemView.findViewById(R.id.tv_item_name) // XML ID와 일치해야 함
    }
}