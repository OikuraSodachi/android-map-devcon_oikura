package devcon.map.abstracts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import devcon.learn.contacts.BuildConfig
import kotlinx.coroutines.launch

/** KakaoMap 관련 로직은 전부 이쪽으로 몰아넣을 것
 *
 * activity lifecycle 에 따른 kakaoMap 로직 케어 **/
abstract class KakaoMapActivity() : AppCompatActivity() {

    abstract val kakaoMapView: MapView
    private val apiKey: String = BuildConfig.APP_KEY
    val mapLifeCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {

        }

        override fun onMapError(p0: Exception?) {

        }
    }

    abstract suspend fun getStartPoint(): LatLng
    abstract fun saveLastPoint(position: LatLng)

    val kakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(map: KakaoMap) {
            lifecycleScope.launch {
                map.moveCamera(
                    CameraUpdateFactory.newCenterPosition(getStartPoint())
                )
            }
            map.setOnCameraMoveEndListener { kakaoMap, cameraPosition, gestureType ->
                saveLastPoint(cameraPosition.position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoMapSdk.init(this, apiKey)
    }

    override fun onResume() {
        super.onResume()
        kakaoMapView.resume()
    }

    override fun onPause() {
        super.onPause()
        kakaoMapView.pause()
    }

}