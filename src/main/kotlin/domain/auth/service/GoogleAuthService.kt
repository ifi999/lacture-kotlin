package org.example.domain.auth.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.FormBody
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.httpClient.CallClient
import org.example.common.json.JsonUtil
import org.example.config.Oauth2Config
import org.example.interfaces.OAuth2TokenResponse
import org.example.interfaces.OAuth2UserResponse
import org.example.interfaces.OAuthServiceInterface
import org.springframework.stereotype.Service

private const val key = "google"

@Service(key)
class GoogleAuthService(
    private val config: Oauth2Config,
    private val httpClient: CallClient
) : OAuthServiceInterface {

    private val oAuthInfo = config.providers[key] ?: throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, key)
    private val tokenURl = "https://oauth2.googleapis.com/token"
    private val userInfoURL = "https://oauth2.googleapis.com/oauth2/v2/userinfo"

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

        val response  : GoogleTokenResponse = JsonUtil.decodeFromJson(jsonString, GoogleTokenResponse.serializer())

        return response
    }

    override fun getUserIfo(accessToken: String): OAuth2UserResponse {
        val headers = mapOf(
            "Content-type" to "application/json",
            "Authorization" to "Bearer $accessToken"
        )

        val jsonString = httpClient.GET(userInfoURL, headers)
        val response : GoogleUserResponse= JsonUtil.decodeFromJson(jsonString, GoogleUserResponse.serializer())

        return response
    }

}

@Serializable
data class GoogleTokenResponse (
    @SerialName("access_token") override val accessToken: String,
) : OAuth2TokenResponse

@Serializable
data class GoogleUserResponse (
    override val id: String,
    override val email: String?,
    override val name: String?,
) : OAuth2UserResponse