package vn.com.vndirect.onlineuserservice.authen;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TestAuthenticationUserService implements AuthenticationUserDetailsService<Authentication> {

	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
