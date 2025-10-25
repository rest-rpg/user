package com.rest_rpg.user

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.LinkedMultiValueMap
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@ContextConfiguration
@SpringBootTest(classes = UserApplication, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TestBase extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    <T> Response<T> httpPost(String url, Object request, Class<T> requiredType, Map customArgs = [:]) {
        Map args = [
                parameters  : new LinkedMultiValueMap<>()
        ]
        args << customArgs

        def requestPost = post(url)
                .content(asJsonString(request))
                .params(args.parameters as LinkedMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        def response = mvc.perform(requestPost).andReturn().response
        return new Response<T>(response, objectMapper, requiredType)
    }

    <T> Response<T> httpPut(String url, Object request, Class<T> requiredType, Map customArgs = [:]) {
        Map args = [
                parameters  : new LinkedMultiValueMap<>(),
                refreshToken: null,
                accessToken : null
        ]
        args << customArgs

        def requestPost = put(url)
                .content(asJsonString(request))
                .params(args.parameters as LinkedMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        if (args.refreshToken) {
            requestPost.cookie(new Cookie("jwt", args.refreshToken))
        }
        if (args.accessToken) {
            requestPost.header("Authorization", "Bearer " + args.accessToken)
        }

        def response = mvc.perform(requestPost).andReturn().response
        return new Response<T>(response, objectMapper, requiredType)
    }

    <T> Response<T> httpDelete(String url, Class<T> requiredType, Map customArgs = [:]) {
        Map args = [
                parameters  : new LinkedMultiValueMap<>(),
                refreshToken: null,
                accessToken : null
        ]
        args << customArgs

        def requestGet = delete(url)
                .params(args.parameters as LinkedMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        if (args.refreshToken) {
            requestGet.cookie(new Cookie("jwt", args.refreshToken))
        }
        if (args.accessToken) {
            requestGet.header("Authorization", "Bearer " + args.accessToken)
        }

        def response = mvc.perform(requestGet).andReturn().response
        return new Response<T>(response, objectMapper, requiredType)
    }

    <T> Response<T> httpGet(String url, Class<T> requiredType, Map customArgs = [:]) {
        Map args = [
                parameters  : new LinkedMultiValueMap<>(),
                refreshToken: null,
                accessToken : null
        ]
        args << customArgs

        def requestGet = get(url)
                .params(args.parameters as LinkedMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        if (args.refreshToken) {
            requestGet.cookie(new Cookie("jwt", args.refreshToken))
        }
        if (args.accessToken) {
            requestGet.header("Authorization", "Bearer " + args.accessToken)
        }

        def response = mvc.perform(requestGet).andReturn().response
        return new Response<T>(response, objectMapper, requiredType)
    }

    private static asJsonString(final Object obj) {
        try {
            def mapper = new ObjectMapper()
            def jsonContent = mapper.writeValueAsString(obj)
            return jsonContent
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }
}
