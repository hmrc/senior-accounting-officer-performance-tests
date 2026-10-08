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
import io.gatling.http.Predef.*
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

import RequestSupport.*

object GrsRequests extends ServicesConfiguration {

  def getGrsPage(
      name: String
  ): HttpRequestBuilder =
    http(s"Get $name Page")
      .get(session => redirectUrlFromSession(session))
      .check(status.is(200))
      .check(
        saveCsrfToken(),
        css("input[name=status]", "value").saveAs("grsStatus"),
        css("textarea[name=body]").saveAs("grsBody")
      )

  def getGrsRedirect(
      name: String,
      page: String
  ): HttpRequestBuilder =
    http(s"Get $name Page")
      .get(page)
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location)
          .transform(_.contains(grsStubPathSegment))
          .is(true),
        header(HttpHeaderNames.Location)
          .transform { location =>
            journeyIdRegexPattern.r
              .findFirstIn(location)
              .getOrElse("")
          }
          .not("")
          .saveAs(journeyIdKey)
      )

  def postGrsPage(
      name: String
  ): HttpRequestBuilder =
    http(s"Post $name Page")
      .post(session => redirectUrlFromSession(session))
      .formParamMap(
        Map(
          csrfTokenKey -> "#{csrfToken}",
          "status"     -> "#{grsStatus}",
          "body"       -> "#{grsBody}"
        )
      )
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).saveAs(redirectUrlKey),
        header(HttpHeaderNames.Location)
          .transform(extractRelativeUrl)
          .is(session => businessMatchWithJourneyIdUrl(session))
      )
}
