package org.example.domain.auth.service

import okhttp3.FormBody
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.httpClient.CallClient
import org.example.config.Oauth2Config
import org.example.interfaces.OAuth2TokenResponse
import org.example.interfaces.OAuth2UserResponse
import org.example.interfaces.OAuthServiceInterface
import org.springframework.stereotype.Service

private const val key = "github"

@Service(key)
class GithubAuthService(
    private val config: Oauth2Config,
    private val httpClient: CallClient
) : OAuthServiceInterface {

    private val oAuthInfo = config.providers[key] ?: throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, key)
    private val tokenURl = "https://github.com/login/oauth/access_token"
    private val userInfoURL = "https://api.github.com/user"

    override val providerName: String = key

    override fun getToken(code: String): OAuth2TokenResponse {
        val body = FormBody.Builder()
            .add("code", code)
            .add("client_id", oAuthInfo.clientId)
            .add("client_secret", oAuthInfo.clientSecret)
            .add("redirect_uri", oAuthInfo.redirectUri)
            .add("grant_type", "authorization_code")
            .build()

        val headers = mapOf("Accept" to "application/json")
        val jsonString = httpClient.POST(tokenURl, headers, body)

        // jsoonString -> json 처리

        TODO("Not yet implemented")
    }

    override fun getUserIfo(accessToken: String): OAuth2UserResponse {
        TODO("Not yet implemented")
    }

}