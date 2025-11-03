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

package uk.gov.hmrc.perftests.sao.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.perftests.sao.support.RequestSupport._
import uk.gov.hmrc.perftests.sao.requests.AuthLoginRequests.redirectUrl

object CompanyDetailsRequests {

  private val pageUrl: String = s"$redirectUrl/business-match"
  private val companyDetailsUrl: String = s"$companyBaseUrl/identify-your-incorporated-business/"

  val navigateToRegistrationPage: HttpRequestBuilder = http("Navigate to the registration page")
    .get(session => session("redirectUrl").as[String])
    .header("Cookie", authCookie)
    .check(status.is(200))

  val navigateToCompanyDetails: HttpRequestBuilder = http("Navigate to the company details page")
    .get(pageUrl)
    .header("Cookie", authCookie)
    .disableFollowRedirect
    .check(status.is(303))
    .check(header("Location").transform(_.contains(companyDetailsUrl)).is(true))
    .check(header("Location").saveAs("redirectUrl"))

  val getCRNPage: HttpRequestBuilder = http("Get the customer registration number (CRN) page")
    .get(session => session("redirectUrl").as[String])
    .header("Cookie", authCookie)
    .check(status.is(200))
    .check(bodyString.saveAs("responseBody"))
    .check(CsrfHelper.saveCsrfToken("crnCsrfToken"))
    .check(css("form", "action").saveAs("postUrl"))

  val submitCRN: HttpRequestBuilder = http("Submit customer registration number (CRN)")
    .post(session => companyBaseUrl + session("postUrl").as[String])
    .formParam("companyNumber", "A1B2C3")
    .formParam("csrfToken", session => session("crnCsrfToken").as[String])
    .check(status.is(303))
    .check(header("Location").saveAs("redirectUrl"))



}
