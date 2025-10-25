package com.rest_rpg.command_line_app_startup_runner

import com.rest_rpg.user.TestBase
import com.rest_rpg.user.UserServiceHelper
import com.rest_rpg.user.config.CommandLineAppStartupRunner
import org.springframework.beans.factory.annotation.Autowired

class CommandLineAppStartupRunnerTest extends TestBase {

    @Autowired
    CommandLineAppStartupRunner runner

    @Autowired
    UserServiceHelper userServiceHelper

    def "should create default admin account"() {
        when:
            runner.run()
            def user = userServiceHelper.getUserByUsername("admin")
        then:
            user.username == "admin"
            user.email == "admin@gmail.com"
            user.password != "12345678"
    }
}
