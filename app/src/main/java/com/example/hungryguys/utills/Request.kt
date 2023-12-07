package com.example.hungryguys.utills

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Request {
    companion object {
        // HTTP API 요청 주소
        const val REQUSET_URL = "http://louk342.iptime.org";

        //get요청 메소드
        fun reqget(url: String): JSONObject? {
            val link = URL(url)
            val huc = link.openConnection() as HttpURLConnection
            // http 연결 부분

            return if (huc.responseCode == HttpURLConnection.HTTP_OK) { // 맞는 응답인지 확인
                // 정상응답
                val json = BufferedReader(InputStreamReader(huc.inputStream, "utf-8")).readLine() //json 읽기
                Log.d("로그", "get요청 성공: $url")
                JSONObject(json) // 정상 응답일 경우 리턴
            } else {
                // 비정상 응답
                Log.d("로그", "get요청 오류: $url")
                null
            }
        }

        //post 요청 메소드
        fun reqpost(url: String, outputjson: JSONObject): JSONObject? {
            val link = URL(url)
            val huc = link.openConnection() as HttpURLConnection
            // http 연결 부분

            huc.requestMethod = "POST" // 요청 메소드 (get/post)
            huc.setRequestProperty("Content-Type", "application/json") // 전송 타입
            huc.setRequestProperty("Accept", " application/json") // 받을 타입
            huc.doOutput = true // 데이터를 보낼껀지 설정

            val output = huc.outputStream
            output.write(outputjson.toString().toByteArray(charset("utf-8"))) // 데이터 보내기
            output.flush() // 바이트 초기화

            return if (huc.responseCode == HttpURLConnection.HTTP_OK) { // 맞는 응답인지 확인
                // 정상응답
                val json = BufferedReader(InputStreamReader(huc.inputStream, "utf-8")).readLine() //json 파일 읽기
                Log.d("로그", "post요청 성공: $url")
                JSONObject(json) // 정상 응답일 경우 리턴
            } else {
                // 비정상 응답
                Log.d("로그", "post요청 오류: $url")
                null
            }
        }
    }
}
