package com.nhnacademy.config;

import com.nhnacademy.Base;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

@Controller
@ComponentScan(basePackageClasses = Base.class, excludeFilters = {@ComponentScan.Filter(Controller.class)})
public class RootConfig {
}