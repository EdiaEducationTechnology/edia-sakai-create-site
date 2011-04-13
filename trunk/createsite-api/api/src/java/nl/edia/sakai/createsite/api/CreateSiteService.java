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
package nl.edia.sakai.createsite.api;

import java.util.Collection;
import java.util.List;

import org.sakaiproject.exception.IdInvalidException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.IdUsedException;
import org.sakaiproject.exception.PermissionException;

/**
 * Class CreateSiteService.
 * @author Maarten van Hoof
 *
 */
public interface CreateSiteService {
	
	public static final String TEMPLATE_SITE_PREFIX = "template";
	
	/**
	 * Return a list of sites that are considered template sites.
	 * The tool can either be configured with a set of template site ids
	 * or with a set of template site types. In the latter case, this method
	 * is used to retrieve the actual template site ids.
	 *  
	 * @param a list of site types to consider, or null or empty list for all types.
	 * @return a List of site ids.
	 */
	public List<String> listTemplateSites(List<String> siteTypes);
	
	/**
	 * Create a new site, using the site with the given id as template. 
	 * This method makes a deep copy of all the available entities in the template. 
	 * If you want to copy only a subselection of tools, please use {@link #createSiteFromTemplate(String, Collection)}. 
	 * This method is equivalent to <code> createSiteFromTemplate(templateSiteId, null) </code>
	 * @param templateSiteId the id of the site to use as template.
	 * @return the id of the new site.
	 * @throws IdUnusedException when the given template site id does not exist.
	 * @throws PermissionException if the current user does not have the right to create a site.
	 */
	public String createSiteFromTemplate(String templateSiteId) throws IdUnusedException, PermissionException;
	
	/**
	 * Create a new site, using the site with the given id as template. 
	 * Use the CopyOptions object to tell this CreateSiteService what to copy from the template to the new site and how.
	 * @param templateSiteId the id of the template
	 * @param options what to copy and how to copy it.
	 * @return the id of the new site.
	 * @throws IdUnusedException, if the template does not exist.
	 * @throws PermissionException, if the user does not have permission
	 * @see CopyOptions
	 */
	public String createSiteFromTemplate(String templateSiteId, CopyOptions options) throws IdUnusedException, PermissionException;

	/**
	 * Create a new site, using the site with the given id as template and with the given tool id.
	 * Use the CopyOptions object to tell this CreateSiteService what to copy from the template to the new site and how.
	 * @param siteId the id of the new site to create
	 * @param templateSiteId the id of the template
	 * @param options what to copy and how to copy it.
	 * @return the id of the new site.
	 * @throws IdUnusedException if the template does not exist.
	 * @throws PermissionException if the current user does not have the right to create a site.
	 * @throws IdUsedException if the given siteId already exists
	 * @throws IdInvalidException if the given siteId is invalid
	 * @see CopyOptions
	 */
    public String createSiteFromTemplate(String siteId, String templateSiteId, CopyOptions options) throws IdUnusedException, PermissionException, IdInvalidException, IdUsedException;
}
