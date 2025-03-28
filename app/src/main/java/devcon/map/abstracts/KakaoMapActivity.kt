package devcon.map.abstracts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

/** KakaoMap 관련 로직은 전부 이쪽으로 몰아넣을 것 **/
abstract class KakaoMapActivity() : AppCompatActivity() {

    abstract val kakaoMapView: MapView
    private val apiKey: String = "1"  // Todo: REST API KEY 전달하기
    val testCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {

        }

        override fun onMapError(p0: Exception?) {

        }
    }

    val testReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(p0: KakaoMap) {

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