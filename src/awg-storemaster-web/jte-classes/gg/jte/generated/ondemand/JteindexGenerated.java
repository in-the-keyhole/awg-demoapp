package gg.jte.generated.ondemand;
import com.awginc.storemaster.web.IndexModel;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,8,8,8,8,8,8,8,8,8,8,12,12,12,12,13,13,13,13,21,21,21,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, IndexModel model) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n    <head>\r\n        <meta charset=\"utf-8\" />\r\n        <title>AWG Store Master (Java)</title>\r\n        <base");
		var __jte_html_attribute_0 = model.basePath();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" href=\"");
			jteOutput.setContext("base", "href");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("base", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" />\r\n\r\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\r\n        <link rel=\"icon\" type=\"image/x-icon\" href=\"favicon.png\" />\r\n        <link rel=\"stylesheet\" crossorigin href=\"assets/index.css?v=");
		jteOutput.setContext("link", "href");
		jteOutput.writeUserContent(model.version());
		jteOutput.setContext("link", null);
		jteOutput.writeContent("\">\r\n        <script type=\"module\" crossorigin src=\"assets/index.js?v=");
		jteOutput.setContext("script", "src");
		jteOutput.writeUserContent(model.version());
		jteOutput.setContext("script", null);
		jteOutput.writeContent("\"></script>\r\n    </head>\r\n\r\n    <body>\r\n        <div id=\"root\"></div>\r\n    </body>\r\n\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		IndexModel model = (IndexModel)params.get("model");
		render(jteOutput, jteHtmlInterceptor, model);
	}
}
