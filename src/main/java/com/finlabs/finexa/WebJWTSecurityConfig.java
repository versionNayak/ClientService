package com.finlabs.finexa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableWebSecurity 
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebJWTSecurityConfig  extends WebSecurityConfigurerAdapter {
	
	   @Value("${security.signing-key}")
	   private String signingKey;

	   @Value("${security.encoding-strength}")
	   private Integer encodingStrength;

	   @Value("${security.security-realm}")
	   private String securityRealm;


	@Autowired
    private UserDetailsService userDetailsService;

   

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();  
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("globalUserDetails ");
        auth.userDetailsService(userDetailsService);
                /*.passwordEncoder(encoder())*/
    }

    /*@Bean
    public JWTAuthenticationFilter authenticationTokenFilterBean() throws Exception {
    	System.out.println("JWTAuthenticationFilter ");
        return new JWTAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    
    	System.out.println("configure");
        http.csrf().disable();
              .authorizeRequests()
                .antMatchers("/findLastLoginTime/**").permitAll()
                .antMatchers("/loggingOut/**").permitAll()
                .anyRequest().authenticated();
                //.and()
               http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
               http
              .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
            
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }*/
    
  /*  @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService)
               .passwordEncoder(new ShaPasswordEncoder(encodingStrength));
    }*/

    @Override
    public void configure(WebSecurity web) throws Exception {
                 web.ignoring()
                .antMatchers("/advisorUser/existEmail")
                .antMatchers("/advisorUser/sendVerificationLink")
                .antMatchers("/forgotPassword/authenticateUser/*")
                .antMatchers("/advisor_change_Pass")
                .antMatchers("/checkLoggedInOrNot")
                .antMatchers("/checkLoggedInOrNotWithLastToken")
                .antMatchers("/clientMaster/existEmail")
                .antMatchers("/clientMaster/sendVerificationLinkForClient")
                .antMatchers("/checkLoggedInOrNotForClient")
                .antMatchers("/checkLoggedInOrNotForClient")
                .antMatchers("/changePasswordadvisorBeforeLogin")
                .antMatchers("/changePasswordClientBeforeLogin");;
                
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	System.out.println("securityRealm "+securityRealm);
                http
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .httpBasic()
               .realmName(securityRealm)
               .and()
               .csrf()
               .disable();
       
       
       // http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
      
              
    }
	/*
	 * @Bean public SessionRegistry sessionRegistry() { return new
	 * SessionRegistryImpl(); }
	 * 
	 * @Bean public ServletListenerRegistrationBean<HttpSessionEventPublisher>
	 * httpSessionEventPublisher() { return new
	 * ServletListenerRegistrationBean<HttpSessionEventPublisher>(new
	 * HttpSessionEventPublisher()); }
	 */

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
    	System.out.println("JwtAccessTokenConverter signingKey "+signingKey);
       JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
       converter.setSigningKey(signingKey);
      // converter.setVerifierKey(signingKey);
       return converter;
    }

    @Bean
    public TokenStore tokenStore() {
    	System.out.println("JwtTokenStore ");
       return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary //Making this primary to avoid any accidental duplication with another token service instance of the same name
    public DefaultTokenServices tokenServices() {
    	System.out.println("tokenServices ");
       DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
       defaultTokenServices.setTokenStore(tokenStore());
       defaultTokenServices.setSupportRefreshToken(true);
       return defaultTokenServices;
    }

}

