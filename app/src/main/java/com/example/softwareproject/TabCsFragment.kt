package  com.example.softwareproject.com.example.softwareproject // 실제 패키지 이름으로 변경

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent // Intent import
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.softwareproject.CsBattleActivity
import com.example.softwareproject.MakeRoomActivity
import com.example.softwareproject.R
import com.example.softwareproject.com.example.softwareproject.presentation.BattleWaitingActivity
import com.example.softwareproject.presentation.room.RoomViewModel
import com.example.softwareproject.presentation.room.adapter.CsRoomAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TabCsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: CsRoomAdapter
    private lateinit var fabPs: FloatingActionButton

    private var isFirstLoaded = false

    private val viewModel: RoomViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_cs, container, false) // fragment_tab_one.xml 사용
        recyclerView = view.findViewById(R.id.recycler_view_tab_one)
        fabPs = view.findViewById(R.id.fab_cs)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(context)
        myAdapter = CsRoomAdapter(emptyList()) { room ->
            lifecycleScope.launch {
                val intent = Intent(requireContext(), BattleWaitingActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }
        }

        recyclerView.adapter = myAdapter

//        // 더미 데이터 생성 (실제로는 ViewModel 등에서 가져옴)
//        val dummyItems = List(20) { MyItem("탭 1 아이템 ${it + 1}", "설명 ${it + 1}") }
//        myAdapter = RoomRecyclerAdapter(dummyItems)
//        recyclerView.adapter = myAdapter

        //임준식 추가
        // 🔥 LiveData 관찰
        viewModel.csRooms.observe(viewLifecycleOwner) { rooms ->
            Log.d("TabCsFragment", "받은 방 개수: ${rooms.size}")
            myAdapter.submitList(rooms)
        }

        // 방 리스트 로드
        if (!isFirstLoaded) {
            viewModel.loadCsRooms() // 수동 1회 로딩
            viewModel.observeCsRooms() // 실시간 리스너 등록
            isFirstLoaded = true
        }

        fabPs.setOnClickListener {
            // BattleLoadingActivity 시작
            val intent = Intent(activity, MakeRoomActivity::class.java)
            // 필요하다면 intent에 데이터 추가 가능
            // intent.putExtra("KEY_BATTLE_ID", battleId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeCsRoomListener()
        isFirstLoaded = false
    }
    companion object {
        fun newInstance() = TabCsFragment()
    }
}