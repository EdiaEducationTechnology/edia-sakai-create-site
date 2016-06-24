/**
 * 
 */
package nl.edia.sakai.createsite.tool.controllers;

import nl.edia.sakai.createsite.api.CreateSiteService;
import nl.edia.sakai.createsite.tool.forms.SelectTemplateForm;
import nl.edia.sakai.tool.util.SakaiUtils;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static nl.edia.sakai.createsite.tool.controllers.Constants.CONFIG_TEMPLATE_IDS;
import static nl.edia.sakai.createsite.tool.controllers.Constants.CONFIG_TEMPLATE_TYPES;
import static nl.edia.sakai.createsite.tool.controllers.Constants.PARAM_TEMPLATE_SITES;
import static nl.edia.sakai.createsite.tool.controllers.Constants.PARAM_TEMPLATE_SITE_ID;

/**
 *
 */

@Controller
@RequestMapping("selecttemplate.spring")
public class SelectTemplateController {

	@Autowired
	private CreateSiteService createSiteService;

	@Autowired
	private SiteService siteService;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onSubmit(SelectTemplateForm form) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put(PARAM_TEMPLATE_SITE_ID, form.getTemplateSiteId());
		
		return new ModelAndView("redirect:editsite.spring", model);
	}

	@ModelAttribute(PARAM_TEMPLATE_SITES)
	public List<Site> referenceData(HttpServletRequest request) throws Exception {
		// if templates are defined, use these.
		List<String> templateSiteIds = getTemplateIds();
		if (templateSiteIds == null) {
			// if not, ask the service for templates.
			templateSiteIds = createSiteService.listTemplateSites(getTemplateTypes());
		}
		
		List<Site> templateSites = new ArrayList<Site>(templateSiteIds.size());
		for (String siteId : templateSiteIds) {
			templateSites.add(siteService.getSite(siteId));
		}
		
		return templateSites;
	}
	
	/**
	 * Get the list of template sites configured for this tool, or null if not configured.
	 * @return the ids of sites used as templates.
	 */
	private List<String> getTemplateIds() {
		List<String> templateIds = null;
		String templateTypeIds = SakaiUtils.getConfigValue(CONFIG_TEMPLATE_IDS);
		if (templateTypeIds != null && templateTypeIds.trim().length() != 0) {
			templateIds = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(templateTypeIds, ",");
			while (tokenizer.hasMoreTokens()) {
				templateIds.add(tokenizer.nextToken().trim());
			}
		}
		return templateIds;
	}
	
	/**
	 * Get the list of template site types configured for this tool, or null if 
	 * not configured. 
	 * @return
	 */
	private List<String> getTemplateTypes() {
		List<String> templateTypes = null;
		String templateTypeIds = SakaiUtils.getConfigValue(CONFIG_TEMPLATE_TYPES);
		if (templateTypeIds != null && templateTypeIds.trim().length() != 0) {
			templateTypes = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(templateTypeIds, ",");
			while (tokenizer.hasMoreTokens()) {
				templateTypes.add(tokenizer.nextToken().trim());
			}
		}
		return templateTypes;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String formBackingObject(HttpServletRequest request, ModelMap model) throws Exception {
		model.put("command", new SelectTemplateForm());
		return "selecttemplate";
	}

}
