package devcon.map.activity

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.vectormap.LatLng
import devcon.learn.contacts.databinding.ActivityMainBinding
import devcon.map.MainViewModelFactory
import devcon.map.abstracts.KakaoMapActivity
import devcon.map.adapters.ContentDiffUtil
import devcon.map.adapters.ContentRecyclerAdapter
import devcon.map.adapters.HistoryDiffUtil
import devcon.map.adapters.HistoryRecyclerAdapter
import devcon.map.data.room.MyDatabase
import devcon.map.repository.DataStoreRepository
import devcon.map.repository.HistoryRepository
import devcon.map.repository.RetrofitRepository
import devcon.map.viewmodel.MainViewModel

class MainActivity : KakaoMapActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(
                dataStoreRepository = DataStoreRepository(applicationContext),
                retrofitRepository = RetrofitRepository(),
                historyRepository = HistoryRepository(
                    MyDatabase.getInstance(applicationContext).keywordHistoryDao()
                )
            )
        ).get(MainViewModel::class.java)
    }

    override val kakaoMapView by lazy { binding.mapView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViewLogics()
    }

    override suspend fun getStartPoint(): LatLng {
        return viewModel.getStartPoint()
    }

    override fun saveLastPoint(position: LatLng) {
        viewModel.saveLastLocation(position)
    }

    override fun onMapErrorCallback(exception: Exception?) {
        binding.run {
            mapView.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            errorMessage.text = exception?.message
        }
    }

    private fun enableResultArea(enabled: Boolean) {
        binding.searchResultArea.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    private fun setViewLogics() {
        binding.run {
            startMapView()
            initContentRecyclerView()
            initHistoryRecyclerView()

            searchEditText.run {
                setOnFocusChangeListener { _, hasFocus ->
                    enableResultArea(hasFocus)
                }
                setOnClickListener {
                    enableResultArea(true)
                }
                doAfterTextChanged {
                    viewModel.afterChanged(it.toString())
                }
            }
            errorButton.setOnClickListener {
                // 대충 재시도
                startMapView()
            }
        }
        onBackPressedDispatcher.addCallback {
            if (binding.searchResultArea.visibility == View.VISIBLE) {
                enableResultArea(false)
            } else {
                finish()
            }
        }
    }

    private fun initContentRecyclerView() {
        val contentAdapter = ContentRecyclerAdapter(
            diffUtil = ContentDiffUtil(),
            onClick = {
                enableResultArea(false)
                kakaoMapReadyCallback.run {
                    moveCamera(LatLng.from(it.y.toDouble(), it.x.toDouble()))
                    showMarker(LatLng.from(it.y.toDouble(), it.x.toDouble()))
                }
                viewModel.saveHistory(it)
            }
        )
        binding.contentRecyclerView.run {
            adapter = contentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
            viewModel.contentFlow.asLiveData().observe(this@MainActivity) {
                contentAdapter.submitList(it)
            }
        }
    }

    private fun initHistoryRecyclerView() {
        binding.historyRecyclerView.run {
            val historyAdapter = HistoryRecyclerAdapter(
                diffUtil = HistoryDiffUtil(),
                onClick = { viewModel.onDeleteHistory(it) }
            )
            adapter = historyAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL
                )
            )
            viewModel.historyFlow.asLiveData().observe(this@MainActivity) {
                historyAdapter.submitList(it)
            }
        }
    }

    private fun startMapView() {
        binding.mapView.start(mapLifeCycleCallback, kakaoMapReadyCallback)
    }
}