package com.example.softwareproject.com.example.softwareproject.model

import android.content.Context
import android.content.res.AssetManager
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.io.Closeable // Closeable을 import 해야 합니다.

class DamageCalculator(context: Context) : Closeable {

    private val interpreter: Interpreter

    // 1. Python에서 추출한 최소/최대값을 여기에 상수로 저장합니다.
    //    반드시 본인의 값으로 교체해주세요!
    private val featureMin = floatArrayOf(1.0f, 10.0f, 1.0f)           // [tier_min, solvers_min, averageTries_min]
    private val featureMax = floatArrayOf(31.0f, 199519.0f, 10.0f)    // [tier_max, solvers_max, averageTries_max]

    init {
        val modelBuffer = loadModelFile(context.assets, "damage_model.tflite")
        interpreter = Interpreter(modelBuffer)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        // ... (이전과 동일)
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * 입력값을 Min-Max 정규화하는 함수
     * X_scaled = (X - X_min) / (X_max - X_min)
     */
    private fun normalize(inputs: FloatArray): FloatArray {
        val scaledInputs = FloatArray(inputs.size)
        for (i in inputs.indices) {
            val min = featureMin[i]
            val max = featureMax[i]
            val range = max - min

            // 분모가 0이 되는 경우 방지
            scaledInputs[i] = if (range > 0) {
                (inputs[i] - min) / range
            } else {
                0f // 또는 0.5f 등 상황에 맞는 값
            }
        }
        return scaledInputs
    }

    /**
     * 입력값을 받아 데미지를 예측합니다.
     * @param tier 문제 티어
     * @param solvers 문제를 푼 사람 수
     * @param averageTries 평균 시도 횟수
     * @return 예측된 데미지 값
     */
    fun predictDamage(tier: Float, solvers: Float, averageTries: Float): Float {
        // 원본(raw) 입력 배열 생성
        val rawInputs = floatArrayOf(tier, solvers, averageTries)

        // 2. 모델에 넣기 전에 데이터를 정규화합니다.
        val scaledInputs = normalize(rawInputs)

        // 3. 입력 버퍼 준비 (3개의 Float 입력 = 3 * 4 bytes)
        val inputBuffer = ByteBuffer.allocateDirect(3 * 4).order(ByteOrder.nativeOrder())
        inputBuffer.asFloatBuffer().put(scaledInputs)

        // 4. 출력 버퍼 준비 (1개의 Float 출력 = 1 * 4 bytes)
        val outputBuffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder())

        // 5. 모델 실행 (추론)
        interpreter.run(inputBuffer, outputBuffer)

        // 6. 결과값 반환
        outputBuffer.rewind()
        return outputBuffer.asFloatBuffer().get()
    }

    override fun close() {
        interpreter.close()
    }
}