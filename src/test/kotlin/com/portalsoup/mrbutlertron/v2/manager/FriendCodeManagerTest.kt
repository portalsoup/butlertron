package com.portalsoup.mrbutlertron.v2.manager

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.testng.annotations.Test


internal class FriendCodeManagerTest {

    @Test
    fun parseCode() {

        val code = "SW-4581-9510-6009"

        val message = "!friendcode add $code"

        val result = FriendCodeManager.parseCode(message)

        assertThat(result, equalTo(code))
    }
}