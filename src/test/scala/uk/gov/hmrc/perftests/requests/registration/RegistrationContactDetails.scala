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
import uk.gov.hmrc.perftests.requests.utils.RequestSupport._
import uk.gov.hmrc.perftests.requests.utils.Requests._
import uk.gov.hmrc.perftests.requests.utils.mappers.GeneralMappers._
import uk.gov.hmrc.perftests.requests.utils.mappers.RegistrationMappers._

trait RegistrationContactDetails {

  self: PerformanceTestRunner =>

  def registrationContactDetails(): Unit =
    setup("registration-contact-details", "Registration Contact Details") withActions (
      getPage("Contact Details", contactDetailsPageUrl, saveToken = true),
      postPage("Contact Details", contactDetailsPageUrl, addFirstContactNameUrl),

      getPage("First Contact Name", addFirstContactNameUrl),
      postPage("First Contact Name", addFirstContactNameUrl, addFirstContactEmailUrl, textInput),

      getPage("First Contact Email", addFirstContactEmailUrl),
      postPage("First Contact Email", addFirstContactEmailUrl, addAnotherContactPageUrl, emailAddress),

      getPage("Add Another Contact", addAnotherContactPageUrl),
      postPage("Add Another Contact", addAnotherContactPageUrl, addSecondContactNameUrl, radioButtonYes),

      getPage("Second Contact Name", addSecondContactNameUrl),
      postPage("Second Contact Name", addSecondContactNameUrl, addSecondContactEmailUrl, textInput),

      getPage("Second Contact Email", addSecondContactEmailUrl),
      postPage("Second Contact Email", addSecondContactEmailUrl, checkYourAnswersUrl, emailAddress),

      getPage("Check Your Answers", checkYourAnswersUrl),
      postPage("Check Your Answers", checkYourAnswersUrl, registrationPageUrl, contactDetails)
    )
}
