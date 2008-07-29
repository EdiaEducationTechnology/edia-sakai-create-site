var edia = edia || {};
edia.createsite = {};

(function(){
	
	edia.createsite.toggleTemplateDetailsVisibility = function() {
		var select = document.getElementById("templateSiteId");
		var templateId = select.options[select.selectedIndex].value
		var wrapper = document.getElementById("siteDetailWrapper");
		var detailDivs = wrapper.getElementsByTagName("div");
		var submitBtn = document.getElementById("submitBtn");
		if (!templateId) {
			submitBtn.disabled = "disabled";
		}
		else {
			submitBtn.disabled = "";
		}
		
		for(var index = 0; index < detailDivs.length; index++) {
			if (!templateId) {
				if (detailDivs[index].id == "noneSelected") {
					detailDivs[index].style.display = "inline";
				}
				else {
					detailDivs[index].style.display = "none";
				}
			}
			else {
				if (detailDivs[index].id == "detail_"+templateId ) {
					detailDivs[index].style.display = "inline";
				}
				else {
					detailDivs[index].style.display = "none";
				}
			}
		}
	}
	
})();
