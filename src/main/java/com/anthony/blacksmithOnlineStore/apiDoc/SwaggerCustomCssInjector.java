package com.anthony.blacksmithOnlineStore.apiDoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import jakarta.servlet.http.HttpServletRequest;

public class SwaggerCustomCssInjector extends SwaggerIndexPageTransformer {
  public SwaggerCustomCssInjector(
    final SwaggerUiConfigProperties swaggerUiConfig,
    final SwaggerUiOAuthProperties swaggerUiOAuthProperties,
    final SwaggerWelcomeCommon swaggerWelcomeCommon,
    final ObjectMapperProvider objectMapperProvider) {
    super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
  }

  @Override
  public Resource transform(
    HttpServletRequest request,
    Resource resource,
    ResourceTransformerChain transformer) throws IOException {
    if ("index.html".equals(resource.getFilename())) {
      try (InputStream in = resource.getInputStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        String html = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        String transformedHtml = injectCss(html);
        return new TransformedResource(resource, transformedHtml.getBytes());
      }
    }
    return super.transform(request, resource, transformer);
  }

  private String injectCss(String html) {
    String cssPath = "/static/css/swagger-dark.css";
    return html.replace("</head>", "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssPath + "\" /></head>");
  }
}
