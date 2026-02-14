package com.rest_rpg.user

import com.ms.user.model.UpdateOwnAccountRequest
import com.ms.user.model.UpdateOwnPasswordRequest
import com.ms.user.model.UserLite
import com.rest_rpg.TestBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@EmbeddedKafka(partitions = 1, brokerProperties = ['listeners=PLAINTEXT://localhost:9092', 'port=9092'])
class MeControllerTest extends TestBase {

    def baseUrl = '/user/self'

    @Autowired
    UserServiceHelper userServiceHelper

    void cleanup() {
        userServiceHelper.clean()
    }

    def "should get own account info"() {
        given:
            def user = userServiceHelper.createUser(username: 'john', email: 'john@example.com', enabled: true)
            authenticate(user.username)
        when:
            def response = httpGet(baseUrl, UserLite)
        then:
            response.status == HttpStatus.OK
            response.body != null
            response.body.username == user.username
            response.body.email == user.email
    }

    def "should update own account"() {
        given:
            def user = userServiceHelper.createUser(username: 'kate', email: 'kate@example.com', enabled: true)
            authenticate(user.username)
            def req = new UpdateOwnAccountRequest()
                    .email('kate.new@example.com')
                    .username('kate')
        when:
            def response = httpPut(baseUrl, req, UserLite)
        then:
            response.status == HttpStatus.OK
            response.body.username == user.username
            response.body.email == 'kate.new@example.com'
    }

    def "should update own password"() {
        given:
            def user = userServiceHelper.createUser(username: 'mike', email: 'mike@example.com', enabled: true, password: 'oldPass123')
            authenticate(user.username)
            def req = new UpdateOwnPasswordRequest()
                    .oldPassword('oldPass123')
                    .newPassword('newPass1234')
        when:
            def response = httpPut("$baseUrl/password", req, Void)
        then:
            response.status == HttpStatus.NO_CONTENT
    }

    private static void authenticate(String username) {
        def authentication = new UsernamePasswordAuthenticationToken(username, null, [])
        def context = SecurityContextHolder.createEmptyContext()
        context.setAuthentication(authentication)
        SecurityContextHolder.setContext(context)
    }
}
