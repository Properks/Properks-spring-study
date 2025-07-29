package com.example.springrestdocs.mock;

import jakarta.servlet.*;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import java.io.IOException;
import java.util.List;

public class TestFilterChain implements FilterChain {

    private final List<Filter> filters;
    private final int size;
    private int currentPosition;

    public TestFilterChain(Filter... filters) {
        this.filters = List.of(filters);
        this.size = filters.length;
        this.currentPosition = 0;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        if (currentPosition < size) {
            Filter filter = filters.get(this.currentPosition++);
            filter.doFilter(servletRequest, servletResponse, this);
        }
    }
}
