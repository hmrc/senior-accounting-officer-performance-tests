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
import uk.gov.hmrc.perftests.requests.AuthorityWizard.{getAuthorityWizardPage, submitNewAuthorityRecord}
import uk.gov.hmrc.perftests.requests.Registration._
import uk.gov.hmrc.perftests.support.GatlingSupport.AugmentJourneyParts

class Simulation extends PerformanceTestRunner {

  setup("create-authority-record", "Submit new authority record")
    .withChainedActions(
      getAuthorityWizardPage,
      submitNewAuthorityRecord
    )

  setup("register-company-for-service", "Registration")
    .withChainedActions(
      getRegistrationPage,

      // GRS
      getGenericRegistrationServiceStubBeforeRedirect,
      getGenericRegistrationServiceStubAfterRedirect,
      sendResponseWithCompanyDetailsBeforeRedirect,
      getInterimRedirectToRegistrationPage,
      getRegistrationPageWithCompleteCompanyDetails,

      // add first contact
      getContactDetailsPage,
      postContactDetailsPage,
      getAddFirstContactNamePage,
      postAddFirstContactNamePage,
      getAddFirstContactEmailPage,
      postAddFirstContactEmailPage,
      getAddAnotherContactPage,

      // add second contact
      postAddAnotherContactPage,
      getAddSecondContactNamePage,
      postAddSecondContactNamePage,
      getAddSecondContactEmailPage,
      postAddSecondContactEmailPage,
      getCheckYourAnswersPageShowingBothContacts,
      postCheckYourAnswersPageShowingBothContacts,

      // Submit sign up
      getRegistrationPageAfterSaving,
      postRegistrationPage,
      getRegistrationCompletePage
    )

//  setup("grs-setup", "GRS Setup")
//    .withChainedActions(
//      getRegistrationPage,
//
//      // GRS
//      getGenericRegistrationServiceStubBeforeRedirect,
//      getGenericRegistrationServiceStubAfterRedirect,
//      sendResponseWithCompanyDetailsBeforeRedirect,
//      getInterimRedirectToRegistrationPage,
//      getRegistrationPageWithCompleteCompanyDetails
//    )

  runSimulation()
}
