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

package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef._
import uk.gov.hmrc.perftests.support.GatlingSupport.convertHttpActionToSeq
import uk.gov.hmrc.perftests.support.RequestSupport.{authBaseUrl, baseUrl, saveCsrfToken}

object AuthorityRecord {

  private val pageUrl: String = s"$authBaseUrl/auth-login-stub/gg-sign-in"
//private val pageUrl: String = s"$authBaseUrl/auth-login-stub/beta-gg-sign-in"
  val redirectUrl: String     = s"$baseUrl/senior-accounting-officer/registration"

  def getAuthorityWizardPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Authority Wizard' page")
      .get(pageUrl)
      .check(status.is(200))
      .check(saveCsrfToken("authCsrfToken"))
  )

  def submitNewAuthorityRecord: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Submit form to create a new authority record")
      .post(pageUrl)
      .disableFollowRedirect
      .formParam("csrfToken", "${authCsrfToken}")
      .formParam("authorityId", "12345")
      .formParam("redirectionUrl", redirectUrl)
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Individual")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .check(status.is(303))
      .check(header("Location").is(redirectUrl))
      .check(header("Location").saveAs("redirectUrl"))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))
      .check(headerRegex("Set-Cookie", """mdtpdi=(.*)""").saveAs("mdtpdiCookie"))
  )
}
