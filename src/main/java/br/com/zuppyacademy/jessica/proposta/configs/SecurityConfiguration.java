package br.com.zuppyacademy.jessica.proposta.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests
                .mvcMatchers("**/actuator/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/propostas/**").hasAuthority("SCOPE_proposta:read")
                .mvcMatchers(HttpMethod.POST, "/propostas/**").hasAuthority("SCOPE_proposta:write")
                .mvcMatchers(HttpMethod.POST, "/biometrias/**").hasAuthority("SCOPE_biometria:write")
                .mvcMatchers(HttpMethod.PATCH, "/cartoes/**").hasAuthority("SCOPE_cartao:write")
        )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

}

