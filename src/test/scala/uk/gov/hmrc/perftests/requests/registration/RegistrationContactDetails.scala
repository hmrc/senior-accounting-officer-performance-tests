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

package uk.gov.hmrc.perftests.requests.registration

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.utils.helpers.URLHelpers.*
import uk.gov.hmrc.perftests.requests.utils.mappers.CommonMappers.*
import uk.gov.hmrc.perftests.requests.utils.mappers.RegistrationMappers.*
import uk.gov.hmrc.perftests.requests.utils.requests.Requests.*

trait RegistrationContactDetails {

  self: PerformanceTestRunner =>

  def registrationContactDetails(): Unit =
    setup("registration-contact-details", "Registration: Contact Details") withActions (
      getPage("Contact Details", registrationContactDetailsUrl, saveToken = true),
      postPage("Contact Details", registrationContactDetailsUrl, registrationFirstContactNameUrl),

      getPage("First Contact Name", registrationFirstContactNameUrl),
      postPage(
        "First Contact Name",
        registrationFirstContactNamePostUrl,
        registrationFirstContactEmailUrl,
        inputNameCDFirst
      ),

      getPage("First Contact Email", registrationFirstContactEmailUrl),
      postPage(
        "First Contact Email",
        registrationFirstContactEmailPostUrl,
        registrationAnotherContactPageUrl,
        inputEmailCDFirst
      ),

      getPage("Add Another Contact", registrationAnotherContactPageUrl),
      postPage(
        "Add Another Contact",
        registrationAnotherContactPageUrl,
        registrationSecondContactNameUrl,
        radioButtonYes
      ),

      getPage("Second Contact Name", registrationSecondContactNameUrl),
      postPage(
        "Second Contact Name",
        registrationSecondContactNamePostUrl,
        registrationSecondContactEmailUrl,
        inputNameCDSecond
      ),

      getPage("Second Contact Email", registrationSecondContactEmailUrl),
      postPage(
        "Second Contact Email",
        registrationSecondContactEmailPostUrl,
        registrationCheckYourAnswersUrl,
        inputEmailCDSecond
      ),

      getPage("Check Your Answers", registrationCheckYourAnswersUrl),
      postPage("Check Your Answers", registrationCheckYourAnswersUrl, registrationUrl, contactDetails)
    )
}
