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

package uk.gov.hmrc.perftests.simulations

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.AuthorityWizard.{getAuthorityWizardPage, submitNewAuthorityRecordForNotification, submitNewAuthorityRecordForRegistration}
import uk.gov.hmrc.perftests.requests.Registration._
import uk.gov.hmrc.perftests.requests.UploadSubmissionTemplate.{getHubPageFromRedirect, getNotificationStartPage, getNotificationUploadPage, postNotificationUpload}
import uk.gov.hmrc.perftests.support.GatlingSupport.AugmentJourneyParts

class Simulation extends PerformanceTestRunner {

  setup("create-authority-record", "Submit new authority record")
    .withChainedActions(
      getAuthorityWizardPage,
      submitNewAuthorityRecordForRegistration
    )

  setup("grs-setup", "GRS Setup")
    .withChainedActions(
      getRegistrationPage,
      getGenericRegistrationServiceStubBeforeRedirect,
      getGenericRegistrationServiceStubAfterRedirect,
      sendResponseWithCompanyDetailsBeforeRedirect,
      getInterimRedirectToRegistrationPage,
      getRegistrationPageWithCompleteCompanyDetails
    )

  setup("registration-first-contact", "Registration First Contact")
    .withChainedActions(
      getContactDetailsPage,
      postContactDetailsPage,
      getAddFirstContactNamePage,
      postAddFirstContactNamePage,
      getAddFirstContactEmailPage,
      postAddFirstContactEmailPage
    )

  setup("registration-second-contact", "Registration Second Contact")
    .withChainedActions(
      getAddAnotherContactPage,
      postAddAnotherContactPage,
      getAddSecondContactNamePage,
      postAddSecondContactNamePage,
      getAddSecondContactEmailPage,
      postAddSecondContactEmailPage,
      getCheckYourAnswersPage,
      postCheckYourAnswersPage
    )

  setup("submit-registration", "Registration Submit")
    .withChainedActions(
      getRegistrationPage,
      postRegistrationPage,
      getRegistrationCompletePage
    )


  setup("create-authority-record-for-notification", "Submit new authority record for notification")
    .withChainedActions(
      getAuthorityWizardPage,
      submitNewAuthorityRecordForNotification
    )

  setup("upload-template", "Upload a new submission template in notification journey")
    .withChainedActions(
      getNotificationStartPage,
      getNotificationUploadPage,
      postNotificationUpload,
      getHubPageFromRedirect
    )



  runSimulation()
}
