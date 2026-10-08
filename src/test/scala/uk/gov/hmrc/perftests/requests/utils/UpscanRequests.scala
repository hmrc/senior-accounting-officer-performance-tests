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
import uk.gov.hmrc.perftests.requests.utils.RequestSupport.*
import uk.gov.hmrc.perftests.requests.utils.UpscanSupport.*

object UpscanRequests {

  def getUploadPage: HttpRequestBuilder =
    http("Get Notification Upload Page")
      .get(notificationUpload)
      .check(status.is(200))
      .check(
        saveUpscanParams()
          .map(e => checkBuilder2HttpCheck(e)(httpBodyCssCheckMaterializer)): _*
      )
      .check(saveSuccessActionRedirectUrl())
      .check(saveFormAction())

  def uploadFile: HttpRequestBuilder =
    http("Upload Notification File")
      .post(session => formActionFromSession(session))
      .disableFollowRedirect
      .formParamSeq(session => upscanParameters.map(name => name -> session(name).as[String]))
      .formUpload("file", "data/sao-valid.csv")
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location)
          .transform(removeQueryParametersFromUrl)
          .is(session =>
            removeQueryParametersFromUrl(
              successActionRedirectUrlFromSession(session)
            )
          )
      )

  def getUploadSuccessPage: HttpRequestBuilder =
    http("Get Upload Success Page")
      .get(session => successActionRedirectUrlFromSession(session))
      .check(status.is(200))

  def followUploadSuccessRedirect: HttpRequestBuilder =
    http("Poll Upload Success")
      .get(session => successActionRedirectUrlFromSession(session))
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location)
          .transform(extractRelativeUrl)
          .is(extractRelativeUrl(notificationUploadTable))
      )

  def getUploadTableRedirect: HttpRequestBuilder =
    http("Get Notification: Upload Table")
      .get(notificationUploadTable)
      .disableFollowRedirect
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location)
          .transform(extractRelativeUrl)
          .is(extractRelativeUrl(notificationHome))
      )
}
