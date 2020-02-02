package com.example.dislerio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationEntryPoint authEntryPoint;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.authorizeRequests()
        .antMatchers(HttpMethod.POST).hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT).hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
        .anyRequest().authenticated();

    // Use AuthenticationEntryPoint to authenticate user/password
    http.httpBasic().authenticationEntryPoint(authEntryPoint);
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    return bCryptPasswordEncoder;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    String password = "123";

    String encrytedPassword = this.passwordEncoder().encode(password);
    System.out.println("Encoded password of 123=" + encrytedPassword);


    InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> //
        mngConfig = auth.inMemoryAuthentication();

    // Defines 2 users, stored in memory.
    // ** Spring BOOT >= 2.x (Spring Security 5.x)
    // Spring auto add ROLE_
    UserDetails u1 = User.withUsername("admin").password(encrytedPassword).roles("ADMIN").build();
    UserDetails u2 = User.withUsername("user").password(encrytedPassword).roles("USER").build();

    mngConfig.withUser(u1);
    mngConfig.withUser(u2);

    // If Spring BOOT < 2.x (Spring Security 4.x)):
    // Spring auto add ROLE_
    // mngConfig.withUser("tom").password("123").roles("USER");
    // mngConfig.withUser("jerry").password("123").roles("USER");
  }
}
