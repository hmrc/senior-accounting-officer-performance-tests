/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.sao.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.perftests.sao.Request_Helper._

object AuthLoginRequests {

  private val redirectUrl: String = baseUrl + "/registration"

  def navigateToGrsToggle: HttpRequestBuilder = http("Navigate to GRS Toggle")
    .get("http://localhost:10057/senior-accounting-officer/registration/test-only/feature-toggle")
    .check(status.is(200))
    .check(saveCsrfToken)

  def toggleGrsOff: HttpRequestBuilder = http("Toggle GRS Off")
    .post("http://localhost:10057/senior-accounting-officer/registration/test-only/feature-toggle")
    .formParam("csrfToken", "${csrfToken}")
//    .check(bodyString.transformOption{ body =>
//      println("POST response body:|n" + body)
//      Some(body)
//    })
    .check(status.is(200))
//    .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))


  def navigateToAuthStubPage: HttpRequestBuilder = http("Navigate to Auth Stub Page")
    .get(s"$authUrl")
    .check(status.is(200))
    .check(saveCsrfToken)

  def submitAuthStub: HttpRequestBuilder = http("Submit Auth Stub")
    .post(s"$authUrl")
    .formParam("csrfToken", "${csrfToken}")
    .formParam("redirectionUrl", s"$redirectUrl")
    .formParam("credentialStrength", "strong")
    .formParam("confidenceLevel", "50")
    .formParam("authorityId", "12345")
    .formParam("affinityGroup", "Individual")
    .formParam("email", "user@test.com")
    .formParam("credentialRole", "User")
    .check(status.is(303))
    .check(header("Location").is(s"$redirectUrl"))
    .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))

  def navigateToRegistrationPage: HttpRequestBuilder = http("Registration Page")
    .get(redirectUrl)
    .header("Cookie", authCookie)
    .check(status.is(200))
    .check(bodyString.transformOption { body =>
      println("POST response body:\n" + body)
      Some(body)
    })
}
