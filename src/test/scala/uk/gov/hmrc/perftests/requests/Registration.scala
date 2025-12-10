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

package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef._
import uk.gov.hmrc.perftests.support.GatlingSupport.convertHttpActionToSeq
import uk.gov.hmrc.perftests.support.RequestSupport._

object Registration {
  private val valueKey: String                  = "value"
  private val amendedFirstContactName: String   = "amendedFirstContactName"
  private val amendedFirstContactEmail: String  = "amendedEmail@1st-Contact.com"
  private val amendedSecondContactName: String  = "amendedSecondContactName"
  private val amendedSecondContactEmail: String = "amendedEmail@2nd-Contact.com"

  def getRegistrationPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'registration' page")
      .get(registrationPageUrl)
      .check(status.is(200))
  )

  def getGenericRegistrationServiceStubBeforeRedirect: Seq[ActionBuilder] =
    convertHttpActionToSeq(
      http("Initial request (before redirect) to access 'enter company details' page (stub)")
        .get(businessMatchUrl)
        .disableFollowRedirect
        .check(status.is(303))
        .check(
          header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
          header(HttpHeaderNames.Location).transform(_.contains(grsStubPathSegment)).is(true),
          headerRegex(HttpHeaderNames.Location, journeyIdRegexPattern).saveAs(journeyIdKey)
        )
    )

  def getGenericRegistrationServiceStubAfterRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Request to access 'enter company details' page (stub)")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def sendResponseWithCompanyDetailsBeforeRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Send stub response with company details (initial call before redirect)")
      .post(session => redirectUrlFromSession(session))
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location)
          .transform(extractRelativeUrl)
          .is(session => businessMatchWithJourneyIdUrl(session))
      )
  )

  def getInterimRedirectToRegistrationPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Interim call to 'registration' page with completed company details")
      .get(session => redirectUrlFromSession(session))
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(registrationPageUrl))
      )
  )

  def getRegistrationPageWithCompleteCompanyDetails: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'registration' page with completed company details")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
  )

  def getContactDetailsPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'provide contact details' page")
      .get(contactDetailsPageUrl)
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToProvideFirstContactDetails: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to add 1st contact details (call before redirect)")
      .post(contactDetailsPageUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(addFirstContactNameUrl))
      )
  )

  def getAddFirstContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 1st contact name' page")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToAddFirstContactEmail: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'add 1st contact email' page (call before redirect)")
      .post(addFirstContactNameUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, "ATestNameForFirstContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(addFirstContactEmailUrl))
      )
  )

  def getAddFirstContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 1st contact email' page")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToAddMoreContactsQuestionPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'add more contacts' question page (call before redirect)")
      .post(addFirstContactEmailUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, "aTestEmailForFirstContact@tester.co.uk")
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(addAnotherContactPageUrl))
      )
  )

  def getAddMoreContactsQuestionPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add more contacts' question page")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToCheckYourAnswersForContact: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page (call before redirect)")
      .post(addAnotherContactPageUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, "yes")
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(checkYourAnswersUrl))
      )
  )

  def getChangeFirstContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change first contact name' page")
      .get(changeFirstContactNameUrl)
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToCheckYourAnswersForFirstContactNameChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 1st contact name change (call before redirect)")
      .post(changeFirstContactNameUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, amendedFirstContactName)
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists,
        header(HttpHeaderNames.Location).is(extractRelativeUrl(checkYourAnswersUrl))
      )
  )

  def getChangeFirstContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change first contact email' page")
      .get(changeFirstContactEmailUrl)
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToCheckYourAnswersForFirstContactEmailChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 1st contact email change (call before redirect)")
      .post(changeFirstContactEmailUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, amendedFirstContactEmail)
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists,
        header(HttpHeaderNames.Location).is(extractRelativeUrl(checkYourAnswersUrl))
      )
  )

  def getCheckYourAnswersPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'check your answers' page")
      .get(checkYourAnswersUrl)
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToAddSecondContact: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to add 2nd contact details (call before redirect)")
      .post(addAnotherContactPageUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, "no")
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(addSecondContactNameUrl))
      )
  )

  def getAddSecondContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 2nd contact name' page")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToAddSecondContactEmail: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'add 2nd contact email' page (call before redirect)")
      .post(addSecondContactNameUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, "TestNameForSecondContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(addSecondContactEmailUrl))
      )
  )

  def getAddSecondContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 2nd contact email' page")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
  )

  def getChangeSecondContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change second contact name' page")
      .get(changeSecondContactNameUrl)
      .check(status.is(200))
  )

  def continueToCheckYourAnswersForSecondContactNameChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 2nd contact name change (call before redirect)")
      .post(changeSecondContactNameUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, amendedSecondContactName)
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists,
        header(HttpHeaderNames.Location).is(extractRelativeUrl(checkYourAnswersUrl))
      )
  )

  def getChangeSecondContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change second contact email' page")
      .get(changeSecondContactEmailUrl)
      .check(status.is(200))
  )

  def continueToCheckYourAnswersForSecondContactEmailChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 2nd contact email change (call before redirect)")
      .post(changeSecondContactEmailUrl)
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam(valueKey, amendedSecondContactEmail)
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(checkYourAnswersUrl))
      )
  )

  def getCheckYourAnswersPageShowingBothContacts: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'check your answers' page to view both contacts")
      .get(checkYourAnswersUrl)
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def continueToSaveAndSubmitRegistration: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to save registration (call before redirect)")
      .post(session => redirectUrlFromSession(session))
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam("contacts[0].name", amendedFirstContactName)
      .formParam("contacts[0].email", amendedFirstContactEmail)
      .formParam("contacts[1].name", amendedSecondContactName)
      .formParam("contacts[1].email", amendedSecondContactEmail)
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(registrationPageUrl))
      )
  )

  def getRegistrationPageAfterSaving: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'registration' page following save and submission'")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
      .check(saveCsrfToken())
      .check( // Not sure whether to keep this check/request
        css("#company-details-status").find.transform(_.trim).is("Completed"),
        css("#contacts-details-status").find.transform(_.trim).is("Completed")
      )
  )

  def continueToSubmitRegistration: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'registration complete' page (call before redirect)")
      .post(session => redirectUrlFromSession(session))
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(registrationCompletePageUrl))
      )
  )

  def getRegistrationCompletePage: Seq[ActionBuilder] =
    convertHttpActionToSeq(
      http("Navigate to 'registration complete' page")
        .get(session => redirectUrlFromSession(session))
        .check(status.is(200))
        .check(css("h1").find.transform(_.trim).is("Registration Complete")) // Not sure whether to keep this check
    )

}
