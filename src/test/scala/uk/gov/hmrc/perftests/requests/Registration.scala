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
import uk.gov.hmrc.perftests.support.RequestSupport.{baseUrl, extractAndSaveCsrfToken, mdtpCookie}

object Registration {

  private val pageUrl: String           = s"$redirectUrl/business-match"
  private val registrationUrl: String   = s"$baseUrl/senior-accounting-officer/registration"
  private val contactDetailsUrl: String = s"$registrationUrl/contact-details"

  def getRegistrationPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the registration page")
      .get(session => session("redirectUrl").as[String])
      .header("Cookie", mdtpCookie)
      .check(status.is(200))
  )

  def getGenericRegistrationServiceStubBeforeRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Initial request to access the 'Enter Company Details' page (stub) without redirection")
      .get(pageUrl)
      .header("Cookie", mdtpCookie)
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains(registrationUrl)).is(true))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))
      .check(headerRegex("Set-Cookie", """mdtpdi=(.*)""").saveAs("mdtpdiCookie"))
  )

  def getGenericRegistrationServiceStubAfterRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Request to access the 'Enter Company Details' page (stub) with redirection")
      .get(session => session("redirectUrl").as[String])
      .header("Cookie", mdtpCookie)
      .check(status.is(200))
      .check(bodyString.saveAs("responseBody"))
      .check(extractAndSaveCsrfToken())
  )

  def sendResponseWithCompanyDetailsBeforeRedirect: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Stub response with company details (initial call before redirect)")
      .post(session => session("redirectUrl").as[String])
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains(registrationUrl)).is(true))
  )

  def getInterimRedirectToRegistrationPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Interim call to registration page with completed company details")
      .get(session => session("redirectUrl").as[String])
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(location => registrationUrl.contains(location)).is(true))
  )

  def getRegistrationPageWithCompleteCompanyDetails: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to registration page with completed company details")
      .get(session => s"$baseUrl${session("redirectUrl").as[String]}")
      .check(status.is(200))
      .check(currentLocation.transform(_.contains(registrationUrl)).is(true))
  )

  def getContactDetailsPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Provide contact details' page")
      .get(contactDetailsUrl)
      .check(status.is(200))
      .check(extractAndSaveCsrfToken())
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))
      .check(headerRegex("Set-Cookie", """mdtpdi=(.*)""").saveAs("mdtpdiCookie"))
      .check(currentLocation.saveAs("currentUrl"))
  )

  def continueToProvideFirstContactDetails: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to add 1st contact details (call before redirect)")
      .post(contactDetailsUrl)
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("first/name")).is(true))
  )

  def getAddFirstContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add 1st contact name' page")
      .get(session => s"$baseUrl${session("redirectUrl").as[String]}")
      .check(status.is(200))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToMissingFirstContactNameErrorPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'missing 1st contact name' error page")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "")
      .check(status.is(400))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("First contact details")))
      .check(css(".govuk-error-summary__title").find.transform(_.trim.equalsIgnoreCase("There is a problem")))
  )

  def continueToAddFirstContactEmail: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add 1st contact email' page (call before redirect)")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "ATestNameForFirstContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("first/email")).is(true))
  )

  def getAddFirstContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add 1st contact email' page")
      .get(session => s"$baseUrl${session("redirectUrl").as[String]}")
      .check(status.is(200))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("First contact details")))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToMissingFirstContactEmailErrorPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'missing 1st contact email' error page")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "")
      .check(status.is(400))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("First contact details")))
      .check(css(".govuk-error-summary__title").find.transform(_.trim.equalsIgnoreCase("There is a problem")))
  )

  def continueToAddMoreContactsQuestionPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add more contacts' question page (call before redirect)")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "ATestEmailForFirstContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("first/add-another")).is(true))
  )

  def getAddMoreContactsQuestionPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add more contacts' question page")
      .get(session => s"$baseUrl${session("redirectUrl").as[String]}")
      .check(status.is(200))
      .check(currentLocation.saveAs("addAnotherContactUrl"))
  )

  def continueToCheckYourAnswersForContact: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'Check your answers' page (call before redirect)")
      .post(session => session("addAnotherContactUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "yes")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("contact-details/check-your-answers")).is(true))
  )

  def getCheckYourAnswersPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Check your answers' page")
      .get(session => s"$baseUrl${session("redirectUrl").as[String]}")
      .check(status.is(200))
      .check(css(".govuk-heading-s").find.transform(_.trim.equalsIgnoreCase("First contact details")))
  )

  def continueToAddSecondContact: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to add 2nd contact details (call before redirect)")
      .post(session => session("addAnotherContactUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "no")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("second/name")).is(true))
  )

  def getAddSecondContactNamePage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add 2nd contact name' page")
      .get(session => s"$baseUrl${session("redirectUrl").as[String]}")
      .check(status.is(200))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("Second contact details")))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToMissingSecondContactNameErrorPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'missing 2nd contact name' error page")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "")
      .check(status.is(400))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("Second contact details")))
      .check(css(".govuk-error-summary__title").find.transform(_.trim.equalsIgnoreCase("There is a problem")))
  )

  def continueToAddSecondContactEmail: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add 2nd contact email' page (call before redirect)")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "ATestNameForSecondContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("second/email")).is(true))
  )

  def getAddSecondContactEmailPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Add 2nd contact email' page")
      .get(session => s"$baseUrl${session("redirectUrl").as[String]}")
      .check(status.is(200))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("Second contact details")))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToMissingSecondContactEmailErrorPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Continue to 'missing 2nd contact email' error page")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "")
      .check(status.is(400))
      .check(css(".govuk-caption-m").find.transform(_.trim.equalsIgnoreCase("Second contact details")))
      .check(css(".govuk-error-summary__title").find.transform(_.trim.equalsIgnoreCase("There is a problem")))
      .check(currentLocation.saveAs("requestUrl"))
  )

  def continueToCheckYourAnswersForBothContacts: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Check your answers' page (call before redirect)")
      .post(session => session("requestUrl").as[String])
      .formParam("csrfToken", session => session("csrfToken").as[String])
      .formParam("value", "ATestEmailForSecondContact")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains("contact-details/check-your-answers")).is(true))
  )

  // THIS STEP IS NOT RUNNING! IT MAY BE DOWN TO A FAILING STEP ALTHOUGH NOT SHOWING AS A FAILURE IN THE REPORT
  def getCheckYourAnswersPageForBothContacts: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Check your answers' page")
//      .get(session => session(s"$baseUrl${session("redirectUrl").as[String]}")
      .get(s"$contactDetailsUrl/check-your-answers")
      .check(status.is(200))
//      .check(currentLocation.saveAs("requestUrl"))
//      .check(css("h2:contains('First contact details')").exists)
//      .check(css("h2:contains('Second contact details')").exists)



    // THERE ARE 2 REMAINING STEPS TO ADD HERE TO CLOSE THIS TICKET.

  )
}
