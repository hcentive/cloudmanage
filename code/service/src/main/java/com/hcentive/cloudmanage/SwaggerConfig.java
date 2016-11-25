package com.hcentive.cloudmanage;


import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(paths())
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .pathProvider(new BasePathAwareRelativePathProvider("/service"));
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Cloudmanage REST Api")
                .description("Cloudmanage api documentation using Swagger")
                .build();
    }

    private Predicate<String> paths(){
        Predicate<String> paths = or(
                regex("/audit.*"),
                regex("/billing.*"),
                regex("/build.*"),
                regex("/instances.*"),
                regex("/alarms.*")
        );
        return paths;
    }

    class BasePathAwareRelativePathProvider extends AbstractPathProvider {
        private String basePath;

        public BasePathAwareRelativePathProvider(String basePath) {
            this.basePath = basePath;
        }

        @Override
        protected String applicationPath() {
            return basePath;
        }

        @Override
        protected String getDocumentationPath() {
            return "/";
        }

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            return Paths.removeAdjacentForwardSlashes(
                    uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
        }
    }
}
