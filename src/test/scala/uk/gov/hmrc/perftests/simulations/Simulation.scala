/*
 * Copyright 2023 HM Revenue & Customs
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
import uk.gov.hmrc.perftests.requests.AuthorityRecord.{getAuthorityWizardPage, submitNewAuthorityRecord}
import uk.gov.hmrc.perftests.requests.Registration.{continueToAddFirstContactEmail, continueToAddMoreContactsQuestionPage, continueToAddSecondContact, continueToAddSecondContactEmail, continueToCheckYourAnswersForBothContacts, continueToCheckYourAnswersForContact, continueToMissingFirstContactEmailErrorPage, continueToMissingFirstContactNameErrorPage, continueToMissingSecondContactEmailErrorPage, continueToMissingSecondContactNameErrorPage, continueToProvideFirstContactDetails, getAddFirstContactEmailPage, getAddFirstContactNamePage, getAddMoreContactsQuestionPage, getAddSecondContactEmailPage, getAddSecondContactNamePage, getCheckYourAnswersPage, getCheckYourAnswersPageForBothContacts, getContactDetailsPage, getGenericRegistrationServiceStubAfterRedirect, getGenericRegistrationServiceStubBeforeRedirect, getInterimRedirectToRegistrationPage, getRegistrationPage, getRegistrationPageWithCompleteCompanyDetails, sendResponseWithCompanyDetailsBeforeRedirect}
import uk.gov.hmrc.perftests.support.GatlingSupport.AugmentJourneyParts
import uk.gov.hmrc.perftests.support.RequestSupport.{logSessionInfo, saveRedirect}

class Simulation extends PerformanceTestRunner {

  setup("create-authority-record", "Submit new authority record")
    .withChainedActions(
      getAuthorityWizardPage,
      submitNewAuthorityRecord,
      saveRedirect
    )

  setup("register-company-for-service", "Registration")
    .withChainedActions(
      getRegistrationPage,
      getGenericRegistrationServiceStubBeforeRedirect,
      saveRedirect,
      getGenericRegistrationServiceStubAfterRedirect,
      sendResponseWithCompanyDetailsBeforeRedirect,
      getInterimRedirectToRegistrationPage,
      getRegistrationPageWithCompleteCompanyDetails,
      getContactDetailsPage,
      continueToProvideFirstContactDetails,
      getAddFirstContactNamePage,
      continueToMissingFirstContactNameErrorPage,
      continueToAddFirstContactEmail,
      getAddFirstContactEmailPage,
      continueToMissingFirstContactEmailErrorPage,
      continueToAddMoreContactsQuestionPage,
      getAddMoreContactsQuestionPage,
      continueToCheckYourAnswersForContact,
      getCheckYourAnswersPage,
      continueToAddSecondContact,
      getAddSecondContactNamePage,
      continueToMissingSecondContactNameErrorPage,
      continueToAddSecondContactEmail,
      getAddSecondContactEmailPage,
      continueToMissingSecondContactEmailErrorPage,
      continueToCheckYourAnswersForBothContacts,
      logSessionInfo,
      getCheckYourAnswersPageForBothContacts
    )

  runSimulation()
}
