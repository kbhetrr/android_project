package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.text2.input.delete
import androidx.compose.foundation.text2.input.insert

import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw // 뷰의 크기를 정확히 알기 위해 사용

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import dagger.hilt.android.AndroidEntryPoint

import androidx.viewpager2.widget.ViewPager2
import com.example.softwareproject.com.example.softwareproject.presentation.fragmenta.ScreenAViewModel
import com.example.softwareproject.util.UserPreferences
import com.google.firebase.auth.FirebaseAuth

import kotlin.text.clear
import kotlin.text.format
import java.util.Locale
import android.graphics.Canvas
import android.Manifest

import android.content.ContentValues
import android.content.Intent
// import android.media.MediaScannerConnection // API 29 미만에서 사용, 여기서는 불필요

import android.os.Environment
import androidx.compose.ui.input.key.type
// import androidx.fragment.app.Fragment // Fragment 클래스 내에 있다면 필요
import java.io.File // File.separator 사용을 위해
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date

// import com.google.android.material.imageview.ShapeableImageView


@AndroidEntryPoint
class ScreenAFragment : Fragment() {

    // 뷰 바인딩을 사용한다면 해당 방식으로 뷰 참조
    private lateinit var buttonSaveImage: Button
    private lateinit var viewToCapture: View // 캡처할 뷰 (예: fragment의 루트 뷰 또는 특정 ViewGroup)

    private lateinit var viewPager: ViewPager2
    private lateinit var carouselFragmentAdapter: CarouselAdapter

    // 권한 요청을 위한 ActivityResultLauncher
    @RequiresApi(Build.VERSION_CODES.R)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Permission", "Storage permission granted")
                captureAndSaveView(viewToCapture) // 권한이 부여되면 이미지 저장 실행
            } else {
                Log.d("Permission", "Storage permission denied")
                Toast.makeText(requireContext(), "저장 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    // private lateinit var userProfileImageView: ShapeableImageView
    private val viewModel: ScreenAViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_a, container, false)
        buttonSaveImage = view.findViewById(R.id.saveImageButton) // 버튼 ID를 실제 ID로 변경
        viewToCapture = view.findViewById(R.id.capture_view) // 캡처할 뷰의 ID로 변경
        return view
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")//임준식 추가
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view.findViewById<ShapeableImageView>(R.id.user_profile_image)?.let {
        //     userProfileImageView = it
        // }

        buttonSaveImage.setOnClickListener {
            checkPermissionAndSaveImage(viewToCapture)
        }
        viewPager = view.findViewById(R.id.carousel_view_pager)
        carouselFragmentAdapter = CarouselAdapter(this) // 어댑터는 무한 스크롤 없는 버전
        viewPager.adapter = carouselFragmentAdapter

        // ViewPager2가 그려지기 직전에 크기를 얻어와서 PageTransformer와 Padding을 설정합니다.
        // 이렇게 하면 ViewPager2의 실제 너비를 기준으로 계산할 수 있어 더 정확합니다.
        viewPager.doOnPreDraw {
            setupCarouselPeekEffect()
        }

        // 초기 페이지 설정 (0번 인덱스, 즉 첫 번째 페이지로 시작)
        // setCurrentItem은 PageTransformer와 Padding이 설정된 후 호출되는 것이 좋습니다.
        // doOnPreDraw 콜백 내에서 호출하거나, 그 이후에 호출합니다.
        // 여기서는 setupCarouselPeekEffect 내부에서 처리하지 않으므로 onViewCreated 마지막에 호출.
        if (carouselFragmentAdapter.itemCount > 0) {
            viewPager.setCurrentItem(0, false)
        }


        //임준식 추가 부분

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModel.loadUserDataByUid(userId)

        viewModel.userData.observe(viewLifecycleOwner) { userInfo ->
            val githubName = userInfo.githubInfo.githubName ?: "알 수 없음"
            val hp = userInfo.userAbility.hp
            val attack = userInfo.userAbility.attack
            val shield = userInfo.userAbility.shield
            val level = userInfo.userAbility.level
            val exp = userInfo.userAbility.exp
            val totalExp = userInfo.userAbility.targetExp
            val wins = userInfo.userBattleLog.win
            val losses = userInfo.userBattleLog.lose
            val total = wins + losses
            val rate = if (total > 0) (wins * 100 / total) else 0

            view.findViewById<TextView>(R.id.nickname).text = githubName
            view.findViewById<TextView>(R.id.level).text = "레벨 $level"
            view.findViewById<TextView>(R.id.hp).text = hp.toString()
            view.findViewById<TextView>(R.id.attack).text = attack.toString()
            view.findViewById<TextView>(R.id.shield).text = shield.toString()
            view.findViewById<TextView>(R.id.total).text = total.toString()
            view.findViewById<TextView>(R.id.win).text = wins.toString()
            view.findViewById<TextView>(R.id.lose).text = losses.toString()
            view.findViewById<TextView>(R.id.rate).text = rate.toString()
        }
        //임준식 추가 부분

    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkPermissionAndSaveImage(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 (API 29) 이상에서는 일반적으로 특별한 권한 없이 MediaStore를 통해 저장 가능
            // (앱별 저장소에 저장 후 MediaStore에 등록하는 방식)
            captureAndSaveView(view)
        } else {
            // Android 9 (API 28) 이하에서는 WRITE_EXTERNAL_STORAGE 권한 필요
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    captureAndSaveView(view)
                }
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    // 사용자에게 권한이 필요한 이유를 설명 (예: 다이얼로그 표시)
                    Toast.makeText(requireContext(), "이미지를 저장하려면 저장 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun captureViewToBitmap(view: View): Bitmap {
        // 뷰의 크기만큼 Bitmap 생성
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        // Canvas를 사용하여 Bitmap에 뷰를 그립니다.
        val canvas = Canvas(bitmap)
        // 뷰의 배경이 있다면 그려줍니다. (선택 사항)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(android.graphics.Color.WHITE) // 배경이 없으면 흰색으로 채움
        }
        view.draw(canvas)
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun captureAndSaveView(view: View) {
        try {
            buttonSaveImage.visibility = View.GONE
            val bitmap = captureViewToBitmap(view)
            saveBitmapToGalleryApi30Plus(requireContext(), bitmap, "ScreenA_Capture")
            openGallery()
        }
        catch (e: Exception) {
            Log.e("CaptureAndSave", "Error during capture or save: ${e.message}", e)
            Toast.makeText(requireContext(), "캡처 또는 저장 중 오류 발생", Toast.LENGTH_SHORT).show()
        } finally {
            // 캡처 후 버튼 원래 상태로 복원 (성공/실패 여부와 관계없이)
            buttonSaveImage.visibility = View.VISIBLE
        }
    }
    private fun openGallery() {
        // ACTION_VIEW Intent를 사용하여 이미지 뷰어를 실행합니다.
        val intent = Intent(Intent.ACTION_VIEW)
        // 데이터 타입을 "image/*"로 설정하여 모든 이미지 파일을 대상으로 합니다.
        // 이렇게 하면 시스템은 이미지 파일을 보여줄 수 있는 적절한 앱(보통 갤러리 앱)을 선택합니다.
        intent.type = "image/*"

        // setDataAndType을 사용하여 특정 디렉토리의 이미지를 보여주도록 시도할 수도 있습니다.
        // 예: MediaStore.Images.Media.EXTERNAL_CONTENT_URI 는 모든 외부 저장소 이미지를 나타냅니다.
        // val galleryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        // intent.setDataAndType(galleryUri, "image/*")

        // FLAG_ACTIVITY_NEW_TASK는 Fragment에서 Activity를 시작할 때 필요할 수 있습니다.
        // (일반적으로 Fragment의 context에서 startActivity를 호출하면 자동으로 처리되지만 명시하는 것이 안전할 수 있음)
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 필요에 따라 추가

        // Intent를 처리할 수 있는 앱이 있는지 확인 (권장)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "갤러리 앱을 열 수 없습니다.", Toast.LENGTH_SHORT).show()
            Log.e("OpenGallery", "No Activity found to handle Intent.ACTION_VIEW with type image/*")
        }
    }


    /**
     * Bitmap 이미지를 기기의 갤러리 (Pictures 폴더)에 저장합니다.
     * 이 함수는 Android 11 (API 30) 이상에서만 동작하도록 작성되었습니다.
     *
     * @param context 컨텍스트
     * @param bitmap 저장할 Bitmap 객체
     * @param displayName 파일 이름으로 사용될 기본 문자열 (타임스탬프가 추가됨)
     */
    @RequiresApi(Build.VERSION_CODES.R) // 또는 Q (API 29)도 MediaStore 사용 가능, R은 더 명확한 범위
    private fun saveBitmapToGalleryApi30Plus(context: Context, bitmap: Bitmap, displayName: String) {
        // 파일 이름에 타임스탬프를 추가하여 고유하게 만듭니다.
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "${displayName}_${timeStamp}.jpg" // 확장자는 .png 등으로 변경 가능

        var fos: OutputStream? = null
        var imageUri: Uri? = null

        // MediaStore에 접근하기 위한 컬렉션 Uri
        // API 29 (Q)부터 getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) 사용 가능
        val imageCollection: Uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // 또는 "image/png"
                // RELATIVE_PATH를 사용하여 Pictures 폴더 아래에 앱 이름으로 된 하위 폴더를 지정합니다.
                // Environment.DIRECTORY_PICTURES 는 "Pictures" 문자열을 반환합니다.
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "CoPet") // "YourAppName"을 실제 앱 이름이나 원하는 폴더명으로 변경
                put(MediaStore.Images.Media.IS_PENDING, 1) // 파일을 쓰는 동안 다른 앱에서 접근하지 못하도록 설정
            }

            imageUri = context.contentResolver.insert(imageCollection, contentValues)
            imageUri?.let { uri ->
                fos = context.contentResolver.openOutputStream(uri)
                fos?.use { outputStream ->
                    // Bitmap.compress의 quality는 0-100 사이 값. JPEG 포맷에만 유효.
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) { // JPEG, 품질 90
                        throw Exception("Bitmap compress failed")
                    }
                }

                // IS_PENDING 상태를 0으로 업데이트하여 파일 쓰기 완료를 알림
                contentValues.clear() // 이전 값들을 지우고 IS_PENDING만 업데이트
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)

                Toast.makeText(context, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show()
                Log.d("SaveImageApi30Plus", "Image saved to MediaStore: $uri")
            } ?: throw Exception("MediaStore insert failed. ImageUri is null.")

        } catch (e: Exception) {
            Log.e("SaveImageApi30Plus", "Error saving image: ${e.message}", e)
            Toast.makeText(context, "이미지 저장에 실패했습니다: ${e.message}", Toast.LENGTH_LONG).show()

            // 실패 시, 생성된 MediaStore 항목을 삭제 (선택 사항이지만 권장)
            // imageUri가 null이 아니고, 예외가 발생했을 때만 실행
            if (imageUri != null) {
                try {
                    context.contentResolver.delete(imageUri, null, null)
                    Log.d("SaveImageApi30Plus", "Pending MediaStore entry deleted due to error: $imageUri")
                } catch (deleteEx: Exception) {
                    Log.e("SaveImageApi30Plus", "Error deleting pending MediaStore entry: ${deleteEx.message}", deleteEx)
                }
            }
        } finally {
            try {
                fos?.close()
            } catch (e: Exception) {
                Log.e("SaveImageApi30Plus", "Error closing FileOutputStream: ${e.message}", e)
            }
        }
    }


    private fun setupCarouselPeekEffect() {
        if (!isAdded || viewPager.width == 0) { // 프래그먼트가 추가되지 않았거나, 너비가 아직 0이면 실행하지 않음
            return
        }

        // 1. ViewPager2의 클리핑 비활성화
        viewPager.clipToPadding = false
        viewPager.clipChildren = false

        // 2. ViewPager2의 offscreenPageLimit 설정
        viewPager.offscreenPageLimit = 1 // 양옆 페이지를 미리 로드

        // 3. 페이지 간 간격 및 양옆에 보여질 너비 정의 (dimens.xml 또는 직접 값 사용)
        // res/values/dimens.xml 에 정의했다고 가정
        // <dimen name="viewpager_page_margin">16dp</dimen>
        // <dimen name="viewpager_peek_offset">32dp</dimen>
        val pageMarginPx = 16
        val peekOffsetPx = 32

        // 4. ViewPager2 자체에 패딩 설정
        // 첫 번째 페이지의 왼쪽과 마지막 페이지의 오른쪽에도 peekOffsetPx 만큼의 공간을 확보하고,
        // 페이지 사이의 간격(pageMarginPx)의 절반을 추가로 확보합니다.
        // 이렇게 하면 첫/마지막 페이지에서도 양옆으로 동일한 시각적 여백/미리보기가 가능해집니다.
        val horizontalPadding = peekOffsetPx + (pageMarginPx / 2)
        viewPager.setPadding(horizontalPadding, 0, horizontalPadding, 0)

        // 5. PageTransformer 설정
        viewPager.setPageTransformer { page, position ->
            // position: 현재 페이지가 중앙에 가까울수록 0에 가까워짐.
            // 오른쪽으로 스와이프하면 왼쪽 페이지가 나타나고 position은 0에서 -1로 감.
            // 왼쪽으로 스와이프하면 오른쪽 페이지가 나타나고 position은 0에서 1로 감.

            // ViewPager2의 너비에서 좌우 패딩을 제외한 실제 컨텐츠 영역의 너비
            // 이 너비는 페이지들이 실제로 배치되고 움직일 수 있는 공간입니다.
            val viewPagerContentWidth =
                viewPager.width - viewPager.paddingLeft - viewPager.paddingRight
        }
    }
}
// 각 페이지 아이템이 차지하는 너비 (PageTransformer가 적용되기 전의 기본 너비)
// 일반적으로 페이지는 ViewPager2의 너비만큼을 차지하려고 합니다.
// val pageFullWidth = page.width // 이 시점에서는 page.width가 정확하지 않을 수 있음

// 페이지를 얼마나 이동시킬 것인가?
// 목표: 현재 페이지(position 0)는 중앙에,
// 옆 페이지(position 1 또는 -1)는 peekOffsetPx 만큼만 보이도록.
// 페이지 사이에는 pageMarginPx 만큼의 간격.

// 페이지 이동량을 계산합니다.
// position이 1일 때 (오른쪽 페이지가 중앙으로 올 때), 해당 페이지는 왼쪽으로 이동해야 합니다.
// 이동량 = (ViewPager2 컨텐츠 너비 - peekOffsetPx) + pageMarginPx
// 하지만 이 계산은 페이지가 완전히 화면 밖으로 나갔다가 들어오는 것을 기준으로 하므로,
// 좀 더 간단하게는, 페이지 간의 상대적인 위치를 조절하는 데 집중합니다.

// `offset`은 각 페이지가 자신의 원래 위치에서 얼마나 이동해야 하는지를 나타냅니다.
// `position`이 0일 때 `offset`은 0이 되어야 합니다 (중앙 페이지는 이동 없음).
// `position`이 1 또는 -1일 때, 페이지는 `peekOffsetPx`만큼 보이도록 이동해야 합니다.
// 이 이동은 `pageMarginPx`도 고려해야 합니다.
// `-(peekOffsetPx * 2 + pageMarginPx)` 와 유사한 값을 `position`에 곱하는 방식이 일반적입니다.
// 하지만 이 방식은 ViewPager2의 패딩과 결합될 때 미세 조정이 필요할 수 있습니다.

// 여기서는 ViewPager2의 패딩을 통해 이미 양옆 공간이 확보되었으므로,
// PageTransformer는 페이지들을 "서로 가깝게" 만드는 역할을 합니다.
// 페이지들이 서로 (페이지 너비 - peekOffset * 2 - margin) 만큼 겹치도록 이동합니다.

// `offsetPosition`은 페이지가 얼마나 이동해야 하는지를 결정하는 핵심 값입니다.
// 페이지가 화면 중앙에서 멀어질수록(position의 절대값이 커질수록) 더 많이 이동합니다.
// `pageMarginPx`는 페이지 사이의 간격을 만듭니다.
// `peekOffsetPx`는 옆 페이지가 얼마나 보일지를 결정합니다.
// `viewPager.paddingLeft` (또는 `horizontalPadding`)은 첫 페이지의 시작 위치에 영향을 줍니다.

// 이 값은 페이지가 `position`에 따라 이동해야 하는 총 거리를 나타냅니다.
// 페이지가 중앙(position=0)에 있을 때는 이동하지 않습니다.
// 페이지가 완전히 옆(position=1 또는 -1)으로 갔을 때,
//