/*
 * Copyright (c) 2008 Edia (www.edia.nl)
 * 
 * Licensed under the Educational Community License version 1.0
 * 
 * This Original Work, including software, source code, documents,
 * or other related items, is being provided by the copyright holder(s)
 * subject to the terms of the Educational Community License. By
 * obtaining, using and/or copying this Original Work, you agree that you
 * have read, understand, and will comply with the following terms and
 * conditions of the Educational Community License:
 * 
 * Permission to use, copy, modify, merge, publish, distribute, and
 * sublicense this Original Work and its documentation, with or without
 * modification, for any purpose, and without fee or royalty to the
 * copyright holder(s) is hereby granted, provided that you include the
 * following on ALL copies of the Original Work or portions thereof,
 * including modifications or derivatives, that you make:
 * 
 * - The full text of the Educational Community License in a location viewable to
 * users of the redistributed or derivative work.
 * 
 * - Any pre-existing intellectual property disclaimers, notices, or terms and
 * conditions.
 * 
 * - Notice of any changes or modifications to the Original Work, including the
 * date the changes were made.
 * 
 * 
 * Any modifications of the Original Work must be distributed in such a manner as
 * to avoid any confusion with the Original Work of the copyright holders.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * The name and trademarks of copyright holder(s) may NOT be used
 * in advertising or publicity pertaining to the Original or Derivative
 * Works without specific, written prior permission. Title to copyright in
 * the Original Work and any associated documentation will at all times
 * remain with the copyright holders. 
 * 
 */
package nl.edia.sakai.createsite.impl;

import nl.edia.sakai.createsite.api.CreateSiteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.entity.api.EntityProducer;
import org.sakaiproject.entity.api.EntityTransferrer;
import org.sakaiproject.entity.cover.EntityManager;
import org.sakaiproject.exception.IdInvalidException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.IdUsedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.id.cover.IdManager;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SitePage;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.site.api.SiteService.SortType;
import org.sakaiproject.tool.api.Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Class CreateSiteServiceImpl.
 * @author Maarten van Hoof
 *
 */
public class CreateSiteServiceImpl implements CreateSiteService {
	
	private SiteService siteService;
	private ContentHostingService contentHostingService;
	private static Log log = LogFactory.getLog(CreateSiteServiceImpl.class);

	/**
	 * 
	 */
	public CreateSiteServiceImpl() {
	}

	/**
	 * @see nl.edia.sakai.createsite.api.CreateSiteService#listTemplateSites()
	 */
	@SuppressWarnings("unchecked")
	public List<String> listTemplateSites(List<String> siteTypes) {
		List<Site> templateSites = siteService.getSites(
				SiteService.SelectionType.ANY, null, null, null, SortType.TITLE_ASC, null);
		List<String> siteIds = new ArrayList<String>(templateSites.size());
		
		for (Site site : templateSites) {
			if (site.getId().startsWith(TEMPLATE_SITE_PREFIX) && site.isPublished() &&
					(siteTypes == null || siteTypes.isEmpty() || siteTypes.contains(site.getType()))) {				
				siteIds.add(site.getId());
			}
		}
		
		return siteIds;
	}

	/**
	 * @see nl.edia.sakai.createsite.api.CreateSiteService#createSiteFromTemplate(java.lang.String)
	 */
	public String createSiteFromTemplate(String templateSiteId) throws IdUnusedException, PermissionException {
		try {
			Site templateSite = siteService.getSite(templateSiteId);
			Site site = siteService.addSite(IdManager.createUuid(), templateSite);
			importToolContent(site.getId(), templateSite, true);
			return site.getId();
		}
		catch (IdUsedException e) {
			// not possible
			return null;
		}
		catch (IdInvalidException e) {
			// not possible
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void importToolContent(String newSiteId, Site templateSite, boolean bypassSecurity) {
		// import tool content
		
		if (bypassSecurity)
		{
			// importing from template, bypass the permission checking:
			// temporarily allow the user to read and write from assignments (asn.revise permission)
	        SecurityService.pushAdvisor(new SecurityAdvisor() {
                public SecurityAdvice isAllowed(String userId, String function, String reference)
                {
                    return SecurityAdvice.ALLOWED;
                }
            });
		}
				
		List<SitePage> pageList = templateSite.getPages();
		if (pageList != null) {
			for (SitePage page : pageList) {

				List<ToolConfiguration> pageToolList = page.getTools();
				if (pageToolList != null) {
					for (ToolConfiguration toolConfiguration : pageToolList) {
						Tool tool = toolConfiguration.getTool();
						if (tool != null) {
							String toolId = tool.getId();
							if (toolId.equalsIgnoreCase("sakai.resources")) {
								// handle resource tool specially
								transferCopyEntities(toolId,
										contentHostingService.getSiteCollection(templateSite.getId()),
										contentHostingService.getSiteCollection(newSiteId));
							} else {
								// other tools
								transferCopyEntities(toolId, templateSite.getId(), newSiteId);
							}
						}
						else {
							log.debug("No tool object in ToolConfiguration object '" + toolConfiguration.getTitle() + "'.");
						}
					}
				}
			}
		}

		if (bypassSecurity)
		{
			SecurityService.popAdvisor();
		}
	}
	

	/**
	 * Transfer a copy of all entities from another context for any entity
	 * producer that claims this tool id.
	 * 
	 * @param toolId
	 *            The tool id.
	 * @param fromContext
	 *            The context to import from.
	 * @param toContext
	 *            The context to import into.
	 */
	@SuppressWarnings("unchecked")
	private void transferCopyEntities(String toolId, String fromContext, String toContext) {
		// offer to all EntityProducers
		for (Iterator<EntityProducer> i = EntityManager.getEntityProducers().iterator(); i.hasNext();) {
			EntityProducer ep = i.next();
			if (ep instanceof EntityTransferrer) {
				try {
					EntityTransferrer et = (EntityTransferrer) ep;
					// if this producer claims this tool id
					if (Arrays.asList(et.myToolIds()).contains(toolId)) {
						et.transferCopyEntities(fromContext, toContext,	null);
					}
					else {
						log.debug("Can't copy content for tool "+toolId+" from: "+ fromContext + " to: " + toContext);
					}
				} 
				catch (Throwable t) {
					log.warn("Excetpion while copying content for tool "+toolId+" from: "+ fromContext + " to: " + toContext, t);
				}
			}
		}
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public ContentHostingService getContentHostingService() {
		return contentHostingService;
	}

	public void setContentHostingService(ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}

}
