/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.requests.submission.notification

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.utils.RequestSupport.*
import uk.gov.hmrc.perftests.requests.utils.Requests.*
import uk.gov.hmrc.perftests.requests.utils.UpscanRequests.*
import uk.gov.hmrc.perftests.requests.utils.mappers.CommonMappers.*

trait NotificationDetails {
  self: PerformanceTestRunner =>

  def notificationSaoDetails(): Unit =
    setup("notification-sao-details", "Notification - Sao Details") withActions (
      getPage("Submission Home", saoLandingPage),

      getPage("Submission Type", submissionType, saveToken = true),
      postPage("Submission Type", submissionType, notificationHome, radioButtonNotification),

      getPage("Notification: Home", notificationHome),

      getPage("Notification: More Than One", notificationMoreThanOne),
      postPage("Notification: More Than One", notificationMoreThanOne, notificationMoreCurrentSaoName, radioButtonTrue),

      getPage("Notification: Current SAO Name", notificationMoreCurrentSaoName),
      postPage(
        "Notification: Current SAO Name",
        notificationMoreCurrentSaoName,
        notificationMoreCurrentSaoStartDate,
        textInput
      ),

      getPage("Notification: Current SAO Start Date", notificationMoreCurrentSaoStartDate),
      postPage(
        "Notification: Current SAO Start Date",
        notificationMoreCurrentSaoStartDate,
        notificationMorePreviousSaoName,
        currentSaoStartDate
      ),

      getPage("Notification: Previous SAO Name", notificationMorePreviousSaoName),
      postPage(
        "Notification: Previous SAO Name",
        notificationMorePreviousSaoName,
        notificationMorePreviousSaoStartDate,
        textInput
      ),

      getPage("Notification: Previous SAO Start Date", notificationMorePreviousSaoStartDate),
      postPage(
        "Notification: Previous SAO Start Date",
        notificationMorePreviousSaoStartDate,
        notificationMorePreviousSaoEndDate,
        previousSaoStartDate
      ),

      getPage("Notification: Previous SAO End Date", notificationMorePreviousSaoEndDate),
      postPage(
        "Notification: Previous SAO End Date",
        notificationMorePreviousSaoEndDate,
        notificationMoreAllAdded,
        previousSaoEndDate
      ),

      getPage("Notification: All SAOs Added", notificationMoreAllAdded),
      postPage("Notification: All SAOs Added", notificationMoreAllAdded, notificationHome, radioButtonTrue),

      getPage("Notification: Home", notificationHome),

      getUploadPage,
      uploadFile,
      getUploadSuccessPage,
      pause(1),
      followUploadSuccessRedirect,

      getPage("Notification: Upload Table", notificationUploadTable),
      postPage("Notification: Upload Table", notificationUploadTable, notificationHome),

      getPage("Notification: Home", notificationHome),

      getPage("Notification: Additional Information", notificationAdditionalInfo),
      postPage("Notification: Additional Information", notificationAdditionalInfo, notificationConfirm, textInput),

      getPage("Notification: Confirm", notificationConfirm),

      getPage("Notification: Check Your Answers", notificationCheckYourAnswers),
      postPageWithQueryRedirect(
        "Notification: Check Your Answers",
        notificationCheckYourAnswers,
        notificationConfirmation
      ),

      getPageFromRedirect("Notification: Confirmation", baseSubmissionUrl)
    )
}
