package com.study.minjin.githubrepoapp.data.response

import com.study.minjin.githubrepoapp.data.entity.GithubRepoEntity


data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)