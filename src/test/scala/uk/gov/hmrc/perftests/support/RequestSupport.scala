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
import io.gatling.core.check.css.CssCheckType
import io.gatling.core.check.regex.RegexCheckType
import jodd.lagarto.dom.NodeSelector
import uk.gov.hmrc.perftests.support.GatlingSupport.convertChainToActions
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object RequestSupport extends ServicesConfiguration {
  val authBaseUrl: String                 = baseUrlFor("auth-login-stub")
  val companyBaseUrl: String              = baseUrlFor("incorporated-entity-identification-frontend")
  val baseUrl: String                     = baseUrlFor("senior-accounting-officer-registration-frontend")
  val registrationPage: String            = s"$baseUrl/senior-accounting-officer/registration"
  val contactDetailsPage: String          = s"$registrationPage/contact-details"
  val changeFirstContactNameUrl: String   = s"$contactDetailsPage/first/change-name"
  val changeFirstContactEmailUrl: String  = s"$contactDetailsPage/first/change-email"
  val changeSecondContactNameUrl: String  = s"$contactDetailsPage/second/change-name"
  val changeSecondContactEmailUrl: String = s"$contactDetailsPage/second/change-email"
  val checkYourAnswersPage: String        = s"$contactDetailsPage/check-your-answers"
  val mdtpCookie: String                  = "mdtp=${mdtpCookie}"
  val mdtpdiCookie: String                = "mdtpdi=${mdtpdiCookie}"

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

  def currentRequestUrl(session: Session): String =
    session("requestUrl").as[String]

  def currentRedirectUrl(session: Session): String =
    s"$baseUrl${session("redirectUrl").as[String]}"

  def assertAllValuesPresentInSelector(
    selector: String,
    expectedValues: Set[String]
  ): CheckBuilder.Final[CssCheckType, NodeSelector] = css(selector).findAll
    .transform { values =>
      val trimmed = values.map(_.trim)
      expectedValues.subsetOf(trimmed.toSet) // return 'true' if all value found
    }
    .is(true)
}
