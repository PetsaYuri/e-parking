package API.eparking.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/profile").failureUrl("/login")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/parking/properties/**").hasAnyRole("admin", "owner")
                .requestMatchers("/api/parking/**").authenticated()
                .requestMatchers("/profile").authenticated()
                .requestMatchers("/api/users/{id}/upload").authenticated()

                //dont forget remove str bellow
                .requestMatchers("/api/users/**").permitAll()
            //    .requestMatchers("/api/users/**").hasAnyRole("admin", "owner")
                .requestMatchers("/api/promocodes/**").hasAnyRole("admin", "owner")
                .requestMatchers("/api/cars/**").authenticated()
                .requestMatchers("/api/transactions/**").hasAnyRole("admin", "owner")
                .requestMatchers("/api/booking/**").authenticated()
                .requestMatchers("/signup").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}
