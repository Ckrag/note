package com.note

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.*
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class JwtAuthenticationTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        val e = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<Any, Any>(HttpRequest.GET<Any>("/").accept(TEXT_PLAIN))
        }
        assertEquals(UNAUTHORIZED, e.status)
    }

    @Test
    fun uponSuccessfulAuthenticationAJsonWebTokenIsIssuedToTheUser() {
        val createReq = HttpRequest.POST("/user/create", CreateUserDto("l@m.dk", "my-pass"))
        val createRsp = client.toBlocking().exchange(createReq, UserDto::class.java)
        assertEquals(CREATED, createRsp.status)

        val creds = UsernamePasswordCredentials("l@m.dk", "my-pass")
        val request: HttpRequest<*> = HttpRequest.POST("/login", creds)
        val loginRsp: HttpResponse<BearerAccessRefreshToken> =
            client.toBlocking().exchange(request, BearerAccessRefreshToken::class.java)
        assertEquals(OK, loginRsp.status)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginRsp.body() as BearerAccessRefreshToken
        assertEquals("l@m.dk", bearerAccessRefreshToken.username)
        assertNotNull(bearerAccessRefreshToken.accessToken)
        assertTrue(JWTParser.parse(bearerAccessRefreshToken.accessToken) is SignedJWT)

        val accessToken: String = bearerAccessRefreshToken.accessToken
        val requestWithAuthorization = HttpRequest.GET<Any>("/note")
            .accept(TEXT_PLAIN)
            .bearerAuth(accessToken)
        val response: HttpResponse<String> = client.toBlocking().exchange(requestWithAuthorization, String::class.java)

        assertEquals(OK, loginRsp.status)
        assertEquals("m@l.dk", response.body())
    }
}