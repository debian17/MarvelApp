package com.debian17.marvelapp.data.network

import com.debian17.marvelapp.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest

class ApiHelper {

    companion object {
        private const val ALGORITHM = "MD5"
    }

    fun getMD5(timeStamp: String): String {

        val completeInput =
            timeStamp + BuildConfig.MARVEL_API_PRIVATE_KEY + BuildConfig.MARVEL_API_KEY

        val messageDigest = MessageDigest.getInstance(ALGORITHM)
        messageDigest.reset()
        messageDigest.update(completeInput.toByteArray())
        val digest = messageDigest.digest()

        val bigInt = BigInteger(1, digest)
        var md5Hex = bigInt.toString(16)

        while (md5Hex.length < 32) {
            md5Hex = "0$md5Hex"
        }

        return md5Hex
    }

    fun getTimeStamp(): String {
        return System.currentTimeMillis().toString()
    }

}