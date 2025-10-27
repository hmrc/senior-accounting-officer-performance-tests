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

object CompanyDetailsRequests {

  private val pageUrl: String = baseUrl + "/registration/business-match"
  private val companyStubUrl: String = baseUrl + "/registration/test-only/grs-stub/"

  def navigateToCompanyStubPage: HttpRequestBuilder = http("Navigate to Company Details Stub Response Page")
    .get(s"$pageUrl")
    .header("Cookie", authCookie)
    .check(status.is(303))
    .check(header("Location").transform(_.contains(s"$companyStubUrl")).is(true))
    .check(header("Location").saveAs("redirectUrl"))


  def submitStubResponse: HttpRequestBuilder = http("Submit Company Details Stub Response")
    .post("${redirectUrl}")
    .formParam("csrfToken", "${csrfToken}")
    .check(status.is(303))
    .check(bodyString.transformOption { body =>
      println("POST response body:\n" + body)
      Some(body)
    })
    .check(header("Location").saveAs("redirectUrl"))

}
