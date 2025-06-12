package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.compose.ui.semantics.text
import com.example.softwareproject.util.UserPreferences

class ScreenCFragment : Fragment() {

    private lateinit var editTextGithubId: EditText
    private lateinit var editTextSolvedAcHandle: EditText
    private lateinit var buttonSaveUserIds: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_c, container, false)

        editTextGithubId = view.findViewById(R.id.edit_text_github_id)
        editTextSolvedAcHandle = view.findViewById(R.id.edit_text_solvedac_handle)
        buttonSaveUserIds = view.findViewById(R.id.button_save_user_ids)

        loadUserIds()

        buttonSaveUserIds.setOnClickListener {
            saveUserIds()
        }

        return view
    }

    private fun loadUserIds() {
        Log.d("ScreenCFragment", "loadUserIds called. Context is null: ${context == null}")
        context?.let {
            val githubId = UserPreferences.getGithubId(it)
            val solvedAcHandle = UserPreferences.getSolvedAcHandle(it)
            Log.d("ScreenCFragment", "Loading values: GitHub ID = $githubId, SolvedAC Handle = $solvedAcHandle")

            editTextGithubId.setText(githubId)
            editTextSolvedAcHandle.setText(solvedAcHandle)
        }
    }

    private fun saveUserIds() {
        val githubId = editTextGithubId.text.toString().trim()
        val solvedAcHandle = editTextSolvedAcHandle.text.toString().trim()

        if (githubId.isEmpty() || solvedAcHandle.isEmpty()) {
            Toast.makeText(context, "아이디와 핸들을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        context?.let {
            UserPreferences.saveGithubId(it, githubId)
            UserPreferences.saveSolvedAcHandle(it, solvedAcHandle)
            Toast.makeText(it, "정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}