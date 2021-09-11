package com.android.example.brzycki_formula

import android.app.Application
import java.io.InputStream
import java.lang.StringBuilder
import java.math.BigInteger
import java.security.MessageDigest

class Util {

    companion object {
        fun getFileChecksum(fileStr: String) : String {
            val md5 = MessageDigest.getInstance("MD5")
            return BigInteger(1, md5.digest(fileStr.toByteArray())).toString(16).padStart(32, '0')
        }

        fun getFileAsString(app: Application) : String {
            val inputStream: InputStream = app.resources.openRawResource(R.raw.workoutdata)
            val sb = StringBuilder()
            inputStream.bufferedReader().useLines { lines -> lines.forEach {
                sb.append(it)
                sb.append('\n')
            }}
            inputStream.close()

            //Remove last newline char
            if (sb.length > 0) {
                sb.setLength(sb.length - 1)
            }

            return sb.toString()
        }
    }

}