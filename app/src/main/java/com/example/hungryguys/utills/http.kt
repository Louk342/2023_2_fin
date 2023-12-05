package com.example.hungryguys.utills

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownServiceException

class http {
    @Throws(UnknownServiceException::class, IOException::class, JSONException::class)
    fun reqget(url: String?): JSONObject {
        val link = URL(url)
        val huc = link.openConnection() as HttpURLConnection
        // http 연결 부분

        return if (huc.responseCode == HttpURLConnection.HTTP_OK) { // 맞는 응답인지 확인
            val json =
                BufferedReader(InputStreamReader(huc.inputStream, "utf-8")).readLine() //json 읽기
            JSONObject(json) // 정상 응답일 경우 리턴
        } else {// 비정상 응답
            val json = BufferedReader(InputStreamReader(huc.errorStream, "utf-8")).readLine()
            val jo = JSONObject(json) //json 읽기
            val err = jo.getString("err") // 모든 오류는 err 이라는 json을 가짐
            throw UnknownServiceException(err)
        }
    }
}