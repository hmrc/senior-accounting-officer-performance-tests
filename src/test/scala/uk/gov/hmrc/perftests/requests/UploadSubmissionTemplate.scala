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
  )

  def postNotificationUpload: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Post to 'Notification Upload' page")
      .post(upscanProxyUrl)
      .formUpload("file", "data/example.csv")
      .formParamSeq(upscanParameters.map(name => (name, (session: Session) => session(name))))
      .check(status.is(303))
      // TODO : Upload Id and Key are held in the hidden fields on the form
      .check(header(HttpHeaderNames.Location).exists.saveAs(redirectUrlKey))
  )
}
