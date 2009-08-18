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
	
	private Collection<String> toolsToCopy;
	private Collection<String> toolsToOmit;
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
	 * tools with the given ids.
	 */
	public CopyOptions(Collection<String> toolIds) {
		super();
		toolsToCopy = toolIds;
	}

	/**
	 * The content of tools with these id's is to be copied.
	 * If no tool id's are given all tool content that can be copied
	 * is copied, except the ones in toolsToOmit. 
	 * If this collection is empty, nothing is copied at all.
	 * @return the tools to copy.
	 */
	public Collection<String> getToolsToCopy() {
		return toolsToCopy;
	}
	
	/**
	 * The content of tools with these id's is to be copied.
	 * Leave null to copy everything except toolsToOmit, 
	 * pass an empty collection to copy nothing.
	 * @param toolIds
	 */
	public void setToolsToCopy(Collection<String> toolIds) {
		this.toolsToCopy = toolIds;
	}
	
	/**
	 * The content of the tools with these id's is not to be copied.
	 * An empty set or null both result in all content being copied, 
	 * unless toolsToCopy is not null.
	 * @see #getToolsToCopy(Collection)
	 */
	public Collection<String> getToolsToOmit() {
		return toolsToOmit;
	}

	/**
	 * The content of the tools with the given id's is not to be copied.
	 * An empty set or null both result in all content being copied, 
	 * unless toolsToCopy is not null.
	 * @param toolIds
	 * @see #setToolsToCopy(Collection)
	 */
	public void setToolsToOmit(Collection<String> toolIds) {
		this.toolsToOmit = toolIds;
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
	 * Use {@link #getToolsToCopy()} in stead.
	 * The content of tools with these id's is to be copied.
	 * If no tool id's are given all tool content that can be copied
	 * is copied, except the ones in toolsToOmit. 
	 * If this collection is empty, nothing is copied at all.
	 * @return the tools to copy.
	 */
	public Collection<String> getToolIds() {
		return toolsToCopy;
	}
	
	/**
	 * @deprecated
	 * Use {@link #setToolsToCopy(Collection)} in stead.
	 * The content of tools with these id's is to be copied.
	 * Leave null to copy everything except toolsToOmit, 
	 * pass an empty collection to copy nothing.
	 * @param toolIds
	 */
	public void setToolIds(Collection<String> toolIds) {
		this.toolsToCopy = toolIds;
	}
	

}
