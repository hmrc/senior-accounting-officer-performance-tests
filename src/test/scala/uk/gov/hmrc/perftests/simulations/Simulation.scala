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

      // example POC
      // Contact Details pages  journey
      // get Contact Details page
      // submit Contact Details page
      // get First Contact Name
      // submit First contact Name
      // get First Email
      // submit First Email

      getContactDetailsPage,
      continueToProvideFirstContactDetails,
      getAddFirstContactNamePage,
      continueToAddFirstContactEmail,
      getAddFirstContactEmailPage,
      continueToAddMoreContactsQuestionPage,
      getAddMoreContactsQuestionPage,

      // continueToCheckYourAnswersForContact,
      // getChangeFirstContactNamePage,
      // continueToCheckYourAnswersForFirstContactNameChange,
      // getChangeFirstContactEmailPage,
      // continueToCheckYourAnswersForFirstContactEmailChange,
      // getCheckYourAnswersPage,  // ?

      continueToAddSecondContact,
      getAddSecondContactNamePage,
      continueToAddSecondContactEmail,
      getAddSecondContactEmailPage,
      // TODO - Post second contact email
      // getChangeSecondContactNamePage,
      // continueToCheckYourAnswersForSecondContactNameChange,
      // getChangeSecondContactEmailPage,
      continueToCheckYourAnswersForSecondContactEmailChange,
      getCheckYourAnswersPageShowingBothContacts,
      continueToSaveAndSubmitRegistration,

      // Submit sign up
      getRegistrationPageAfterSaving,
      continueToSubmitRegistration,
      getRegistrationCompletePage
    )

  runSimulation()
}
