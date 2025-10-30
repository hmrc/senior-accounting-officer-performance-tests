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
import uk.gov.hmrc.perftests.sao.Request_Helper._
import uk.gov.hmrc.perftests.sao.requests.AuthLoginRequests.redirectUrl

object CompanyDetailsRequests {

  private val pageUrl: String = s"$redirectUrl/business-match"
  private val companyDetailsUrl: String = s"$companyBaseUrl/identify-your-incorporated-business/"


  def navigateToRegistrationPage: HttpRequestBuilder = http("Registration Page")
    .get(redirectUrl)
    .header("Cookie", authCookie)
    .check(status.is(200))

  def navigateToCompanyDetails: HttpRequestBuilder = http("Navigate to Company Details")
    .get(s"$pageUrl")
    .header("Cookie", authCookie)
    .check(status.is(303))
    .check(header("Location").transform(_.contains(s"$companyDetailsUrl")).is(true))
    .check(header("Location").transform(_.replaceAll("/[^/]*$", "")).saveAs("companyNumberUrl"))

  def getCRNPage: HttpRequestBuilder = http("Get CRN Page")
    .get("${companyNumberUrl}")
    .header("Cookie", authCookie)
    .check(status.is(200))
    .check(saveCsrfToken)


  def submitCRN: HttpRequestBuilder = http("Submit Customer Registration Number")
    .post("${companyNumberUrl}/company-number")
    .formParam("companyName", "A1B2C3")
    .formParam("csrfToken", "#{csrfToken}")
    .check(status.is(303))
    //    .check(header("Location").is("${companyNumberUrl}/confirm-business-name"))



  def getBusinessNamePage: HttpRequestBuilder = http("Get Business Name Page")
    .get("${companyNumberUrl}/confirm-business-name")
    .header("Cookie", authCookie)
    .check(status.is(200))
    .check(saveCsrfToken)


}
