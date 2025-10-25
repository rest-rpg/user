package com.rest_rpg.user

import org.openapitools.model.RegisterRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.kafka.test.context.EmbeddedKafka
import spock.lang.Unroll

@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class UserControllerTest extends TestBase {

    def baseUrl = "/user"
    def verifyUrl = { String code -> baseUrl + "/verify/" + code }

//    KafkaTemplate kafkaTemplate = Mock()

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
            def response = httpPost(baseUrl, request, Void)
        then:
//            1 * kafkaTemplate.send(SendVerificationEmailEvent.TOPIC_NAME,)
            response.status == HttpStatus.OK
    }

    @Unroll
    def "should verify not verified user"() {
        given:
            def user = userServiceHelper.createUser(enabled: enabled)
        when:
            def response = httpGet(verifyUrl(user.verificationCode), Void)
        then:
            response.status == httpStatus
        where:
            enabled || httpStatus
            false   || HttpStatus.OK
            true    || HttpStatus.FORBIDDEN
    }
}
