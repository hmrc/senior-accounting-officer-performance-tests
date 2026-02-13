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
  val baseRegistrationUrl: String            = baseUrlFor("senior-accounting-officer-registration-frontend")
  val authorityWizardPageUrl: String         = s"$authBaseUrl/auth-login-stub/gg-sign-in"
  val registrationPageUrl: String            = s"$baseRegistrationUrl/senior-accounting-officer/registration"
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

  val baseSubmissionUrl: String = baseUrlFor("senior-accounting-officer-submission-frontend")
  val baseUpScanUrl: String     = baseUrlFor("upscan-proxy")

  val notificationStartPageUrl: String  = s"$baseSubmissionUrl/senior-accounting-officer/submission/notification/start"
  val notificationUploadPageUrl: String = s"$baseSubmissionUrl/senior-accounting-officer/submission/notification/upload"

  val upscanProxyUrl: String = s"$baseUpScanUrl/upscan/upload-proxy"

  val redirectUrlKey: String              = "redirectUrl"
  val csrfTokenKey: String                = "csrfToken"
  val successActionRedirectUrlKey: String = "successActionRedirectUrl"
  val mdtpCookieKey: String               = "mdtpCookie"
  val mdtpdiCookieKey: String             = "mdtpdiCookie"
  val mdtpCookieValue: String             = "mdtp=${mdtpCookie}"
  val mdtpdiCookieValue: String           = "mdtpdi=${mdtpdiCookie}"
  val journeyIdKey: String                = "journeyId"
  val journeyIdRegexPattern: String       = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

  val successActionRedirect: String          = "success_action_redirect"
  val xAmzCredential: String                 = "x-amz-credential"
  val xAmzMetaUpscanInitiateResponse: String = "x-amz-meta-upscan-initiate-response"
  val xAmzMetaOriginalFilename: String       = "x-amz-meta-original-filename"
  val xAmzAlgorithm: String                  = "x-amz-algorithm"
  val xAmzSignature: String                  = "x-amz-signature"
  val errorActionRedirect: String            = "error_action_redirect"
  val xAmzMetaSessionId: String              = "x-amz-meta-session-id"
  val xAmzMetaCallbackUrl: String            = "x-amz-meta-callback-url"
  val xAmzDate: String                       = "x-amz-date"
  val xAmzMetaUpscanInitiateReceived: String = "x-amz-meta-upscan-initiate-received"
  val xAmzMetaRequestId: String              = "x-amz-meta-request-id"
  val key: String                            = "key"
  val acl: String                            = "acl"
  val xAmzMetaConsumingService: String       = "x-amz-meta-consuming-service"
  val policy: String                         = "policy"

  val upscanParameters: List[String] = List(
    successActionRedirect,
    xAmzCredential,
    xAmzMetaUpscanInitiateResponse,
    xAmzMetaOriginalFilename,
    xAmzAlgorithm,
    xAmzSignature,
    errorActionRedirect,
    xAmzMetaSessionId,
    xAmzMetaCallbackUrl,
    xAmzDate,
    xAmzMetaUpscanInitiateReceived,
    xAmzMetaRequestId,
    key,
    acl,
    xAmzMetaConsumingService,
    policy
  )

  def saveUpscanParams(): Seq[CheckBuilder.Final[CssCheckType, NodeSelector]] =
    upscanParameters.map(param => css(s"input[name=$param]", "value").exists.saveAs(param))

  def saveCsrfToken(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("input[name=csrfToken]", "value").exists.saveAs(csrfTokenKey)

  def csrfTokenFromSession(session: Session): String = session(csrfTokenKey).as[String]

  def redirectUrlFromSession(session: Session): String = {
    val redirectUrl = session(redirectUrlKey).as[String]
    if (redirectUrl.startsWith("http")) {
      redirectUrl
    } else s"$baseRegistrationUrl$redirectUrl"
  }

  def businessMatchWithJourneyIdUrl(session: Session): String =
    s"$businessMatchResultPathSegment${journeyIdFromSession(session)}"

  def journeyIdFromSession(session: Session): String = session(journeyIdKey).as[String]

  def extractRelativeUrl(url: String): String = {
    val uri   = java.net.URI.create(url)
    val query = Option(uri.getRawQuery).fold("")(queryParam => s"?$queryParam")
    s"${uri.getPath}$query"
  }

  def saveSuccessActionRedirectUrl(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("input[name=success_action_redirect]", "value").exists.saveAs(successActionRedirectUrlKey)

  def successActionRedirectUrlFromSession(session: Session): String = session(successActionRedirectUrlKey).as[String]
}
