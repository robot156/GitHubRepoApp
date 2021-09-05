package com.study.minjin.githubrepoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import com.study.minjin.githubrepoapp.data.entity.GithubRepoEntity
import com.study.minjin.githubrepoapp.databinding.ActivitySearchBinding
import com.study.minjin.githubrepoapp.utillity.RetrofitUtil
import com.study.minjin.githubrepoapp.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {


    private val job = Job()

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun searchKeyword(keywordString: String) = launch {

        withContext(Dispatchers.IO) {
            val response = RetrofitUtil.githubApiService.searchRepositories(keywordString)
            if (response.isSuccessful) {

                val body = response.body()
                withContext(Dispatchers.Main) {

                    body?.let {
                        setData(it.items)
                    }
                }
            }
        }

    }

    private fun setData(items: List<GithubRepoEntity>) {
        adapter.setSearchResultList(items) {
            val intent = Intent(this, RepositoryActivity::class.java)
            intent.putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
            intent.putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
            startActivity(intent)
        }
    }

}