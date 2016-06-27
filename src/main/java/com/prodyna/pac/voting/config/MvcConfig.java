/**
 *
 */
package com.prodyna.pac.voting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.com">PRODYNA AG</a>
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(final ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/403").setViewName("403");
    }
}
