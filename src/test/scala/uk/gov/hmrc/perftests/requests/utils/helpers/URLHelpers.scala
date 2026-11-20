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

package uk.gov.hmrc.perftests.requests.utils.helpers

import uk.gov.hmrc.performance.conf.ServicesConfiguration

object URLHelpers extends ServicesConfiguration {
  val baseAuthUrl: String         = baseUrlFor("auth-login-stub")
  val baseSaoHubUrl: String       = baseUrlFor("senior-accounting-officer-hub-frontend")
  val baseRegistrationUrl: String = baseUrlFor("senior-accounting-officer-registration-frontend")
  val baseSubmissionUrl: String   = baseUrlFor("senior-accounting-officer-submission-frontend")

  val authorityWizardPageUrl: String = s"$baseAuthUrl/auth-login-stub/gg-sign-in"
  val saoLandingPage: String         = s"$baseSaoHubUrl/senior-accounting-officer"

  val submission: String     = s"$baseSubmissionUrl/senior-accounting-officer/submission"
  val submissionType: String = s"$submission/submission"

  val notification: String                         = s"$submission/notification"
  val notificationHome: String                     = s"$notification/task"
  val notificationMoreThanOne: String              = s"$notification/confirm-multiple-officers?mode=NormalMode"
  val notificationOneSao: String                   = s"$notification/one-sao?mode=NormalMode"
  val notificationOneFullName: String              = s"$notificationOneSao/submit-notification-full-name"
  val notificationSaoLast: String                  = s"$notification/last"
  val notificationMoreSao: String                  = s"$notification/more-sao"
  val notificationMoreCurrentSaoName: String       = s"$notificationSaoLast/officer-name?mode=NormalMode"
  val notificationMoreCurrentSaoStartDate: String  = s"$notificationSaoLast/officer-start-date?mode=NormalMode"
  val notificationMoreSaoPrevious: String          = s"$notification/previous"
  val notificationMorePreviousSaoName: String      = s"$notificationMoreSaoPrevious/officer-name?mode=NormalMode"
  val notificationMorePreviousSaoStartDate: String = s"$notificationMoreSaoPrevious/officer-start-date?mode=NormalMode"
  val notificationMorePreviousSaoEndDate: String   = s"$notificationMoreSaoPrevious/officer-end-date?mode=NormalMode"
  val notificationMoreAddOfficer: String           = s"$notification/add-officer?mode=NormalMode"
  val notificationUploadFile: String               = s"$notification/submission-template"
  val notificationUploadTable: String              = s"$notification/review-companies"
  val notificationAdditionalInfo: String           = s"$notification/additional-information"
  val notificationConfirm: String                  = s"$notification/confirm-notification"
  val notificationCheckYourAnswers: String         = s"$notification/check-answers"
  val notificationComplete: String                 = s"$notification/complete"
  val notificationConfirmation: String             = s"$notification/confirmation"

  val certificate: String                  = s"$submission/certificate"
  val certificateHome: String              = s"$certificate/task"
  val certificateSaoFullName: String       = s"$certificate/officer-name?mode=NormalMode"
  val certificateSaoEmail: String          = s"$certificate/email-address?mode=NormalMode"
  val certificateUploadFile: String        = s"$certificate/upload-template"
  val certificateReviewQualified: String   = s"$certificate/review-qualified-certificate"
  val certificateReviewUnqualified: String = s"$certificate/review-unqualified-certificate"
  val certificateAdditionalInfo: String    = s"$certificate/additional-information?mode=NormalMode"
  val certificateWhoIsSubmitting: String   = s"$certificate/officer-submit-certificate"
  val certificateConfirmSao: String        = s"$certificate/declaration/confirm-certificate"
  val certificateCheckYourAnswers: String  = s"$certificate/check-answers"
  val certificateSubmitting: String        = s"$certificate/submitting"
  val certificateTaskListComplete: String  = s"$certificate/task-list/complete"
  val certificateTaskList: String          = s"$certificate/task-list"
  val certificateConfirmation: String      = s"$certificate/confirmation"

  val registrationUrl: String                       = s"$baseRegistrationUrl/senior-accounting-officer/registration"
  val registrationFeatureToggleUrl: String          = s"$registrationUrl/test-only/feature-toggle"
  val registrationStubEnrolmentUrl: String          = s"$registrationUrl/test-only/stub-enrolment"
  val registrationBusinessMatchUrl: String          = s"$registrationUrl/business-match"
  val registrationBusinessMatchResultUrl: String    = s"$registrationUrl/business-match/result?journeyId="
  val registrationContactDetailsUrl: String         = s"$registrationUrl/contact-details"
  val registrationFirstContactNameUrl: String       = s"$registrationContactDetailsUrl/first/full-name"
  val registrationFirstContactNamePostUrl: String   = s"$registrationFirstContactNameUrl?mode=NormalMode"
  val registrationFirstContactEmailUrl: String      = s"$registrationContactDetailsUrl/first/email-address"
  val registrationFirstContactEmailPostUrl: String  = s"$registrationFirstContactEmailUrl?mode=NormalMode"
  val registrationAnotherContactPageUrl: String     = s"$registrationContactDetailsUrl/first/add-second"
  val registrationSecondContactNameUrl: String      = s"$registrationContactDetailsUrl/second/full-name"
  val registrationSecondContactNamePostUrl: String  = s"$registrationSecondContactNameUrl?mode=NormalMode"
  val registrationSecondContactEmailUrl: String     = s"$registrationContactDetailsUrl/second/email-address"
  val registrationSecondContactEmailPostUrl: String = s"$registrationSecondContactEmailUrl?mode=NormalMode"
  val registrationCheckYourAnswersUrl: String       = s"$registrationContactDetailsUrl/check-answers"
  val registrationCompletePageUrl: String           = s"$registrationUrl/confirmation"

  val grsStubPathSegment: String = "/test-only/grs-stub"

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
}
