/**
 * 
 */
package nl.edia.sakai.createsite.tool.controllers;

import nl.edia.sakai.createsite.api.CopyOptions;
import nl.edia.sakai.createsite.api.CreateSiteService;
import nl.edia.sakai.createsite.tool.forms.EditSiteForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static nl.edia.sakai.createsite.tool.controllers.Constants.PARAM_SITE_ID;
import static nl.edia.sakai.createsite.tool.controllers.Constants.PARAM_TEMPLATE_SITE;
import static nl.edia.sakai.createsite.tool.controllers.Constants.PARAM_TEMPLATE_SITE_ID;

/**
 * @author Maarten van Hoof
 *
 */
@Controller
@RequestMapping("/editsite.spring")
public class EditSiteController {

	@Autowired
	private SiteService siteService;
	@Autowired
	private CreateSiteService createSiteService;

	private static Log log = LogFactory.getLog(EditSiteController.class);

	/**
	 * 
	 */
	public EditSiteController() {
	}

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.setRequiredFields(new String[] {PARAM_TEMPLATE_SITE_ID, "title"});
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onSubmit(HttpServletRequest request, @ModelAttribute("command") EditSiteForm form, SessionStatus status) throws Exception {
		String templateSiteId = request.getParameter(PARAM_TEMPLATE_SITE_ID);
		
		if (log.isDebugEnabled()) {
			log.debug("Creating a new site from template site with id '"+templateSiteId+"'.");
		}
		
		// TODO makes this configurable by the end user.
		CopyOptions options = new CopyOptions();
		options.setNewSiteTitle(form.getTitle());
		options.setCopyAssignmentsAsDraft(false);
		options.setContentToOmit(Arrays.asList(new String[] {"sakai.announcements", "sakai.schedule"}));
		
		String siteId = createSiteService.createSiteFromTemplate(templateSiteId, options);

		Site site = siteService.getSite(siteId);
		site.setShortDescription(form.getShortDescription());
		site.setDescription(form.getDescription());
		site.setPublished("true".equals(form.getPublished()));
		if ("true".equals(form.getJoinable())) {
			if (site.getJoinerRole() != null && site.getJoinerRole().trim().length() > 0) {
				site.setJoinable(true);
			}
			else {
				log.warn("User selected joinable, but there's no joiner role. Site not made joinable.");
			}
		}
		else {
			site.setJoinable(false);
		}
		
		siteService.save(site);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(PARAM_SITE_ID, siteId);
		status.setComplete();
		return  new ModelAndView("redirect:viewsite.spring", model);
	}

	@ModelAttribute(PARAM_TEMPLATE_SITE)
	protected Site getTemplateSite(HttpServletRequest request) throws Exception {
		String templateSiteId = request.getParameter(PARAM_TEMPLATE_SITE_ID);
		Site templateSite =  siteService.getSite(templateSiteId);
		
		return templateSite;
	}

	@RequestMapping(method = RequestMethod.GET)
	protected String formBackingObject(HttpServletRequest request, ModelMap model) throws Exception {
		String templateSiteId = request.getParameter(PARAM_TEMPLATE_SITE_ID);
		Site templateSite =  siteService.getSite(templateSiteId);
		
		EditSiteForm form = new EditSiteForm();
		// Remove the term 'template'. This is for the community manager.
		if (templateSite.getTitle() != null) {
			form.setTitle(templateSite.getTitle().replaceAll(" [Tt]emplate ", " "));
		}
		if (templateSite.getShortDescription() != null) {
			form.setShortDescription(templateSite.getShortDescription().replaceAll("The template ", "A "));
		}
		if (templateSite.getDescription() != null) {
			form.setDescription(templateSite.getDescription().replaceAll("The template ", "A "));
		}

		model.put("command", form);
		
		return "editsite";
	}

	public void setCreateSiteService(CreateSiteService createSiteService) {
		this.createSiteService = createSiteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

}
