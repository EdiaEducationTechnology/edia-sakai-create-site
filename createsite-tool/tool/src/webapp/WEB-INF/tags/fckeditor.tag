<%@ attribute name="texteareaid" required="true" %>
<script type="text/javascript" language="JavaScript">

  function chef_setupformattedtextarea(textarea_id)
  {
          var oFCKeditor = new FCKeditor(textarea_id);
    oFCKeditor.BasePath = "/library/editor/FCKeditor/";

                var courseId = "$vppa_site_collection_id";

                oFCKeditor.Config['ImageBrowserURL'] = oFCKeditor.BasePath + "editor/filemanager/browser/default/browser.html?Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector&Type=Image&CurrentFolder=" + courseId;
                oFCKeditor.Config['LinkBrowserURL'] = oFCKeditor.BasePath + "editor/filemanager/browser/default/browser.html?Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector&Type=Link&CurrentFolder=" + courseId;
                oFCKeditor.Config['FlashBrowserURL'] = oFCKeditor.BasePath + "editor/filemanager/browser/default/browser.html?Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector&Type=Flash&CurrentFolder=" + courseId;
                oFCKeditor.Config['ImageUploadURL'] = oFCKeditor.BasePath + "/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector?Type=Image&Command=QuickUpload&Type=Image&CurrentFolder=" + courseId;
                oFCKeditor.Config['FlashUploadURL'] = oFCKeditor.BasePath + "/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector?Type=Flash&Command=QuickUpload&Type=Flash&CurrentFolder=" + courseId;
                oFCKeditor.Config['LinkUploadURL'] = oFCKeditor.BasePath + "/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector?Type=File&Command=QuickUpload&Type=Link&CurrentFolder=" + courseId;
    oFCKeditor.Width  = "600" ;
    oFCKeditor.Height = "400" ;
                oFCKeditor.Config['CustomConfigurationsPath'] = "/library/editor/FCKeditor/config.js";
        oFCKeditor.ReplaceTextarea() ;
  }
  
</script>
<script type="text/javascript" defer="1">chef_setupformattedtextarea('${texteareaid}');</script>