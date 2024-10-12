package com.example.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.coroutines.data.model.Post
import com.example.coroutines.ui.theme.CoroutinesTheme
import com.example.coroutines.viewModel.PostViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoroutinesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    println("app is start")

                    runBlocking {
                        launch {
                            delay(1000)
                            println("runBlocking 1 is start")
                        }
                        launch {
                            delay(2000)
                            println("runBlocking 2 is start")
                        }
                    }

                    println("app is resume")
                }
            }
        }
    }


    @Composable
    private fun ObservePostsViewModel() {
        var postList by remember {
            mutableStateOf(emptyList<Post>())
        }


        Column {
            PostView(postList = postList)
        }

        LaunchedEffect(key1 = Unit) {
            val viewModel = ViewModelProvider(this@MainActivity).get(PostViewModel::class.java)
            viewModel.getAllPostsRequest()
            viewModel.postList.observe(this@MainActivity) { posts ->
                // show data
                postList = posts
                println()
            }

            viewModel.postListError.observe(this@MainActivity) { isError ->
                isError?.let {
                    println(isError)
                }
            }
            viewModel.loading.observe(this@MainActivity) { loading ->
                println("loading : $loading")
            }
        }
    }

    @Composable
    fun PostView(postList: List<Post>) {
        LazyColumn() {
            items(postList) { post ->
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(androidx.compose.ui.graphics.Color.Gray)
                ) {
                    Text(
                        text = "${post.id} : ${post.title}",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                    Text(text = post.body, color = androidx.compose.ui.graphics.Color.White)
                }
            }
        }
    }
}