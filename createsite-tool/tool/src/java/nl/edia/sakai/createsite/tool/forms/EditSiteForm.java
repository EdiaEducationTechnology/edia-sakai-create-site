/**
 * 
 */
package nl.edia.sakai.createsite.tool.forms;

/**
 * @author Maarten van Hoof
 *
 */
public class EditSiteForm {
	
	private String title;
	private String shortDescription;
	private String description;
	// "true" or "false" 
	private String published;

	/**
	 * 
	 */
	public EditSiteForm() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String longDescription) {
		this.description = longDescription;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

}
