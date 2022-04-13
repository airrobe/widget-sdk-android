package com.airrobe.widgetsdk.airrobewidget.service

import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

internal object AirRobeApiService {
    const val GET: String = "GET"
    const val POST: String = "POST"
    private val userHeaderString: String = "airrobeWidget/Android ${android.os.Build.VERSION.RELEASE}"

    fun requestPOST(r_url: String?, postDataParams: JSONObject, isGraphQLQuery: Boolean = true): String? {
        val url = URL(r_url)
        return try {
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("User-Agent", userHeaderString)
            urlConnection.readTimeout = 3000
            urlConnection.connectTimeout = 3000
            urlConnection.requestMethod = POST
            urlConnection.doInput = true
            urlConnection.doOutput = true

            if (isGraphQLQuery) {
                val os: OutputStream = urlConnection.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(encodeParams(postDataParams))
                writer.flush()
                writer.close()
                os.close()
            } else {
                val dos = DataOutputStream(urlConnection.outputStream)
                dos.writeBytes(postDataParams.toString())
                dos.flush()
                dos.close()
            }

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
                sb.toString()
            } else null
        } catch (e: Exception) {
            return null
        }
    }

    fun requestGET(url: String?): String? {
        val obj = URL(url)
        return try {
            val urlConnection = obj.openConnection() as HttpURLConnection
            urlConnection.requestMethod = GET

            val responseCode = urlConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val `in` = BufferedReader(InputStreamReader(urlConnection.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                `in`.close()
                response.toString()
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun encodeParams(params: JSONObject): String? {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        return try {
            while (itr.hasNext()) {
                val key = itr.next()
                val value = params[key]
                if (first) first = false else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value.toString(), "UTF-8"))
            }
            result.toString()
        } catch (e: Exception) {
            null
        }
    }
}
