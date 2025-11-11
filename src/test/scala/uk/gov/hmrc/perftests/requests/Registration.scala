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
import uk.gov.hmrc.perftests.support.GatlingSupport.convertHttpActionToSeq
import uk.gov.hmrc.perftests.requests.AuthorityRecord.redirectUrl
import uk.gov.hmrc.perftests.support.RequestSupport.{baseUrl, companyBaseUrl, mdtpCookie, saveCsrfToken}

object Registration {

  private val pageUrl: String         = s"$redirectUrl/business-match"
  private val registrationUrl: String = s"$baseUrl/senior-accounting-officer/registration"

  def getRegistrationPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the registration page")
      .get(session => session("redirectUrl").as[String])
      .header("Cookie", mdtpCookie)
      .check(status.is(200))
  )

  def getGenericRegistrationServiceStub: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to the 'Enter Company Details' page (stub)")
      .get(pageUrl)
      .header("Cookie", mdtpCookie)
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("Location").find.exists.saveAs("redirectUrl"))
      .check(header("Location").find.transform(_.contains(registrationUrl)).is(true))
  )

  def getCRNPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Get the customer registration number (CRN) page")
      .get(session => session("redirectUrl").as[String])
      .header("Cookie", mdtpCookie)
      .check(status.is(200))
      .check(bodyString.saveAs("responseBody"))
      .check(saveCsrfToken("crnCsrfToken"))
      .check(css("form", "action").saveAs("postUrl"))
  )

  def submitCRN: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Submit customer registration number (CRN)")
      .post(session => baseUrl + session("postUrl").as[String])
      .formParam("companyNumber", "A1B2C3")
      .formParam("csrfToken", session => session("crnCsrfToken").as[String])
      .check(status.is(303))
      .check(header("Location").saveAs("redirectUrl"))
  )
}
