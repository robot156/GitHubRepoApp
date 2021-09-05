package com.study.minjin.githubrepoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.study.minjin.githubrepoapp.data.dao.SearchHistoryDao
import com.study.minjin.githubrepoapp.data.entity.GithubRepoEntity


@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase : RoomDatabase() {

    abstract fun repositoryDao(): SearchHistoryDao

}