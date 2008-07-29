/**
 * 
 */
package nl.edia.sakai.createsite.tool.controllers;

import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Maarten van Hoof
 *
 */
public class ViewSiteController extends ParameterizableViewController implements Constants {
	
	private SiteService siteService;

	public ViewSiteController() {
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String siteId = request.getParameter(SITE_ID);
		Site site =  siteService.getSite(siteId);
		String siteUrl = request.getScheme() +"://"+ request.getServerName() + 
			(request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + "/portal/site/" + site.getId();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(SITE, site);
		model.put(SITE_URL, siteUrl);
		
		return new ModelAndView(getViewName(), model);
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

}
