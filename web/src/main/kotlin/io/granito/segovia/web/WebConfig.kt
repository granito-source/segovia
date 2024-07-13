package io.granito.segovia.web

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.MapperFeature
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
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
import java.util.concurrent.TimeUnit

private const val STATIC = "/META-INF/resources/"
private val INDEX = ClassPathResource("${STATIC}index.html")

@SpringBootConfiguration
@ComponentScan
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
    open fun objectMapperBuilderCustomizer() = Jackson2ObjectMapperBuilderCustomizer {
        it
            .failOnUnknownProperties(true)
            .featuresToEnable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .serializationInclusion(Include.NON_NULL)
    }
}
