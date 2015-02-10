package vn.com.vndirect.onlineuserservice.authen;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/** 
 * @author Spring
 */
public class CasAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    //~ Static fields/initializers =====================================================================================

    /** Used to identify a CAS request for a stateful user agent, such as a web browser. */
    public static final String CAS_STATEFUL_IDENTIFIER = "_cas_stateful_";

    /**
     * Used to identify a CAS request for a stateless user agent, such as a remoting protocol client (e.g.
     * Hessian, Burlap, SOAP etc). Results in a more aggressive caching strategy being used, as the absence of a
     * <code>HttpSession</code> will result in a new authentication attempt on every request.
     */
    public static final String CAS_STATELESS_IDENTIFIER = "_cas_stateless_";

    /**
     * The last portion of the receptor url, i.e. /proxy/receptor
     */
    private String proxyReceptorUrl;

    /**
     * The backing storage to store ProxyGrantingTicket requests.
     */
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage;

    private String artifactParameter = ServiceProperties.DEFAULT_CAS_ARTIFACT_PARAMETER;

    //~ Constructors ===================================================================================================

    public CasAuthenticationFilter() {
        super("/j_spring_cas_security_check");
        System.out.println("------------ Construct CasAuthenticationFilter ");
    }

    //~ Methods ========================================================================================================

    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException {
    	
    	System.out.println("------------ attemptAuthentication");
    	
    	
        final String username = CAS_STATEFUL_IDENTIFIER;
        String password = request.getParameter(this.artifactParameter);

        if (password == null) {
            password = "";
        }

        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Overridden to provide proxying capabilities.
     */
    protected boolean requiresAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
    	System.out.println("------------ requiresAuthentication");
    	
    	final String requestUri = request.getRequestURI();
    	System.out.println("------------ " + requestUri);

        if (CommonUtils.isEmpty(this.proxyReceptorUrl) || !requestUri.endsWith(this.proxyReceptorUrl) || this.proxyGrantingTicketStorage == null) {
            return super.requiresAuthentication(request, response);
        }

        try {
            CommonUtils.readAndRespondToProxyReceptorRequest(request, response, this.proxyGrantingTicketStorage);
            return false;
        } catch (final IOException e) {
            return super.requiresAuthentication(request, response);
        }
    }

    public final void setProxyReceptorUrl(final String proxyReceptorUrl) {
        this.proxyReceptorUrl = proxyReceptorUrl;
    }

    public final void setProxyGrantingTicketStorage(
            final ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
        this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
    }

    public final void setServiceProperties(final ServiceProperties serviceProperties) {
        this.artifactParameter = serviceProperties.getArtifactParameter();
    }
}
