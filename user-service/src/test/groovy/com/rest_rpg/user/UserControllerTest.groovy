package com.rest_rpg.user

import com.ms.user.model.CreateUserRequest
import com.ms.user.model.Role
import com.ms.user.model.UpdatePasswordRequest
import com.ms.user.model.UserLite
import com.ms.user.model.UserLitePage
import com.ms.user.model.UserUpdateRequest
import com.rest_rpg.TestBase
import com.ms.user.model.RegisterRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.kafka.test.context.EmbeddedKafka

import java.util.stream.IntStream

@EmbeddedKafka(partitions = 1, brokerProperties = ['listeners=PLAINTEXT://localhost:9092', 'port=9092'])
class UserControllerTest extends TestBase {

    def baseUrl = '/user'
    def registerUrl = "$baseUrl/register"
    def verifyUrl = { String code -> "/user/verify/$code" }

    @Autowired
    UserServiceHelper userServiceHelper

    void cleanup() {
        userServiceHelper.clean()
    }

    def "should register user"() {
        when:
            def request = new RegisterRequest()
                    .username("Michael1099")
                    .email("michael1099@gmail.com")
                    .password("12345678")
            def response = httpPost(registerUrl, request, Void)
        then:
            // TODO: Dodać sprawdzenie wysłania eventu kafka
            response.status == HttpStatus.NO_CONTENT
    }

    def "should verify not verified user"() {
        given:
            def user = userServiceHelper.createUser(enabled: enabled)
        when:
            def response = httpGet(verifyUrl(user.verificationCode), Void)
        then:
            response.status == httpStatus
        where:
            enabled || httpStatus
            false   || HttpStatus.NO_CONTENT
            true    || HttpStatus.FORBIDDEN
    }

    def "should create user by admin"() {
        when:
            def request = new CreateUserRequest()
                    .registerRequest(new RegisterRequest()
                            .username("Michael1099")
                            .email("michael1099@gmail.com")
                            .password("12345678"))
                    .role(Role.ADMIN)
            def response = httpPost(baseUrl, request, UserLite)
        then:
            response.status == HttpStatus.OK
            UserHelper.compare(request, response.body)
    }

    def "should get user by id"() {
        given:
            def created = userServiceHelper.createUser(username: 'alice', email: 'alice@example.com', enabled: true)
        when:
            def response = httpGet("$baseUrl/${created.id}", UserLite)
        then:
            response.status == HttpStatus.OK
            response.body != null
            response.body.id == created.id
            response.body.username == created.username
            response.body.email == created.email
    }

    def "should update user by admin"() {
        given:
            def created = userServiceHelper.createUser(username: 'bruce', email: 'bruce@example.com', enabled: true)
            def req = new UserUpdateRequest()
                    .email('bruce.new@example.com')
                    .username('Bruce')
                    .role(Role.ADMIN)
        when:
            def response = httpPut("$baseUrl/${created.id}", req, UserLite)
        then:
            response.status == HttpStatus.OK
            response.body.email == 'bruce.new@example.com'
            response.body.username == 'Bruce'
    }

    def "should update user password by admin"() {
        given:
            def created = userServiceHelper.createUser(username: 'charlie', email: 'charlie@example.com', enabled: true, password: 'oldPass123')
            def req = new UpdatePasswordRequest()
                    .newPassword('adminChanged1234')
        when:
            def response = httpPut("$baseUrl/${created.id}/password", req, Void)
        then:
            response.status == HttpStatus.NO_CONTENT
    }

    def "should delete user by admin"() {
        given:
            def created = userServiceHelper.createUser(username: 'dave', email: 'dave@example.com', enabled: true)
        when:
            def response = httpDelete("$baseUrl/${created.id}", Void)
        then:
            response.status == HttpStatus.NO_CONTENT
    }

    def "should find users by filter and pageable"() {
        given:
            IntStream.range(0, 7).forEach { i ->
                userServiceHelper.createUser(username: "user_${i}", email: "user_${i}@example.com", enabled: true)
            }
            def params = [
                    parameters: [
                            page: ['0'],
                            size: ['5']
                            // Dodaj tu pola filtra mapowane do UserFilter (np. username/email/enabled) jeśli potrzebne
                    ]
            ]
        when:
            def response = httpGet("$baseUrl/search", UserLitePage, params)
        then:
            response.status == HttpStatus.OK
            response.body != null
            response.body.data != null
            response.body.data.size() == 5
            response.body.meta.totalElements >= 7
    }

    def "should register user (204)"() {
        when:
            def request = new RegisterRequest()
                    .username("RegUser01")
                    .email("reguser01@example.com")
                    .password("12345678")
            def response = httpPost("$baseUrl/register", request, Void)
        then:
            response.status == HttpStatus.NO_CONTENT
    }

    def "should verify user (204)"() {
        given:
            def user = userServiceHelper.createUser(enabled: false)
        when:
            def response = httpGet("$baseUrl/verify/${user.verificationCode}", Void)
        then:
            response.status == HttpStatus.NO_CONTENT
    }

    def "should not create duplicate username (negative)"() {
        given:
            userServiceHelper.createUser(username: 'dupUser', email: 'dup@example.com', enabled: true)
            def req = new CreateUserRequest()
                    .registerRequest(new RegisterRequest()
                            .username('dupUser')
                            .email('dup2@example.com')
                            .password('12345678'))
                    .role(Role.ADMIN)
        when:
            def response = httpPost(baseUrl, req, UserLite)
        then:
            response.status.is4xxClientError()
    }
}
