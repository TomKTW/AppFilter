package dev.kobalt.core.components.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


fun httpClient(): HttpClient {

    return HttpClient(Android) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        engine {
            sslManager = {
                it.sslSocketFactory = getInsecureSSLContext().socketFactory
                it.hostnameVerifier = getInsecureHostnameVerifier()
            }
        }
    }
}

fun getInsecureTrustManager(): X509TrustManager = object : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
    override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
}

fun getInsecureSSLContext(): SSLContext = SSLContext.getInstance("SSL").apply {
    init(null, arrayOf<TrustManager>(getInsecureTrustManager()), SecureRandom())
}

fun getInsecureHostnameVerifier(): HostnameVerifier = HostnameVerifier { _, _ -> true }