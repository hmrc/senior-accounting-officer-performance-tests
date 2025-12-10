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
  val authBaseUrl: String                    = baseUrlFor("auth-login-stub")
  val companyBaseUrl: String                 = baseUrlFor("incorporated-entity-identification-frontend")
  val baseUrl: String                        = baseUrlFor("senior-accounting-officer-registration-frontend")
  val authorityWizardPageUrl: String         = s"$authBaseUrl/auth-login-stub/gg-sign-in"
  val registrationPageUrl: String            = s"$baseUrl/senior-accounting-officer/registration"
  val registrationCompletePageUrl: String    = s"$registrationPageUrl/registration-complete"
  val businessMatchUrl: String               = s"$registrationPageUrl/business-match"
  val contactDetailsPageUrl: String          = s"$registrationPageUrl/contact-details"
  val addFirstContactNameUrl: String         = s"$contactDetailsPageUrl/first/name"
  val addFirstContactEmailUrl: String        = s"$contactDetailsPageUrl/first/email"
  val addAnotherContactPageUrl: String       = s"$contactDetailsPageUrl/first/add-another"
  val addSecondContactNameUrl: String        = s"$contactDetailsPageUrl/second/name"
  val addSecondContactEmailUrl: String       = s"$contactDetailsPageUrl/second/email"
  val changeFirstContactNameUrl: String      = s"$contactDetailsPageUrl/first/change-name"
  val changeFirstContactEmailUrl: String     = s"$contactDetailsPageUrl/first/change-email"
  val changeSecondContactNameUrl: String     = s"$contactDetailsPageUrl/second/change-name"
  val changeSecondContactEmailUrl: String    = s"$contactDetailsPageUrl/second/change-email"
  val checkYourAnswersUrl: String            = s"$contactDetailsPageUrl/check-your-answers"
  val grsStubPathSegment: String             = "/test-only/grs-stub"
  val businessMatchResultPathSegment: String =
    "/senior-accounting-officer/registration/business-match/result?journeyId="
  val redirectUrlKey: String                 = "redirectUrl"
  val csrfTokenKey: String                   = "csrfToken"
  val mdtpCookieKey: String                  = "mdtpCookie"
  val mdtpdiCookieKey: String                = "mdtpdiCookie"
  val mdtpCookieValue: String                = "mdtp=${mdtpCookie}"
  val mdtpdiCookieValue: String              = "mdtpdi=${mdtpdiCookie}"
  val journeyIdKey: String                   = "journeyId"
  val journeyIdRegexPattern: String          = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

  def saveCsrfToken(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("input[name=csrfToken]", "value").exists.saveAs(csrfTokenKey)

  def csrfTokenFromSession(session: Session): String = session(csrfTokenKey).as[String]

  def redirectUrlFromSession(session: Session): String = {
    val redirectUrl = session(redirectUrlKey).as[String]
    if (redirectUrl.startsWith("http")) {
      redirectUrl
    } else s"$baseUrl$redirectUrl"
  }

  def businessMatchWithJourneyIdUrl(session: Session): String =
    s"$businessMatchResultPathSegment${journeyIdFromSession(session)}"

  def journeyIdFromSession(session: Session): String = session(journeyIdKey).as[String]

  def assertAllValuesPresentInSelector(
    selector: String,
    expectedValues: Set[String]
  ): CheckBuilder.Final[CssCheckType, NodeSelector] = css(selector).findAll
    .transform(_.map(_.trim).toSet)
    .is(expectedValues)

  def extractRelativeUrl(url: String): String = {
    val uri = java.net.URI.create(url)
    val query = Option(uri.getRawQuery).fold("")(queryParam => s"?$queryParam")
    s"${uri.getPath}$query"
  }
}
