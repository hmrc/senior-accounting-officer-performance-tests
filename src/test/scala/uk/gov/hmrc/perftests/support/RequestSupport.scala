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
  val baseRegistrationUrl: String                        = baseUrlFor("senior-accounting-officer-registration-frontend")
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

  val baseSubmissionUrl: String     = baseUrlFor("senior-accounting-officer-submission-frontend")
  val notificationStartPageUrl: String = s"$baseSubmissionUrl/senior-accounting-officer/submission/notification/start"
  val notificationUploadPageUrl: String = s"$baseSubmissionUrl/senior-accounting-officer/submission/notification/upload"

  val redirectUrlKey: String                 = "redirectUrl"
  val csrfTokenKey: String                   = "csrfToken"
  val mdtpCookieKey: String                  = "mdtpCookie"
  val mdtpdiCookieKey: String                = "mdtpdiCookie"
  val mdtpCookieValue: String                = "mdtp=${mdtpCookie}"
  val mdtpdiCookieValue: String              = "mdtpdi=${mdtpdiCookie}"
  val journeyIdKey: String                   = "journeyId"
  val journeyIdRegexPattern: String          = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

  val successActionRedirect: String = "success_action_redirect"
  val xAmzCredential: String = "x-amz-credential"
  val xAmzMetaUpscanInitiateResponse: String = "x-amz-meta-upscan-initiate-response"
  val xAmzMetaOriginalFilename: String = "x-amz-meta-original-filename"
  val xAmzAlgorithm: String = "x-amz-algorithm"
  val xAmzSignature: String = "x-amz-signature"
  val errorActionRedirect: String = "error_action_redirect"
  val xAmzMetaSessionId: String = "x-amz-meta-session-id"
  val xAmzMetaCallbackUrl: String = "x-amz-meta-callback-url"
  val xAmzDate: String = "x-amz-date"
  val xAmzMetaUpscanInitiateReceived: String = "x-amz-meta-upscan-initiate-received"
  val xAmzMetaRequestId: String = "x-amz-meta-request-id"
  val key: String = "key"
  val acl: String = "acl"
  val xAmzMetaConsumingService: String = "x-amz-meta-consuming-service"
  val policy: String = "policy"

  val upscanParameters : List[String] = List(successActionRedirect, xAmzCredential, xAmzMetaUpscanInitiateResponse, xAmzMetaOriginalFilename, xAmzAlgorithm, xAmzSignature,
    errorActionRedirect, xAmzMetaSessionId, xAmzMetaCallbackUrl, xAmzDate, xAmzMetaUpscanInitiateReceived, xAmzMetaRequestId, key, acl, xAmzMetaConsumingService,
    policy)


//    <input type="hidden" name="success_action_redirect" value="http://localhost:10056/senior-accounting-officer?uploadId=2f9dbcb3-c792-4169-83aa-191fe54adde0&amp;key=44de48ff-4f9c-4f19-8667-bfb4ce484a4b"/>
  //            <input type="hidden" name="x-amz-credential" value="ASIAxxxxxxxxx/20180202/eu-west-2/s3/aws4_request"/>
  //            <input type="hidden" name="x-amz-meta-upscan-initiate-response" value="2026-02-12T13:48:15.023433Z"/>
  //            <input type="hidden" name="x-amz-meta-original-filename" value="${filename}"/>
  //            <input type="hidden" name="x-amz-algorithm" value="AWS4-HMAC-SHA256"/>
  //            <input type="hidden" name="x-amz-signature" value="xxxx"/>
  //            <input type="hidden" name="error_action_redirect" value="http://localhost:10056/senior-accounting-officer"/>
  //            <input type="hidden" name="x-amz-meta-session-id" value="723e6d8c-a351-4c99-b07d-b00258653d2f"/>
  //            <input type="hidden" name="x-amz-meta-callback-url" value="http://localhost:10058/internal/upscan-callback"/>
  //            <input type="hidden" name="x-amz-date" value="20260212T134815Z"/>
  //            <input type="hidden" name="x-amz-meta-upscan-initiate-received" value="2026-02-12T13:48:15.023433Z"/>
  //            <input type="hidden" name="x-amz-meta-request-id" value="e9d5d22a-bda6-4588-8e90-8538911e6b29"/>
  //            <input type="hidden" name="key" value="44de48ff-4f9c-4f19-8667-bfb4ce484a4b"/>
  //            <input type="hidden" name="acl" value="private"/>
  //            <input type="hidden" name="x-amz-meta-consuming-service" value="senior-accounting-officer-submission-frontend"/>
  //            <input type="hidden" name="policy" value="eyJjb25kaXRpb25zIjpbWyJjb250ZW50LWxlbmd0aC1yYW5nZSIsMCw0MDk2XV19"/>


  def saveUpscanParams(): Seq[CheckBuilder.Final[CssCheckType, NodeSelector]] = {
    upscanParameters.map(param =>
      css(s"input[name=$param]", "value").exists.saveAs(param))
  }

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
}
