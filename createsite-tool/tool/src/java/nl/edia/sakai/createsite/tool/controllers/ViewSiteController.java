/**
 * 
 */
package nl.edia.sakai.createsite.tool.controllers;

import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nl.edia.sakai.createsite.tool.controllers.Constants.*;
import static nl.edia.sakai.createsite.tool.controllers.Constants.PARAM_TEMPLATE_SITE;
import static nl.edia.sakai.createsite.tool.controllers.Constants.PARAM_TEMPLATE_SITE_ID;

/**
 *
 */
@Controller
@RequestMapping("/viewsite.spring")
public class ViewSiteController  {

	@Autowired
	private SiteService siteService;

	public ViewSiteController() {
	}

	@RequestMapping
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
		String siteId = request.getParameter(PARAM_SITE_ID);
		Site site =  siteService.getSite(siteId);
		String siteUrl = site.getUrl();

		modelMap.put(PARAM_SITE, site);
		modelMap.put(PARAM_SITE_URL, siteUrl);
		
		return new ModelAndView("viewsite");
	}

}
