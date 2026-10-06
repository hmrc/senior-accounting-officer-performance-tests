/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.requests.utils

import io.gatling.core.Predef.*
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef.*
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import RequestSupport.*

import scala.concurrent.duration.*

object Requests extends ServicesConfiguration {

  def pause(duration: Int = 3): ActionBuilder =
    io.gatling.core.Predef.pause(duration.seconds).actionBuilders.head

  def getPage(
      name: String,
      page: String,
      saveToken: Boolean = false
  ): HttpRequestBuilder = {

    val request =
      http(s"Get $name Page")
        .get(page)
        .check(status.is(200))

    if (saveToken)
      request.check(css("input[name=csrfToken]", "value").saveAs("csrfToken"))
    else request
  }

  def getPageFromRedirect(
      name: String,
      saveToken: Boolean = false
  ): HttpRequestBuilder = {
    val request =
      http(s"Get $name Page")
        .get(session => redirectUrlFromSession(session))
        .check(status.is(200))

    if (saveToken) {
      request.check(saveCsrfToken())
    } else {
      request
    }
  }

  def followRedirect(
      name: String,
      nextPage: String
  ): HttpRequestBuilder =
    http(s"Get $name")
      .get(session => redirectUrlFromSession(session))
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location).is(extractRelativeUrl(nextPage))
      )

  def postPage(
      name: String,
      currentPage: String,
      nextPage: String,
      formParams: Map[String, String]
  ): HttpRequestBuilder =
    http(s"Post $name Page")
      .post(currentPage)
      .formParamMap(formParams + (csrfTokenKey -> "#{csrfToken}"))
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location)
          .is(extractRelativeUrl(nextPage))
      )

  def postPage(
      name: String,
      currentPage: String,
      nextPage: String
  ): HttpRequestBuilder =
    postPage(name, currentPage, nextPage, Map.empty)
}
