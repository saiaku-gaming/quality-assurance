package com.valhallagame.qa.client

import org.apache.http.HttpHost
import org.apache.http.HttpResponse
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.impl.auth.BasicScheme
import org.apache.http.impl.client.BasicAuthCache
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClients
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URI


@Service
class JenkinsClient {

    var logger: Logger = LoggerFactory.getLogger(JenkinsClient::class.java)


    fun triggerStacktraceParse(id: Long, jenkinsApiToken: String = System.getenv("JENKINS_API_TOKEN")) {
        val uri = URI.create("https://jenkins.valhalla-game.com/job/valhalla-qa-stacktrace-composer/buildWithParameters?id=$id&token=$jenkinsApiToken")
        logger.info("Sending {}", uri)
        val host = HttpHost(uri.host, uri.port, uri.scheme)
        val credentialsProvider: CredentialsProvider = BasicCredentialsProvider()
        credentialsProvider.setCredentials(AuthScope(uri.host, uri.port), UsernamePasswordCredentials("phrozenone", jenkinsApiToken))
        val authCache = BasicAuthCache()
        val basicAuth = BasicScheme()
        authCache.put(host, basicAuth)
        val httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()
        val httpGet = HttpGet(uri)
        val localContext = HttpClientContext.create()
        localContext.authCache = authCache

        val response: HttpResponse = httpClient.execute(host, httpGet, localContext)
        println(response.entity)

    }
}
