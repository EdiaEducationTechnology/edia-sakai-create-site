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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nl.edia.sakai.createsite.api.CopyOptions;
import nl.edia.sakai.createsite.api.CreateSiteService;
import nl.edia.sakai.createsite.api.EntityPostProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.EntityProducer;
import org.sakaiproject.entity.api.EntityTransferrer;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.exception.IdInvalidException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.IdUsedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.id.api.IdManager;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SitePage;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.site.api.SiteService.SortType;
import org.sakaiproject.tool.api.Tool;

/**
 * Class CreateSiteServiceImpl.
 * @author Maarten van Hoof
 *
 */
public class CreateSiteServiceImpl implements CreateSiteService {
	
	private SiteService siteService;
	private ContentHostingService contentHostingService;
	private static Log LOG = LogFactory.getLog(CreateSiteServiceImpl.class);
	private List<EntityPostProcessor> entityPostProcessors;
	private EntityManager entityManager;
	private IdManager idManager;
	private SecurityService securityService;
	

	/**
	 * 
	 */
	public CreateSiteServiceImpl() {
	}

	/**
	 * @see nl.edia.sakai.createsite.api.CreateSiteService#listTemplateSites()
	 */
	public List<String> listTemplateSites(List<String> siteTypes) {
		List<Site> templateSites = siteService.getSites(SiteService.SelectionType.ANY, null, null, null, SortType.TITLE_ASC, null);
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
	    return createSiteFromTemplate(templateSiteId, new CopyOptions());
	}
	
    /** 
     * @see nl.edia.sakai.createsite.api.CreateSiteService#createSiteFromTemplate(java.lang.String, java.util.Collection)
     */
    public String createSiteFromTemplate(String templateSiteId, CopyOptions options) throws IdUnusedException, PermissionException {
        try {
            return createSiteFromTemplate(idManager.createUuid(), templateSiteId, options);
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
    
    /**
     * @see nl.edia.sakai.createsite.api.CreateSiteService#createSiteFromTemplate(java.lang.String, java.lang.String, java.util.Collection)
     */
    public String createSiteFromTemplate(final String newId, String templateSiteId, CopyOptions options) throws IdUnusedException, IdInvalidException, IdUsedException, PermissionException {
		try {
			// Users of this tool can add sites, even if they do not have the proper permissions.
			// TODO maybe create a privilege for using this tool?
			securityService.pushAdvisor(new SecurityAdvisor() {
                public SecurityAdvice isAllowed(String userId, String function, String reference)
                {
                    if ((function.equals(SiteService.SECURE_ADD_SITE) || function.equals(SiteService.SECURE_UPDATE_SITE)) && reference.endsWith(newId)) {
                    	return SecurityAdvice.ALLOWED;
                    }
                    if (function.equals(AuthzGroupService.SECURE_ADD_AUTHZ_GROUP) && reference.endsWith(newId)) {
                    	return SecurityAdvice.ALLOWED;
                    }
                    return SecurityAdvice.PASS;
                }
            });
			
	    	Site templateSite = siteService.getSite(templateSiteId);
			Site site = siteService.addSite(newId, templateSite);
			if (options.getNewSiteTitle() != null) {
				site.setTitle(options.getNewSiteTitle());
			}
			copyToolContent(site.getId(), templateSite, options, true);
			siteService.save(site);
			return site.getId();
			
		}
		finally {
			securityService.popAdvisor();
		}
    }
    
	private void copyToolContent(String newSiteId, Site templateSite, CopyOptions options, boolean bypassSecurity) {
		// import tool content
		
		if (bypassSecurity)
		{
			// importing from template, bypass the permission checking:
			// temporarily allow the user to read and write from assignments (asn.revise permission)
	        securityService.pushAdvisor(new SecurityAdvisor() {
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
							if ((options.getContentToCopy() == null || options.getContentToCopy().contains(toolId)) &&
									!(options.getContentToOmit() != null && options.getContentToOmit().contains(toolId)) ) {
    							if (toolId.equalsIgnoreCase("sakai.resources")) {
    								// handle resource tool specially: create the collection
    								String newCollectionId = contentHostingService.getSiteCollection(newSiteId);
									if (!contentHostingService.isCollection(newCollectionId)) {
										ContentCollectionEdit collection;
										try {
											collection = contentHostingService.addCollection(newCollectionId);
											if (options.getNewSiteTitle() != null) {
												ResourcePropertiesEdit properties = collection.getPropertiesEdit();
												properties.addProperty(ResourceProperties.PROP_DISPLAY_NAME, options.getNewSiteTitle());
											}
											contentHostingService.commitCollection(collection);
										} 
										catch (Exception e) {
											LOG.warn("Can't create site collection.", e);
										}
									}
									// then transfer the data.
									transferCopyEntities(toolId,
											contentHostingService.getSiteCollection(templateSite.getId()),
											contentHostingService.getSiteCollection(newSiteId), options);
    							} 
    							else if (toolId.equalsIgnoreCase("sakai.iframe")) {
    								// do nothing. transferCopyEntities will end up adding new pages for each
    								// web content tool, but the web content pages have been correctly copied
    								// in siteService.addSite.
    							} 
    							else {
    								// other tools
    								transferCopyEntities(toolId, templateSite.getId(), newSiteId, options);
    							}
    							// post process these new entities.
    							postProcessEntities(toolId, templateSite.getId(), newSiteId, options);
							}
						}
						else {
							LOG.debug("No tool object in ToolConfiguration object '" + toolConfiguration.getTitle() + "'.");
						}
					}
				}
			}
		}

		if (bypassSecurity)
		{
			securityService.popAdvisor();
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
	private void transferCopyEntities(String toolId, String fromContext, String toContext, CopyOptions options) {
		// offer to all EntityProducers
		boolean copySucceeded = false;
		for (Iterator<EntityProducer> i = entityManager.getEntityProducers().iterator(); i.hasNext();) {
			EntityProducer ep = i.next();
			if (ep instanceof EntityTransferrer) {
				try {
					EntityTransferrer et = (EntityTransferrer) ep;
					// if this producer claims this tool id
					if (Arrays.asList(et.myToolIds()).contains(toolId)) {
						et.transferCopyEntities(fromContext, toContext,	null);
						copySucceeded = true;
					}
				} 
				catch (Throwable t) {
					LOG.warn("Excetpion while copying content for tool "+toolId+" from: "+ fromContext + " to: " + toContext, t);
				}
			}
		}
		if (copySucceeded) {
			LOG.debug("Succeeded in copying content for tool "+toolId+" from: "+ fromContext + " to: " + toContext);
		}
		else {
			LOG.debug("Can't copy content for tool "+toolId+" from: "+ fromContext + " to: " + toContext);
		}
	}

	private void postProcessEntities(String toolId, String fromContext, String toContext, CopyOptions options) {
		for (Iterator<EntityPostProcessor> iter = entityPostProcessors.iterator(); iter.hasNext();) {
			EntityPostProcessor postProcessor = iter.next();
			if (Arrays.asList(postProcessor.myToolIds()).contains(toolId)) {
				postProcessor.postProcessEntity(fromContext, toContext, options);
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

	public List<EntityPostProcessor> getEntityPostProcessors() {
		return entityPostProcessors;
	}

	public void setEntityPostProcessors(List<EntityPostProcessor> entityPostProcessors) {
		this.entityPostProcessors = entityPostProcessors;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @param idManager the idManager to set
	 */
	public void setIdManager(IdManager idManager) {
		this.idManager = idManager;
	}

	/**
	 * @param securityService the securityService to set
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}


}
