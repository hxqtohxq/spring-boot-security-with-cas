/**
 * 
 */
package vn.com.vndirect.onlineuserservice.authen;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

public class VNDSCasAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {
	final static Logger logger = Logger.getLogger(VNDSCasAuthenticationEntryPoint.class);

	private boolean encodeServiceUrlWithSessionId = true;
	private String fwAuthErrorUrlJson = "/WEB-INF/jsps/exceptions/VNDSCasAuthRequiredAJAXJson.jsp";
	private String fwAuthErrorUrlHtml = "/WEB-INF/jsps/exceptions/VNDSCasAuthRequiredAJAXHtml.jsp";

	private final static String XML_HTTP_REQ_VALUE = "XMLHttpRequest";
	private final static String JAVASCRIPT_KEY = "javascript";
	private final static String JSON_KEY = "json";

	// ---------- Properties
	private ServiceProperties serviceProperties;

	private String loginUrl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		System.out.println("----------------------------------");
		Assert.hasLength(this.loginUrl, "loginUrl must be specified");
		Assert.notNull(this.serviceProperties, "serviceProperties must be specified");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.AuthenticationException)
	 */
	public final void commence(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse, final AuthenticationException authenticationException)
			throws IOException, ServletException {
		
		System.out.println("1 ===========" + "casEntryPoint");
		
		final String LOCATION = "commence()";
		if (logger.isDebugEnabled())
			logger.debug(LOCATION + ":: BEGIN");
		try {
			final HttpServletResponse response = (HttpServletResponse) servletResponse;
			final HttpServletRequest request = (HttpServletRequest) servletRequest;

			String fwdUrl = checkJsonJavascript(request);
			/*request.getRequestURI()*/

			if (fwdUrl != null) {
				if (logger.isDebugEnabled())
					logger.debug(LOCATION + ":: BEGIN - fwd:" + fwdUrl);
				request.getRequestDispatcher(fwdUrl).forward(request, response);

			} else {
				final String urlEncodedService = createServiceUrl(servletRequest, response);
				final String redirectUrl = createRedirectUrl(servletRequest, urlEncodedService);
				if (logger.isDebugEnabled())
					logger.debug(LOCATION + ":: BEGIN - redirect:" + redirectUrl);

				preCommence(servletRequest, response);

				response.sendRedirect(redirectUrl);
			}
		} catch (Exception e) {
			logger.error(LOCATION, e);
		}
	}

	/**
	 * Constructs a new Service Url. The default implementation relies on the CAS client to do the bulk of the work.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @param response
	 *            the HttpServlet Response
	 * @return the constructed service url. CANNOT be NULL.
	 */
	protected String createServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
		return CommonUtils.constructServiceUrl(null, response, this.serviceProperties.getService(), null, this.serviceProperties.getArtifactParameter(),
				this.encodeServiceUrlWithSessionId);
	}

	/**
	 * Constructs the Url for Redirection to the CAS server. Default implementation relies on the CAS client to do the bulk of the work.
	 * 
	 * @param serviceUrl
	 *            the service url that should be included.
	 * @return the redirect url. CANNOT be NULL.
	 */
	protected String createRedirectUrl(HttpServletRequest request, final String serviceUrl) {
//		 return CommonUtils.constructRedirectUrl(this.loginUrl, this.serviceProperties.getServiceParameter(), serviceUrl, this.serviceProperties.isSendRenew(), false);
		return this.constructRedirectUrl(request, this.loginUrl, this.serviceProperties.getServiceParameter(), serviceUrl, this.getServiceProperties().isSendRenew(), false);
	}

	/**
	 * Template method for you to do your own pre-processing before the redirect occurs.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @param response
	 *            the HttpServletResponse
	 */
	protected void preCommence(final HttpServletRequest request, final HttpServletResponse response) {

	}

	/**
	 * The enterprise-wide CAS login URL. Usually something like <code>https://www.mycompany.com/cas/login</code>.
	 * 
	 * @return the enterprise-wide CAS login URL
	 */
	public final String getLoginUrl() {
		return this.loginUrl;
	}

	public final ServiceProperties getServiceProperties() {
		return this.serviceProperties;
	}

	public final void setLoginUrl(final String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public final void setServiceProperties(final ServiceProperties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	protected String checkJsonJavascript(final HttpServletRequest request) {
		String fwdUrl = null;
		// +++ check request header
		String xReqVal = request.getHeader("X-Requested-With");
		xReqVal = (xReqVal == null ? "" : xReqVal.trim());

		String acceptVal = request.getHeader("Accept");
		acceptVal = (acceptVal == null ? "" : acceptVal.toLowerCase());

		if (logger.isDebugEnabled())
			logger.debug("checkJsonJavascript():: acceptVal: " + acceptVal + ", xReqVal: " + xReqVal);

		if (XML_HTTP_REQ_VALUE.equalsIgnoreCase(xReqVal) || acceptVal.indexOf(JSON_KEY) > -1 || acceptVal.indexOf(JAVASCRIPT_KEY) > -1) {
			if (acceptVal.indexOf(JSON_KEY) > 0 || acceptVal.indexOf(JAVASCRIPT_KEY) > 0) {
				fwdUrl = fwAuthErrorUrlJson;
			} else {
				fwdUrl = fwAuthErrorUrlHtml;
			}
		}
		// ---
		return fwdUrl;
	}

	/**
	 * 
	 * @param casServerLoginUrl
	 * @param serviceParameterName
	 * @param serviceUrl
	 * @param renew
	 * @param gateway
	 * @return
	 */
	public final String constructRedirectUrl(HttpServletRequest request, String casServerLoginUrl, String serviceParameterName, String serviceUrl, boolean renew, boolean gateway) {
		return casServerLoginUrl + serviceUrl;
	}

	/**
	 * @return the encodeServiceUrlWithSessionId
	 */
	public boolean isEncodeServiceUrlWithSessionId() {
		return encodeServiceUrlWithSessionId;
	}

	/**
	 * @param encodeServiceUrlWithSessionId
	 *            the encodeServiceUrlWithSessionId to set
	 */
	public void setEncodeServiceUrlWithSessionId(boolean encodeServiceUrlWithSessionId) {
		this.encodeServiceUrlWithSessionId = encodeServiceUrlWithSessionId;
	}

	public String getFwAuthErrorUrlJson() {
		return fwAuthErrorUrlJson;
	}

	public void setFwAuthErrorUrlJson(String fwAuthErrorUrl) {
		this.fwAuthErrorUrlJson = fwAuthErrorUrl;
	}

	public String getFwAuthErrorUrlHtml() {
		return fwAuthErrorUrlHtml;
	}

	public void setFwAuthErrorUrlHtml(String fwAuthErrorUrlHtml) {
		this.fwAuthErrorUrlHtml = fwAuthErrorUrlHtml;
	}
}
