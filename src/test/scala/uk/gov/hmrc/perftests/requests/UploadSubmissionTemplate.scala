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

package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef._
import uk.gov.hmrc.perftests.support.GatlingSupport.convertHttpActionToSeq
import uk.gov.hmrc.perftests.support.RequestSupport._

object UploadSubmissionTemplate {

  def getNotificationStartPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'notification start' page")
      .get(notificationStartPageUrl)
      .check(status.is(200))
  )

  def getNotificationUploadPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'Notification Upload' page")
      .get(notificationUploadPageUrl)
      .check(status.is(200))
      .check(saveUpscanParams().map(e => checkBuilder2HttpCheck(e)(httpBodyCssCheckMaterializer)): _*)
      .check(saveSuccessActionRedirectUrl())
      .check(saveFormAction())
  )

  def postNotificationUpload: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Post to 'Notification Upload' page")
      .post(session => formActionFromSession(session))
      .disableFollowRedirect
      .formParamSeq(session => upscanParameters.map(name => name -> session(name).as[String]))
      .formUpload("file", "data/example.csv")
      .check(status.is(303))
      .check(header(HttpHeaderNames.Location)
        .transform(removeQueryParametersFromUrl)
        .is(session => removeQueryParametersFromUrl(successActionRedirectUrlFromSession(session)))
      )
  )

  def getUploadSuccessPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'Upload Success' page")
      .get(session => successActionRedirectUrlFromSession(session))
      .check(status.is(200))
  )

}
