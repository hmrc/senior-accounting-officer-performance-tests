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

package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef._
import uk.gov.hmrc.perftests.requests.AuthorityRecord.redirectUrl
import uk.gov.hmrc.perftests.support.GatlingSupport.convertHttpActionToSeq
import uk.gov.hmrc.perftests.support.RequestSupport.{assertAllValuesPresentInSelector, changeFirstContactEmailUrl, changeFirstContactNameUrl, changeSecondContactEmailUrl, changeSecondContactNameUrl, checkYourAnswersPage, contactDetailsPage, currentRedirectUrl, currentRequestUrl, extractAndSaveCsrfToken, mdtpCookie, registrationPage}

object Registration {

  private val pageUrl: String = s"$redirectUrl/business-match"

  def getRegistrationPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'registration' page")
      .get(session => session("redirectUrl").as[String])
      .header("Cookie", mdtpCookie)
      .check(status.is(200))
  )

  def getGenericRegistrationServiceStubBeforeRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Initial request (before redirect) to access 'enter company details' page (stub)")
      .get(pageUrl)
      .header("Cookie", mdtpCookie)
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains(registrationPage)).is(true))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))
      .check(headerRegex("Set-Cookie", """mdtpdi=(.*)""").saveAs("mdtpdiCookie"))
  )

  def getGenericRegistrationServiceStubAfterRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Request to access 'enter company details' page (stub)")
      .get(session => session("redirectUrl").as[String])
      .header("Cookie", mdtpCookie)
      .check(status.is(200))
      .check(bodyString.saveAs("responseBody"))
      .check(extractAndSaveCsrfToken())
  )

  def sendResponseWithCompanyDetailsBeforeRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Send stub response with company details (initial call before redirect)")
      .post(session => session("redirectUrl").as[String])
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains(registrationPage)).is(true))
  )

  def getInterimRedirectToRegistrationPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Interim call to 'registration' page with completed company details")
      .get(session => session("redirectUrl").as[String])
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(location => registrationPage.contains(location)).is(true))
  )

  def getRegistrationPageWithCompleteCompanyDetails: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'registration' page with completed company details")
      .get(session => currentRedirectUrl(session))
      .check(status.is(200))
      .check(currentLocation.transform(_.contains(registrationPage)).is(true))
  )

  def getContactDetailsPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'provide contact details' page")
      .get(contactDetailsPage)
      .check(status.is(200))
      .check(extractAndSaveCsrfToken())
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))
      .check(headerRegex("Set-Cookie", """mdtpdi=(.*)""").saveAs("mdtpdiCookie"))
      .check(currentLocation.saveAs("currentUrl"))
  )

  def continueToProvideFirstContactDetails: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to add 1st contact details (call before redirect)")
      .post(contactDetailsPage)
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("first/name")).is(true))
  )

  def getAddFirstContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 1st contact name' page")
      .get(session => currentRedirectUrl(session))
      .check(status.is(200))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToAddFirstContactEmail: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'add 1st contact email' page (call before redirect)")
      .post(session => currentRequestUrl(session))
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "ATestNameForFirstContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("first/email")).is(true))
  )

  def getAddFirstContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 1st contact email' page")
      .get(session => currentRedirectUrl(session))
      .check(status.is(200))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("First contact details")))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToAddMoreContactsQuestionPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'add more contacts' question page (call before redirect)")
      .post(session => currentRequestUrl(session))
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "aTestEmailForFirstContact@tester.co.uk")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("first/add-another")).is(true))
  )

  def getAddMoreContactsQuestionPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add more contacts' question page")
      .get(session => currentRedirectUrl(session))
      .check(status.is(200))
      .check(currentLocation.saveAs("areAllContactsAddedUrl"))
  )

  def continueToCheckYourAnswersForContact: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page (call before redirect)")
      .post(session => session("areAllContactsAddedUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "yes")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("contact-details/check-your-answers")).is(true))
  )

  def getChangeFirstContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change first contact name' page")
      .get(changeFirstContactNameUrl)
      .check(status.is(200))
  )

  def continueToCheckYourAnswersForFirstContactNameChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 1st contact name change (call before redirect)")
      .post(changeFirstContactNameUrl)
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "amendedFirstContactName")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.transform(_.contains("contact-details/check-your-answers")).is(true))
  )

  def getChangeFirstContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change first contact email' page")
      .get(changeFirstContactEmailUrl)
      .check(status.is(200))
  )

  def continueToCheckYourAnswersForFirstContactEmailChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 1st contact email change (call before redirect)")
      .post(changeFirstContactEmailUrl)
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "amendedEmail@1st-Contact.com")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.transform(_.contains("contact-details/check-your-answers")).is(true))
  )

  def getCheckYourAnswersPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Check your answers' page")
      .get(checkYourAnswersPage)
      .check(status.is(200))
      .check(css(".govuk-heading-s").find.transform(_.trim.equalsIgnoreCase("First contact details")))
  )

  def continueToAddSecondContact: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to add 2nd contact details (call before redirect)")
      .post(session => session("areAllContactsAddedUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "no")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("second/name")).is(true))
  )

  def getAddSecondContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 2nd contact name' page")
      .get(session => currentRedirectUrl(session))
      .check(status.is(200))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("Second contact details")))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToAddSecondContactEmail: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'add 2nd contact email' page (call before redirect)")
      .post(session => currentRequestUrl(session))
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "ATestNameForSecondContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("second/email")).is(true))
  )

  def getAddSecondContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'add 2nd contact email' page")
      .get(session => currentRedirectUrl(session))
      .check(status.is(200))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("Second contact details")))
  )

  def getChangeSecondContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change second contact name' page")
      .get(changeSecondContactNameUrl)
      .check(status.is(200))
  )

  def continueToCheckYourAnswersForSecondContactNameChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 2nd contact name change (call before redirect)")
      .post(changeSecondContactNameUrl)
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "amendedSecondContactName")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.transform(_.contains("contact-details/check-your-answers")).is(true))
  )

  def getChangeSecondContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'change second contact email' page")
      .get(changeSecondContactEmailUrl)
      .check(status.is(200))
  )

  def continueToCheckYourAnswersForSecondContactEmailChange: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'check your answers' page after 2nd contact email change (call before redirect)")
      .post(changeSecondContactEmailUrl)
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "amendedEmail@2nd-Contact.com")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("contact-details/check-your-answers")).is(true))
  )

  def getCheckYourAnswersPageShowingBothContacts: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'check your answers' page to view both contacts")
      .get(checkYourAnswersPage)
      .check(status.is(200))
      .check(
        assertAllValuesPresentInSelector(
          "dd",
          Set(
            "amendedFirstContactName",
            "amendedEmail@1st-Contact.com",
            "amendedSecondContactName",
            "amendedEmail@2nd-Contact.com"
          )
        )
      )
  )

  def continueToSaveAndSubmitRegistration: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to save registration (call before redirect)")
      .post(session => currentRedirectUrl(session))
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("contacts[0].name", "amendedFirstContactName")
      .formParam("contacts[0].email", "amendedEmail@1st-Contact.com")
      .formParam("contacts[1].name", "amendedSecondContactName")
      .formParam("contacts[1].email", "amendedEmail@2nd-Contact.com")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("/senior-accounting-officer/registration")).is(true))
  )

  def getRegistrationPageAfterSaving: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'registration' page following save and submission'")
      .get(session => currentRedirectUrl(session))
      .check(status.is(200))
      .check(css("#company-details-status").find.transform(_.trim).is("Completed"))
      .check(css("#contacts-details-status").find.transform(_.trim).is("Completed"))
  )

  def continueToSubmitRegistration: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'registration complete' page (call before redirect)")
      .post(session => currentRedirectUrl(session))
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(
        header("Location").find
          .transform(_.contains("/senior-accounting-officer/registration/registration-complete"))
          .is(true)
      )
  )

  def getRegistrationCompletePage: Seq[ActionBuilder] =
    convertHttpActionToSeq(
      http("Navigate to 'registration complete' page")
        .get(session => currentRedirectUrl(session))
        .check(status.is(200))
        .check(css("h1").find.transform(_.trim).is("Registration Complete"))
    )
}
