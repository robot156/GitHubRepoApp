package com.study.minjin.githubrepoapp

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutinesTest01 {

    @Test
    fun test01() = runBlocking {
        val time = measureTimeMillis {
            val name = getFirstName()
            val lastName = getLastName()
            print("hello $name $lastName")
        }
        print("measure time : $time\n")
    }

    @Test
    fun test02() = runBlocking {
        val time = measureTimeMillis {
            val name = async { getFirstName() }
            val lastName = async { getLastName() }
            print("hello ${name.await()} ${lastName.await()}")
        }
        print("measure time : $time")
    }

    private suspend fun getFirstName() : String {
        delay(1000)
        return "김"
    }

    private suspend fun getLastName() : String {
        delay(1000)
        return "민진"
    }

}