package com.example.msccspringtesting.infrastructure.adapters.aspectIT;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserSecurityContextFactory
        implements WithSecurityContextFactory<WithCustomMockUser> {

    private static final String tokenValue ="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOLVRKOFJFZi1pQ0VMZUVLa010cHVVT2hTQk9hdkRkejMxZGZrN1V1cEY4In0.eyJleHAiOjE2Njg5MjYxNTEsImlhdCI6MTY2ODkyNTg1MSwianRpIjoiODY1ZDc4YzgtZjZmOS00OTJkLThjMzctNWYwOTExMmI4Njg1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdC9yZWFsbXMvdGVzdC1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI1NzM5YmNkMy0xMmUwLTQyOGItOTQwZi1lNzMxZmY5ZjA1MTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzcHJpbmctYm9vdC1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiZDA0Y2ZiNmUtYWM3Yy00Mjc5LThiODAtMTgwZTdiMWRjMGUxIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLXRlc3QtcmVhbG0iLCJvZmZsaW5lX2FjY2VzcyIsIlBBWSIsInVtYV9hdXRob3JpemF0aW9uIiwiVVNFUiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImQwNGNmYjZlLWFjN2MtNDI3OS04YjgwLTE4MGU3YjFkYzBlMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiVGhlIFJvY2siLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiIxMDI0IiwiZ2l2ZW5fbmFtZSI6IlRoZSIsImZhbWlseV9uYW1lIjoiUm9jayIsImVtYWlsIjoiMTAyNEB0ZXN0LmNvbSJ9.DGChP_lICcf640RXY1KVL5DTjyYbdrVd64-Ngp9JsinRtrYwh-or2o1ti3J-QwahhVkDTfyuFZMzrUYv7rNd5eqYvPXT5BUDEmrcZV-Yui5OlFxrN4sZT2JtkdaCS5naj82KFepVcDkUYP6RpanqJL6j_7Vlgg_DkDXvL-eLHb6jUAjmJ1VZint8EOtgkYpjzsHc-BelXgcFfLPU-5V_mC1jFkCG9o4fIsx7JSMS_BphnHq81dl119g65XsQUVnNoCsAIgsD6vDtYcnwBVRg6R1jAsL4QwSxVLsjxGsYPMdc33LhO4Am-KODjdAAgcSjS7tJzPlFsHwuTOjZgh4QMA";
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Jwt jwt = Jwt.withTokenValue(tokenValue).claim("preferred_username", customUser.username())
                .header("alg", "HS256")
                .header("typ", "JWT")
                .build();

        Authentication authentication = new JwtAuthenticationToken(jwt);
        context.setAuthentication(authentication);
        return context;
    }
}
