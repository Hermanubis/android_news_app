package edu.gwu.myapplication

import android.location.Address
import android.util.Base64
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class newsManager {
    val okHttpClient: OkHttpClient

    init {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        okHttpClient = okHttpClientBuilder.build()
    }


    fun retrieveSources(newsAPI: String, category: String): List<sources> {
        val sourceList: MutableList<sources> = mutableListOf()

        // Unlike normal API Keys (like Google Maps and News API) Twitter uses something slightly different,
        // so the "apiKey" here isn't really an API Key - we'll see in Lecture 7.
        var request: Request =
            Request.Builder()
                .url("https://newsapi.org/v2/top-headlines/sources?category=${category}&language=en&apiKey=$newsAPI")
                .header("Authorization", "$newsAPI")
                .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json: JSONObject = JSONObject(responseBody)
            val sources: JSONArray = json.getJSONArray("sources")

            for (i in 0 until sources.length()) {
                val curr: JSONObject = sources.getJSONObject(i)
                val name = curr.getString("name")
                val description = curr.getString("description")
                sourceList.add(sources(name, description))
            }
        }

        return sourceList
    }

    fun retrieveMapNews(newsAPI: String, location: List<Address>): List<news> {
        val newsArticles: MutableList<news> = mutableListOf()

        // Unlike normal API Keys (like Google Maps and News API) Twitter uses something slightly different,
        // so the "apiKey" here isn't really an API Key - we'll see in Lecture 7.

        if (location[1].countryCode.isNullOrEmpty()) {
            return emptyList()
        }

        var request: Request =
            Request.Builder()
                .url("https://newsapi.org/v2/everything?qInTitle=${location[0].countryName}&sortBy=relevancy&apiKey=$newsAPI")
                .header("Authorization", "$newsAPI")
                .build()


        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json: JSONObject = JSONObject(responseBody)
            val articles: JSONArray = json.getJSONArray("articles")

            for (i in 0 until articles.length()) {
                val curr: JSONObject = articles.getJSONObject(i)
                val title = curr.getString("title")
                val source = curr.getJSONObject("source").getString("name")
                val urlToImage = curr.getString("urlToImage")
                val content = curr.getString("content")
                val url = curr.getString("url")
                newsArticles.add(news(title, source, urlToImage, content, url))
            }
        }

        return newsArticles
    }

    fun retrieveAllNews(newsAPI: String, term: String): List<news> {
        val newsArticles: MutableList<news> = mutableListOf()

        // Unlike normal API Keys (like Google Maps and News API) Twitter uses something slightly different,
        // so the "apiKey" here isn't really an API Key - we'll see in Lecture 7.

        var request: Request =
            Request.Builder()
                .url("https://newsapi.org/v2/everything?qInTitle=${term}&sortBy=relevancy&apiKey=$newsAPI")
                .header("Authorization", "$newsAPI")
                .build()


        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json: JSONObject = JSONObject(responseBody)
            val articles: JSONArray = json.getJSONArray("articles")

            for (i in 0 until articles.length()) {
                val curr: JSONObject = articles.getJSONObject(i)
                val title = curr.getString("title")
                val source = curr.getJSONObject("source").getString("name")
                val urlToImage = curr.getString("urlToImage")
                val content = curr.getString("content")
                val url = curr.getString("url")
                newsArticles.add(news(title, source, urlToImage, content, url))
            }
        }

        return newsArticles
    }

    fun retrieveNewsFromSource(newsAPI: String, term: String, selectedSource: String): List<news> {
        val newsArticles: MutableList<news> = mutableListOf()

        // Unlike normal API Keys (like Google Maps and News API) Twitter uses something slightly different,
        // so the "apiKey" here isn't really an API Key - we'll see in Lecture 7.

        var request: Request =
            Request.Builder()
                .url("https://newsapi.org/v2/everything?qInTitle=${term}&sources=${selectedSource}&sortBy=relevancy&apiKey=$newsAPI")
                .header("Authorization", "$newsAPI")
                .build()


        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json: JSONObject = JSONObject(responseBody)
            val articles: JSONArray = json.getJSONArray("articles")

            for (i in 0 until articles.length()) {
                val curr: JSONObject = articles.getJSONObject(i)
                val title = curr.getString("title")
                val source = curr.getJSONObject("source").getString("name")
                val urlToImage = curr.getString("urlToImage")
                val content = curr.getString("content")
                val url = curr.getString("url")
                newsArticles.add(news(title, source, urlToImage, content, url))
            }
        }

        return newsArticles
    }
}