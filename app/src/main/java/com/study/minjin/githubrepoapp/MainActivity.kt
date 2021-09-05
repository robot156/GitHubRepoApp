package com.study.minjin.githubrepoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import com.study.minjin.githubrepoapp.data.database.DataBaseProvider
import com.study.minjin.githubrepoapp.data.entity.GithubOwner
import com.study.minjin.githubrepoapp.data.entity.GithubRepoEntity
import com.study.minjin.githubrepoapp.databinding.ActivityMainBinding
import com.study.minjin.githubrepoapp.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    private val repositoryDao by lazy {
        DataBaseProvider.provideDB(applicationContext).repositoryDao()
    }

    private lateinit var adapter: RepositoryRecyclerAdapter
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initAdapter()

    }

    override fun onResume() {
        super.onResume()

        launch {
            loadLikeRepositoryList()
        }
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private suspend fun loadLikeRepositoryList() = withContext(Dispatchers.IO) {
        val repoList = DataBaseProvider.provideDB(this@MainActivity).repositoryDao().getHistory()
        withContext(Dispatchers.Main) {
            setData(repoList)
        }
    }

    private fun setData(githubRepoList : List<GithubRepoEntity>) = with(binding) {

        if(githubRepoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setSearchResultList(githubRepoList) {
                val intent = Intent(this@MainActivity, RepositoryActivity::class.java)
                intent.putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
                intent.putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                startActivity(intent)
            }
        }

    }

    private fun initViews() = with(binding) {

        searchButton.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}