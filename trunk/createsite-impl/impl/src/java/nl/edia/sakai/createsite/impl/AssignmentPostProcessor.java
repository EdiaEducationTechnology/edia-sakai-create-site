/*
 * Copyright (c) 2009 Edia (www.edia.nl)
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

import nl.edia.sakai.createsite.api.CopyOptions;
import nl.edia.sakai.createsite.api.EntityPostProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.assignment.api.Assignment;
import org.sakaiproject.assignment.api.AssignmentEdit;
import org.sakaiproject.assignment.api.AssignmentService;

import java.util.Iterator;

/**
 * Class AssignmentPostProcessor
 * 
 * @author maarten
 *
 */
public class AssignmentPostProcessor implements EntityPostProcessor {
	
	private AssignmentService assignmentService;
	
	private static Log LOG = LogFactory.getLog(AssignmentPostProcessor.class);

	/**
	 * @see nl.edia.sakai.createsite.api.EntityPostProcessor#myToolIds()
	 */
	public String[] myToolIds() {
		String[] toolIds = { "sakai.assignment", "sakai.assignment.grades" };
		return toolIds;
	}

	/**
	 * @see nl.edia.sakai.createsite.api.EntityPostProcessor#postProcessEntity(java.lang.String, nl.edia.sakai.createsite.api.CopyOptions)
	 */
	public void postProcessEntity(String fromContext, String toContext, CopyOptions options) {
		@SuppressWarnings("unchecked")
		Iterator<Assignment> assignments = assignmentService.getAssignmentsForContext(toContext);
		while (assignments.hasNext()) {
			Assignment assignment = assignments.next();
			try {
				AssignmentEdit assignmentsEdit = assignmentService.editAssignment(assignment.getReference());
				assignmentsEdit.setDraft(options.isCopyAssignmentsAsDraft());
				assignmentService.commitEdit(assignmentsEdit);
			}
			catch (Exception e) {
				LOG.debug("Exception while setting draft status of assignment "+ assignment.getId(), e);
			}
		}

	}

	public AssignmentService getAssignmentService() {
		return assignmentService;
	}

	public void setAssignmentService(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

}
