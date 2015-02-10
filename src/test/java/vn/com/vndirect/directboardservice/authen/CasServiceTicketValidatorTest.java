package vn.com.vndirect.directboardservice.authen;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.onlineuserservice.authen.CasServiceTicketValidator;

public class CasServiceTicketValidatorTest {
	private static final String CAS_LOGIN_URL = "http://suat.vndirect.com.vn/login/";
	
	private CasServiceTicketValidator validator;
	
	@Before
	public void setUp() {
		validator = new CasServiceTicketValidator(CAS_LOGIN_URL);
	}
	
	@Test
	public void test() throws TicketValidationException {
		Assertion assertion = validator.validate("ticket", "service");
		System.out.println(assertion);
	}
}
