/**
 * 
 */
package nl.edia.sakai.createsite.tool.controllers;

import nl.edia.sakai.createsite.api.CreateSiteService;
import nl.edia.sakai.createsite.tool.forms.EditSiteForm;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Maarten van Hoof
 *
 */
public class EditSiteController extends SimpleFormController implements Constants {
	
	private SiteService siteService;
	private CreateSiteService createSiteService;
	private static Log log = LogFactory.getLog(EditSiteController.class);

	/**
	 * 
	 */
	public EditSiteController() {
	}

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.setRequiredFields(new String[] {PARAM_TEMPLATE_SITE_ID, "title"});
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		EditSiteForm form = (EditSiteForm)command;
		String templateSiteId = request.getParameter(PARAM_TEMPLATE_SITE_ID);
		
		if (log.isDebugEnabled()) {
			log.debug("Creating a new site from template site with id '"+templateSiteId+"'.");
		}
		
		String siteId = createSiteService.createSiteFromTemplate(templateSiteId);
		
		Site site = siteService.getSite(siteId);
		site.setTitle(form.getTitle());
		site.setShortDescription(form.getShortDescription());
		site.setDescription(form.getDescription());
		site.setPublished("true".equals(form.getPublished()));
		
		// current user becomes maintainer
		if (site.getMaintainRole() != null) {
			site.addMember(SakaiUtils.getCurrentUserId(), site.getMaintainRole(), true, false);
		}
		else {
			log.error("Can't make the current user maintainer of the new site: no maintainer role defined.");
		}
		
		siteService.save(site);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(PARAM_SITE_ID, siteId);
		return new ModelAndView(getSuccessView(), model);
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		String templateSiteId = request.getParameter(PARAM_TEMPLATE_SITE_ID);
		Site templateSite =  siteService.getSite(templateSiteId);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(PARAM_TEMPLATE_SITE, templateSite);
		return model;		
	}

	@Override
	protected EditSiteForm formBackingObject(HttpServletRequest request) throws Exception {
		String templateSiteId = request.getParameter(PARAM_TEMPLATE_SITE_ID);
		Site templateSite =  siteService.getSite(templateSiteId);
		
		EditSiteForm form = new EditSiteForm();
		form.setTitle(templateSite.getTitle());
		form.setShortDescription(templateSite.getShortDescription());
		form.setDescription(templateSite.getDescription());
		
		return form;
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
