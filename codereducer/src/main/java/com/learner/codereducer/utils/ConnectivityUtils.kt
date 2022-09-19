package com.learner.codereducer.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

object ConnectivityUtils {
    fun openUrl(context: Context, url: String) =
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    fun isUrl(url: String) = URLUtil.isValidUrl(url) && Patterns.WEB_URL.matcher(url).matches()

    fun isNetworkPresent(context: Context): Boolean {
        var isNetworkAvailable = false
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            if (netInfo != null) {
                isNetworkAvailable = netInfo.isConnectedOrConnecting
            }

            // check for wifi also
            if (!isNetworkAvailable) {
                val connect =
                    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifi = cm.getNetworkInfo(1)!!.state
                isNetworkAvailable = (connect.isWifiEnabled
                        && wifi.toString().equals("CONNECTED", ignoreCase = true))
            }
        } catch (ex: Exception) {
            Log.e("Network Avail Error", ex.message!!)
        }
        return isNetworkAvailable
    }

    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    fun isOnline(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val socketAddress: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(socketAddress, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}