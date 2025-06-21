package com.example.softwareproject.com.example.softwareproject.data.repository

import android.util.Log
import com.example.softwareproject.domain.repository.ProblemRepository
import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.problem.PsProblemDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProblemRepositoryImpl @Inject constructor(
    private val fireBaseStore : FirebaseFirestore
) : ProblemRepository
{

    override suspend fun createCsProblem(csProblemDto: CsProblemDto) {
        val firestore = FirebaseFirestore.getInstance()

        val docId = "${csProblemDto.csRoomId}_${csProblemDto.problemIndex}"

        firestore.collection("cs_problem")
            .document(docId)
            .set(csProblemDto)
            .addOnSuccessListener {
                Log.d("Firestore", "CS 문제 생성 성공: ${csProblemDto.problemIndex}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "CS 문제 생성 실패: ${e.message}")
            }
    }
    override suspend fun createPsProblem(psProblemDto: PsProblemDto) {
        val firestore = FirebaseFirestore.getInstance()

        // 예: codingRoomId = 1, problemIndex = 1 이면 → 문서 ID: 1_1
        val docId = "${psProblemDto.codingRoomId}_${psProblemDto.problemIndex}"

        firestore.collection("coding_problem")
            .document(docId)
            .set(psProblemDto)
            .addOnSuccessListener {
                Log.d("Firestore", "PS 문제 생성 성공: ${psProblemDto.problemId}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "PS 문제 생성 실패: ${e.message}")
            }
    }

    override suspend fun getCsProblem(csRoomId: String, problemIndex: Int): CsProblemDto? {
        val firestore = FirebaseFirestore.getInstance()

        val snapshot = firestore.collection("cs_problem")
            .whereEqualTo("csRoomId", csRoomId)
            .whereEqualTo("problemIndex", problemIndex.toString())
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.toObject(CsProblemDto::class.java)
    }

    override suspend fun getPsProblem(psRoomId: String, problemIndex: Int): PsProblemDto? {
        val firestore = FirebaseFirestore.getInstance()

        val snapshot = firestore.collection("coding_problem")
            .whereEqualTo("codingRoomId", psRoomId)
            .whereEqualTo("problemIndex", problemIndex.toString())
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.toObject(PsProblemDto::class.java)
    }

    override suspend fun getCsProblemList(csRoomId: String): List<CsProblemDto> {
        val firestore = FirebaseFirestore.getInstance()

        val snapshot = firestore.collection("cs_problem")
            .whereEqualTo("csRoomId", csRoomId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(CsProblemDto::class.java) }
    }

    override suspend fun getPsProblemList(psRoomId: String): List<PsProblemDto> {
        val firestore = FirebaseFirestore.getInstance()

        val snapshot = firestore.collection("coding_problem")
            .whereEqualTo("codingRoomId", psRoomId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(PsProblemDto::class.java) }
    }

    override suspend fun getProblemByIndex(roomId: String, index: Int): CsProblemDto {
        return try {
            val snapshot = fireBaseStore.collection("cs_problem")
                .whereEqualTo("csRoomId", roomId)
                .whereEqualTo("problemIndex", index.toString())
                .get()
                .await()

            snapshot.documents.first().toObject(CsProblemDto::class.java) ?: CsProblemDto()
        } catch (e: Exception) {
            Log.e("Repository", "문제 불러오기 실패: ${e.message}")
            CsProblemDto()
        }
    }

}