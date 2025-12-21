package io.granito.segovia.web

import java.util.concurrent.TimeUnit
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType
import org.springframework.http.CacheControl
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.resource.PathResourceResolver
import reactor.core.publisher.Mono
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.MapperFeature

private const val STATIC = "/META-INF/resources/"
private val INDEX = ClassPathResource("${STATIC}index.html")

@SpringBootApplication
@EnableHypermediaSupport(type = [HypermediaType.HAL])
@Lazy
open class WebConfig: WebFluxConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry)
    {
        registry
            .addResourceHandler("/**")
            .addResourceLocations("classpath:$STATIC")
            .setCacheControl(CacheControl.maxAge(4, TimeUnit.HOURS))
            .resourceChain(true)
            .addResolver(object: PathResourceResolver() {
                override fun getResource(path: String,
                    location: Resource): Mono<Resource>
                {
                    val resource = location.createRelative(path)

                    return Mono.just(if (resource.isReadable) resource
                        else INDEX)
                }
            })
    }

    @Bean
    open fun jacksonCustomizer() = JsonMapperBuilderCustomizer {
        builder -> builder
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .changeDefaultPropertyInclusion {
                it.withContentInclusion(JsonInclude.Include.NON_NULL)
            }
    }
}
