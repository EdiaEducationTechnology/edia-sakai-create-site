/**
 * 
 */
package nl.edia.sakai.createsite.tool.controllers;

import nl.edia.sakai.createsite.api.CreateSiteService;
import nl.edia.sakai.createsite.tool.forms.SelectTemplateForm;

import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Maarten van Hoof
 *
 */
public class SelectTemplateController extends SimpleFormController implements Constants {
	
	private CreateSiteService createSiteService;
	private SiteService siteService;

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		SelectTemplateForm form = (SelectTemplateForm)command;
		model.put(TEMPLATE_SITE_ID, form.getTemplateSiteId());
		
		return new ModelAndView(getSuccessView(), model);
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		List<String> templateSiteIds = createSiteService.listTemplateSites();
		List<Site> templateSites = new ArrayList<Site>(templateSiteIds.size());
		for (String siteId : templateSiteIds) {
			templateSites.add(siteService.getSite(siteId));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(TEMPLATE_SITES, templateSites);
		
		return model;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new SelectTemplateForm();
	}

	public CreateSiteService getCreateSiteService() {
		return createSiteService;
	}

	public void setCreateSiteService(CreateSiteService createSiteService) {
		this.createSiteService = createSiteService;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
	
	

}
