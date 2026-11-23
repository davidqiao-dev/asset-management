package com.qiao.assetmanagement.security;

import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public final class ErrorResponses {
    public static void write(HttpServletResponse response
            , int status
            , String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(Map.of("error", message)));
    }
}
