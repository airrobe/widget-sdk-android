package com.airrobe.widgetsdk.airrobewidget.service

import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.BuildConfig
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

internal object AirRobeApiService {
    const val GET: String = "GET"
    const val POST: String = "POST"
    private val userHeaderString: String = "airrobeWidget/${BuildConfig.VERSION_NAME} (Android ${android.os.Build.VERSION.RELEASE})"

    @Throws(IOException::class)
    fun requestPOST(r_url: String?, postDataParams: JSONObject): String? {
        val url = URL(r_url)
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestProperty("User-Agent", userHeaderString)
        urlConnection.readTimeout = 3000
        urlConnection.connectTimeout = 3000
        urlConnection.requestMethod = POST
        urlConnection.doInput = true
        urlConnection.doOutput = true
        val os: OutputStream = urlConnection.outputStream
        val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
        writer.write(encodeParams(postDataParams))
        writer.flush()
        writer.close()
        os.close()
        val responseCode: Int = urlConnection.responseCode // To Check for 200
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val `in` = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val sb = StringBuffer("")
            var line: String?
            while (`in`.readLine().also { line = it } != null) {
                sb.append(line)
                break
            }
            `in`.close()
            return sb.toString()
        }
        return null
    }

    @Throws(IOException::class)
    fun requestGET(url: String?): String? {
        val obj = URL(url)
        Log.d("TEST:::", url ?: "NULLL")
        val urlConnection = obj.openConnection() as HttpURLConnection
        urlConnection.requestMethod = GET
        val responseCode = urlConnection.responseCode
        println("Response Code :: $responseCode")
        return if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            val `in` = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var inputLine: String?
            val response = StringBuffer()
            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()
            response.toString()
        } else null
    }

    @Throws(java.lang.Exception::class)
    private fun encodeParams(params: JSONObject): String? {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }
}