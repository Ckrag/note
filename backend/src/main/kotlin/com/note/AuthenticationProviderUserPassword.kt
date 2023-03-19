package com.note

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

@Singleton
class AuthenticationProviderUserPassword(
    private val userRepo: UserRepository,
    private val passwordRepository: PasswordRepository,
    private val pwHasher: PasswordHasher,
) : AuthenticationProvider {
    // https://guides.micronaut.io/latest/micronaut-security-jwt-gradle-kotlin.html
    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        println("authenticating ${authenticationRequest.identity}")
        val user = userRepo.getUserByEmail(authenticationRequest.identity.toString())
        val password = passwordRepository.getUserPassword(user)
        return Flux.create({ emitter: FluxSink<AuthenticationResponse> ->
            if (pwHasher.matches(authenticationRequest.secret.toString(), password)) {
                val roles = mutableListOf<String>()
                val attr = mutableMapOf<String, Any>()
                emitter.next(AuthenticationResponse.success(
                    authenticationRequest.identity as String,
                    roles,
                    attr
                ))
                emitter.complete()
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        }, FluxSink.OverflowStrategy.ERROR)
    }
}