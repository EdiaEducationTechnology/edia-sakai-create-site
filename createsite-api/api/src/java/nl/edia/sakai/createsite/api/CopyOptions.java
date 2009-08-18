package nl.edia.sakai.createsite.api;

import java.util.Collection;

/**
 * CopyOptions is an object that can be passed to the CreateSiteService to let that 
 * class know how to copy existing content from the template to the new site.
 * 
 * Which tools have their content copied is decided by toolsToCopy and toolsToOmit.
 * Caller of this class can choose which to use: use toolsToCopy to explicitly
 * set the id's of the tools that have their content copied, use toolsToOmit to 
 * copy everything but omit the content of the given tools.
 * 
 * @author Maarten van Hoof
 */
public class CopyOptions {
	
	private String newSiteTitle;
	private Collection<String> contentToCopy;
	private Collection<String> contentToOmit;
	private boolean copyAssignmentsAsDraft = true;
	private boolean copyUnpublishedAssesments = true;
	
	/**
	 * Create a CopyOptions object.
	 */
	public CopyOptions() {
		super();
	}

	/**
	 * Create a CopyOptions that copies content from the 
	 * tools with the given id's.
	 */
	public CopyOptions(Collection<String> toolIds) {
		super();
		contentToCopy = toolIds;
	}

	/**
	 * Returns which content to copy as a set of id's of tools that have their content copied.
	 * If no tool id's are given all tool content that can be copied
	 * is copied, except the ones in contentToOmit. 
	 * If this collection is empty, nothing is copied at all.
	 * @return the tools to copy.
	 */
	public Collection<String> getContentToCopy() {
		return contentToCopy;
	}
	
	/**
	 * Sets which content to copy as a set of id's of tools that have their content copied.
	 * Leave null to copy everything except contentToOmit, 
	 * pass an empty collection to copy nothing.
	 * @param toolIds
	 */
	public void setContentToCopy(Collection<String> toolIds) {
		this.contentToCopy = toolIds;
	}
	
	/**
	 * Returns which content is NOT copied as a set of id's of tools that are omitted in the copy process.
	 * An empty set or null both result in all content being copied, 
	 * unless contentToCopy is not null.
	 * @see #getToolsToCopy(Collection)
	 */
	public Collection<String> getContentToOmit() {
		return contentToOmit;
	}

	/**
	 * Sets which content NOT to copy as a set of id's of tools that are omitted in the copy process.
	 * An empty set or null both result in all content being copied, 
	 * unless contentToCopy is not null.
	 * @param toolIds
	 * @see #setContentToCopy(Collection)
	 */
	public void setContentToOmit(Collection<String> toolIds) {
		this.contentToOmit = toolIds;
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
	
	/**
	 * @deprecated
	 * Use {@link #getContentToCopy()} in stead.
	 * The content of tools with these id's is to be copied.
	 * If no tool id's are given all tool content that can be copied
	 * is copied, except the ones in toolsToOmit. 
	 * If this collection is empty, nothing is copied at all.
	 * @return the tools to copy.
	 */
	public Collection<String> getToolIds() {
		return contentToCopy;
	}
	
	/**
	 * @deprecated
	 * Use {@link #setContentToCopy(Collection)} in stead.
	 * The content of tools with these id's is to be copied.
	 * Leave null to copy everything except toolsToOmit, 
	 * pass an empty collection to copy nothing.
	 * @param toolIds
	 */
	public void setToolIds(Collection<String> toolIds) {
		this.contentToCopy = toolIds;
	}

	public String getNewSiteTitle() {
		return newSiteTitle;
	}

	public void setNewSiteTitle(String newSiteTitle) {
		this.newSiteTitle = newSiteTitle;
	}
	

}
