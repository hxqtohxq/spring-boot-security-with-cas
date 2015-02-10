package vn.com.vndirect.onlineuserservice.config;

import javax.servlet.Filter;

import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

import vn.com.vndirect.onlineuserservice.authen.CasAuthenticationFilter;
import vn.com.vndirect.onlineuserservice.authen.CasServiceTicketValidator;
import vn.com.vndirect.onlineuserservice.authen.VNDSCasAuthenticationEntryPoint;
import vn.com.vndirect.onlineuserservice.authen.TestAuthenticationUserService;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${sso.online.home.url}")
	private String homeUrl;

	@Value("${sso.online.sec_check.uri}")
	private String sec_check;

	@Value("${sso.online.ticket.validation.url}")
	private String validationUrl;
	
	@Value("${sso.online.login.url}")
	private String loginUrl;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.addFilterAfter(casAuthenticationFilter(), BasicAuthenticationFilter.class);
		
		http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());
		
		http.csrf().disable();
	}

	@Bean
	public Filter casAuthenticationFilter() {
		return new CasAuthenticationFilter();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		 auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public CasAuthenticationProvider authenticationProvider() {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setServiceProperties(serviceProperties());
		provider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
		provider.setTicketValidator(ticketValidator());
		provider.setKey("an_id_for_this_auth_provider_only");

		return provider;
	}

	@Bean
	public VNDSCasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		VNDSCasAuthenticationEntryPoint casAuthenticationEntryPoint = new VNDSCasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(loginUrl);
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	@Bean
	public Filter sessionManagementFilter() {
		return new SessionManagementFilter(new HttpSessionSecurityContextRepository());
	}

	@Bean
	AuthenticationUserDetailsService<Authentication> authenticationUserDetailsService() {
		return new TestAuthenticationUserService();
	}

	@Bean
	public TicketValidator ticketValidator() {
		CasServiceTicketValidator validator = new CasServiceTicketValidator(validationUrl);
		return validator;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(homeUrl + "/" + sec_check);
		serviceProperties.setSendRenew(false);

		return serviceProperties;
	}
}
