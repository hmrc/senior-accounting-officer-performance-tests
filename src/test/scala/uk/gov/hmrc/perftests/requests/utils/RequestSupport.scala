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

package uk.gov.hmrc.perftests.requests.utils

import io.gatling.core.Predef.*
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.css.CssCheckType
import jodd.lagarto.dom.NodeSelector
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object RequestSupport extends ServicesConfiguration {
  val baseAuthUrl: String         = baseUrlFor("auth-login-stub")
  val baseSaoHubUrl: String       = baseUrlFor("senior-accounting-officer-hub-frontend")
  val baseRegistrationUrl: String = baseUrlFor("senior-accounting-officer-registration-frontend")
  val baseSubmissionUrl: String   = baseUrlFor("senior-accounting-officer-submission-frontend")

  val authorityWizardPageUrl: String = s"$baseAuthUrl/auth-login-stub/gg-sign-in"
  val saoLandingPage: String         = s"$baseSaoHubUrl/senior-accounting-officer"

  val submission: String     = s"$baseSubmissionUrl/senior-accounting-officer/submission"
  val submissionType: String = s"$submission/submission-type"

  val notification: String                         = s"$submission/notification"
  val notificationHome: String                     = s"$notification/start"
  val notificationMoreThanOne: String              = s"$notification/more-than-one-sao"
  val notificationOneSao: String                   = s"$notification/one-sao"
  val notificationOneFullName: String              = s"$notificationOneSao/submit-notification-full-name"
  val notificationMoreSao: String                  = s"$notification/more-sao"
  val notificationMoreCurrentSaoName: String       = s"$notificationMoreSao/submit-notification-full-name"
  val notificationMoreCurrentSaoStartDate: String  = s"$notificationMoreSao/submit-notification-first-start-date"
  val notificationMorePreviousSaoName: String      = s"$notification/who-was-the-sao-before"
  val notificationMorePreviousSaoStartDate: String = s"$notification/multi-sao-second-start-date"
  val notificationMorePreviousSaoEndDate: String   = s"$submission/notificationMoreSaoSecondEndDate"
  val notificationMoreAllAdded: String             = s"$notificationMoreSao/are-all-added"
  val notificationUploadFile: String               = s"$notification/upload"
  val notificationUploadTable: String              = s"$notificationUploadFile/table"
  val notificationAdditionalInfo: String           = s"$notification/additional-information"
  val notificationConfirm: String                  = s"$notification/confirm-your-notification"
  val notificationCheckYourAnswers: String         = s"$notification/check-your-answers"
  val notificationConfirmation: String             = s"$notification/confirmation"

  val certificate: String                  = s"$submission/certificate"
  val certificateOnly: String              = s"$submission/certificate-only"
  val certificateHome1: String             = s"$certificate/task-list/1"
  val certificateHome2: String             = s"$certificate/task-list/2"
  val certificateHome3: String             = s"$certificate/task-list/3"
  val certificateSaoFullName: String       = s"$certificate/submit-certificate-sao-full-name"
  val certificateSaoEmail: String          = s"$certificateOnly/sao-email"
  val certificateUploadFile: String        = s"$certificate/upload"
  val certificateReviewQualified: String   = s"$submission/certificateReviewQualified"
  val certificateReviewUnqualified: String = s"$submission/certificateReviewUnqualified"
  val certificateAdditionalInfo: String    = s"$submission/certificateAdditionalInformation"
  val certificateWhoIsSubmitting: String   = s"$submission/certificate-who-is-submitting"
  val certificateConfirmSao: String        = s"$certificate/submit-certificate-confirm-sao"
  val certificateCheckYourAnswers: String  = s"$submission/certificateCheckYourAnswers"
  val certificateTaskListComplete: String  = s"$certificate/task-list/complete"
  val certificateTaskList: String          = s"$certificate/task-list"
  val certificateConfirmation: String      = s"$submission/certificate-confirmation"

  val registrationPageUrl: String            = s"$baseRegistrationUrl/senior-accounting-officer/registration"
  val stubEnrolmentPageUrl: String           = s"$registrationPageUrl/test-only/stub-enrolment"
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

  val notificationStartPageUrl: String  = s"$baseSubmissionUrl/senior-accounting-officer/submission/notification/start"
  val notificationUploadPageUrl: String = s"$baseSubmissionUrl/senior-accounting-officer/submission/notification/upload"

  val redirectUrlKey: String        = "redirectUrl"
  val csrfTokenKey: String          = "csrfToken"
  val mdtpCookieKey: String         = "mdtpCookie"
  val mdtpdiCookieKey: String       = "mdtpdiCookie"
  val mdtpCookieValue: String       = "mdtp=${mdtpCookie}"
  val mdtpdiCookieValue: String     = "mdtpdi=${mdtpdiCookie}"
  val journeyIdKey: String          = "journeyId"
  val journeyIdRegexPattern: String = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

  val formAction: String = ""

  def saveCsrfToken(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("input[name=csrfToken]", "value").exists.saveAs(csrfTokenKey)

  def saveFormAction(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("form", "action").exists.saveAs(formAction)

  def csrfTokenFromSession(session: Session): String = session(csrfTokenKey).as[String]

  def formActionFromSession(session: Session): String = session(formAction).as[String]

  def redirectUrlFromSession(
      session: Session,
      baseUrl: String = baseRegistrationUrl
  ): String = {
    val redirectUrl = session(redirectUrlKey).as[String]

    if (redirectUrl.startsWith("http")) {
      redirectUrl
    } else {
      s"$baseUrl$redirectUrl"
    }
  }

  def businessMatchWithJourneyIdUrl(session: Session): String =
    s"$businessMatchResultPathSegment${journeyIdFromSession(session)}"

  def journeyIdFromSession(session: Session): String = session(journeyIdKey).as[String]

  def extractRelativeUrl(url: String): String = {
    val uri   = java.net.URI.create(url)
    val query = Option(uri.getRawQuery).fold("")(queryParam => s"?$queryParam")
    s"${uri.getPath}$query"
  }

  val authorityRedirectUrl: String = if (runLocal) stubEnrolmentPageUrl else registrationPageUrl

  val requiresStubEnrolment: Boolean = runLocal

  def removeQueryParametersFromUrl(url: String): String =
    url.split("\\?")(0)
}
