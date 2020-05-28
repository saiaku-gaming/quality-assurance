package com.valhallagame.qa.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest


@Configuration
@Profile("prod")
@Order(1)
class BasicConfig : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.requestMatcher(BasicRequestMatcher())
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic()
    }

    @Bean
    override fun userDetailsService(): UserDetailsService? {
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val user = User.withUsername("jenkins")
                .password(System.getenv("JENKINS_API_TOKEN"))
                .roles("JENKINS")
                .passwordEncoder(encoder::encode)
                .build()
        return InMemoryUserDetailsManager(user)
    }

    private class BasicRequestMatcher : RequestMatcher {
        override fun matches(request: HttpServletRequest): Boolean {
            val auth = request.getHeader("Authorization")
            return auth != null && auth.startsWith("Basic")
        }
    }
}

@Configuration
@Profile("prod")
@Order(2)
class OauthAndPublicConfig : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.authorizeRequests()
                .antMatchers("/public/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
        http.cors().and().csrf().disable()
    }
}


@Configuration
@Profile("dev")
class SecurityConfigDev : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
        http.authorizeRequests().anyRequest().permitAll()
    }
}
