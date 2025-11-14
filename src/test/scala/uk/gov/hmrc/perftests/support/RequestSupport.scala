/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.support

import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.regex.RegexCheckType
import io.gatling.http.Predef.currentLocation
import uk.gov.hmrc.perftests.support.GatlingSupport.convertChainToActions
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object RequestSupport extends ServicesConfiguration {
  val authBaseUrl: String    = baseUrlFor("auth-login-stub")
  val companyBaseUrl: String = baseUrlFor("incorporated-entity-identification-frontend")
  val baseUrl: String        = baseUrlFor("senior-accounting-officer-registration-frontend")
  val mdtpCookie: String     = "mdtp=${mdtpCookie}"
  val mdtpdiCookie: String   = "mdtpdi=${mdtpdiCookie}"

  def extractAndSaveCsrfToken(): CheckBuilder.Final[RegexCheckType, String] =
    regex(s"""name="csrfToken" value="([^"]+)""").saveAs("csrfToken")

  def saveRedirect: Seq[ActionBuilder] = convertChainToActions(
    exec { session =>
      val user     = "testUser"
      val redirect = session("redirectUrl").as[String]
      RedirectStore.set(user, redirect)
      session.set("redirectUrl", redirect)
    }
  )

  // REMOVE THIS METHOD WHEN TICKET IS READY FOR REVIEW
  def logSessionInfo: Seq[ActionBuilder] = convertChainToActions(exec { session =>
    println("========================================================================")
    println("DEBUG LOG")
    println("========================================================================")
    println("Request URL: " + session("Request URL").asOption[String])
    println("CSRF Token: " + session("csrfToken").asOption[String])
    println("BASE URL: " + session(s"$baseUrl").asOption[String])
    println("POST URL: " + session("postUrl").asOption[String])
    println("REDIRECT_URL: " + session("redirectUrl").asOption[String])
    println("CURRENT URL: " + session("currentUrl").asOption[String])
    println("MDTP Cookie: " + session("mdtpCookie").asOption[String])
    println("MDTPDI Cookie: " + session("mdtpdiCookie").asOption[String])

    println("BODY: " + session("responseBody").asOption[String])

    println("========================================================================")
    println("========================================================================")
    session
  })

}
