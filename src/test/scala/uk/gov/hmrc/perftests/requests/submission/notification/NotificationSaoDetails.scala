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
import uk.gov.hmrc.perftests.requests.utils.mappers.CommonMappers.*

trait NotificationSaoDetails {
  self: PerformanceTestRunner =>

  def notificationSaoDetails(): Unit =
    setup("notification-sao-details", "Notification: Sao Details") withActions (
      getPage("Current SAO Name", notificationMoreCurrentSaoName),
      postPage("Current SAO Name", notificationMoreCurrentSaoName, notificationMoreCurrentSaoStartDate, textInput),

      getPage("Current SAO Start Date", notificationMoreCurrentSaoStartDate),
      postPage(
        "Current SAO Start Date",
        notificationMoreCurrentSaoStartDate,
        notificationMorePreviousSaoName,
        currentSaoStartDate
      ),

      getPage("Notification: Previous SAO Name", notificationMorePreviousSaoName),
      postPage("Previous SAO Name", notificationMorePreviousSaoName, notificationMorePreviousSaoStartDate, textInput),

      getPage("Previous SAO Start Date", notificationMorePreviousSaoStartDate),
      postPage(
        "Previous SAO Start Date",
        notificationMorePreviousSaoStartDate,
        notificationMorePreviousSaoEndDate,
        previousSaoStartDate
      ),

      getPage("Previous SAO End Date", notificationMorePreviousSaoEndDate),
      postPage(
        "Previous SAO End Date",
        notificationMorePreviousSaoEndDate,
        notificationMoreAllAdded,
        previousSaoEndDate
      ),

      getPage("All SAOs Added", notificationMoreAllAdded),
      postPage("All SAOs Added", notificationMoreAllAdded, notificationHome, radioButtonTrue)
    )
}
