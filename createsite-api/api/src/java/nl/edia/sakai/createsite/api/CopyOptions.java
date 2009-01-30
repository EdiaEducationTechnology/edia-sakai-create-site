package nl.edia.sakai.createsite.api;

import java.util.Collection;

/**
 * CopyOptions is an object that can be passed to the CreateSiteService to let that 
 * class know how to copy existing content from the template to the new site. 
 * 
 * @author Maarten van Hoof
 */
public class CopyOptions {
	
	private Collection<String> toolIds;
	private boolean copyAssignmentsAsDraft = true;
	private boolean copyUnpublishedAssesments = true;
	
	/**
	 * Create a CopyOptions object that has null toolIds.
	 */
	public CopyOptions() {
		super();
	}

	/**
	 * Create a CopyOptions object for copying the given tool ids.
	 * @param toolIds
	 */
	public CopyOptions(Collection<String> toolIds) {
		super();
		this.toolIds = toolIds;
	}

	/**
	 * Returns a collection of Id's of tools to copy.
	 * If no tool ids are given (when this method returns null) all tool content that can be copied
	 * is copied. If this collection is empty, nothing is copied at all.
	 * @return the tools to copy.
	 */
	public Collection<String> getToolIds() {
		return toolIds;
	}
	
	/**
	 * Set the id's of the tools to copy.
	 * Leave null to copy everything, pass an empty collection to copy nothing.
	 * @param toolIds
	 */
	public void setToolIds(Collection<String> toolIds) {
		this.toolIds = toolIds;
	}
	
	/**
	 * Copy assignments to the new site as draft.
	 * @return
	 */
	public boolean isCopyAssignmentsAsDraft() {
		return copyAssignmentsAsDraft;
	}
	
	/**
	 * Set whether assignments should be copied as draft. Default is true.
	 * @param copyAssignmentsAsDraft
	 */
	public void setCopyAssignmentsAsDraft(boolean copyAssignmentsAsDraft) {
		this.copyAssignmentsAsDraft = copyAssignmentsAsDraft;
	}
	
	/**
	 * Copy assessments that haven't been published to the new site.
	 * @return
	 */
	public boolean isCopyUnpublishedAssesments() {
		return copyUnpublishedAssesments;
	}
	
	/**
	 * Set whether unpublished assesments should be copied to the new site.
	 * @param copyUnpublishedAssesments
	 */
	public void setCopyUnpublishedAssesments(boolean copyUnpublishedAssesments) {
		this.copyUnpublishedAssesments = copyUnpublishedAssesments;
	}
	

}
