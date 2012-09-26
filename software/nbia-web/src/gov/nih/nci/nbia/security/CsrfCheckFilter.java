package gov.nih.nci.nbia.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import gov.nih.nci.nbia.util.NCIAConfig;

/**
 * Taken from sun software forums
 *
 */
public class CsrfCheckFilter implements Filter {
    final static Logger logger = Logger.getLogger(CsrfCheckFilter.class);

    public CsrfCheckFilter() { // called once. no method arguments allowed
        // here!
    }

    public void init(FilterConfig conf) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request,
    		             ServletResponse response,
    		             FilterChain chain) throws IOException, ServletException {

        HttpServletRequest hreq = (HttpServletRequest) request;
        String currentPage = hreq.getServletPath();
        String referer = hreq.getHeader("referer");
        String serverLoc=NCIAConfig.getImageServerUrl();
        String url = hreq.getRequestURL().toString();
        
        System.out.println("$$$$$$$$$$$referer= "+ referer);
        System.out.println("$$$$$$$$$$$current req"+  currentPage);
        System.out.println("$$$$$$$$$$$url="+url);
        
        
        if (!shouldApplyFilter(currentPage)) {
        	chain.doFilter(request, response);
        }
        else if ((referer == null)) {
        	chain.doFilter(request, response);
        }
        else if ((referer != null) && referer.startsWith(serverLoc)) {
        	chain.doFilter(request, response);
        }
        else if (referer != null && !referer.startsWith(serverLoc)){
        	System.out.print("CSRF attack!");
        	((HttpServletResponse)response).sendRedirect(serverLoc+"/ncia/csrfErrorPage.jsp?hiddenSite="+referer);
        }
        else {
        	System.out.println("?????????how reached here url="+url);
        	System.out.println("??????????how reached here referer="+referer);
        }
    }
    

    /**
     * Based upon the path of the request, does CSRF (referer) check apply to the
     * path.  Mostly yes, but some pre-login screens shouldn't be secured.
     *
     */
    private boolean shouldApplyFilter(String currentPage) {
        return !currentPage.endsWith("login.jsf")&&
        	   !currentPage.endsWith("welcome.jsp")&&
               !currentPage.endsWith("csrfErrorPage.jsp");
    }
}
