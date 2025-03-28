package devcon.map.activity

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import devcon.learn.contacts.databinding.ActivityMainBinding
import devcon.map.MainViewModelFactory
import devcon.map.SearchTextWatcher
import devcon.map.abstracts.KakaoMapActivity
import devcon.map.adapters.ContentRecyclerAdapter
import devcon.map.adapters.HistoryRecyclerAdapter
import devcon.map.repository.SampleRepository
import devcon.map.viewmodel.MainViewModel

class MainActivity : KakaoMapActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(SampleRepository(applicationContext))
        ).get(MainViewModel::class.java)
    }

    override val kakaoMapView by lazy { binding.mapView }

    override val api_key by lazy { "temporaryString" }  // api key 임시 값

    //override val api_key by lazy { TODO() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewLogics()
        viewModel.checkDB()
    }

    private fun setViewLogics() {
        binding.run {
            mapView.start(testCallback, testReadyCallback)

            contentRecyclerView.run {
                adapter = ContentRecyclerAdapter(
                    itemFlow = viewModel.contentFlow,
                    onClick = { viewModel.onSelectItem(it) }
                )
                layoutManager = LinearLayoutManager(this@MainActivity)
                addItemDecoration(
                    DividerItemDecoration(
                        this@MainActivity,
                        LinearLayoutManager.VERTICAL
                    )
                )
            }

            historyRecyclerView.run {
                adapter = HistoryRecyclerAdapter(
                    itemFlow = viewModel.historyFlow,
                    onClick = { viewModel.onDeleteHistory(it) }
                )
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(
                    DividerItemDecoration(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL
                    )
                )

            }

            searchEditText.run {
                setOnFocusChangeListener { _, hasFocus ->
                    enableResultArea(hasFocus)
                }
                setOnClickListener {
                    enableResultArea(true)
                }

                addTextChangedListener(SearchTextWatcher { viewModel.afterChanged(it) })
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

    private fun enableResultArea(enabled: Boolean) {
        binding.searchResultArea.visibility = if (enabled) View.VISIBLE else View.GONE
    }
}