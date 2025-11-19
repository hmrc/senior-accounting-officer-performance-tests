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
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.css.CssCheckType
import jodd.lagarto.dom.NodeSelector
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object RequestSupport extends ServicesConfiguration {
  val authBaseUrl: String                 = baseUrlFor("auth-login-stub")
  val companyBaseUrl: String              = baseUrlFor("incorporated-entity-identification-frontend")
  val baseUrl: String                     = baseUrlFor("senior-accounting-officer-registration-frontend")
  val authorityWizardPageUrl: String      = s"$authBaseUrl/auth-login-stub/gg-sign-in"
  val registrationPageUrl: String         = s"$baseUrl/senior-accounting-officer/registration"
  val businessMatchUrl: String            = s"$registrationPageUrl/business-match"
  val contactDetailsPageUrl: String       = s"$registrationPageUrl/contact-details"
  val changeFirstContactNameUrl: String   = s"$contactDetailsPageUrl/first/change-name"
  val changeFirstContactEmailUrl: String  = s"$contactDetailsPageUrl/first/change-email"
  val changeSecondContactNameUrl: String  = s"$contactDetailsPageUrl/second/change-name"
  val changeSecondContactEmailUrl: String = s"$contactDetailsPageUrl/second/change-email"
  val checkYourAnswersUrl: String         = s"$contactDetailsPageUrl/check-your-answers"
  val mdtpCookie: String                  = "mdtp=${mdtpCookie}"
  val mdtpdiCookie: String                = "mdtpdi=${mdtpdiCookie}"

  def extractAndSaveCsrfToken(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("input[name=csrfToken]", "value").exists.saveAs("csrfToken")

  def currentRequestUrl(session: Session): String =
    session("requestUrl").as[String]

  def currentRedirectUrl(session: Session): String = {
    val redirectUrl = session("redirectUrl").as[String]
    if (redirectUrl.startsWith("http")) {
      redirectUrl
    } else s"$baseUrl$redirectUrl"
  }

  def areAllContactsAddedUrl(session: Session): String =
    session("areAllContactsAddedUrl").as[String]

  def assertAllValuesPresentInSelector(
    selector: String,
    expectedValues: Set[String]
  ): CheckBuilder.Final[CssCheckType, NodeSelector] = css(selector).findAll
    .transform(_.map(_.trim).toSet)
    .is(expectedValues)

}
