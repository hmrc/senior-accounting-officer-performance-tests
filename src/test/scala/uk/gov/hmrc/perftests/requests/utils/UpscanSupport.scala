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

import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.css.CssCheckType
import io.gatling.core.Predef.*
import jodd.lagarto.dom.NodeSelector
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object UpscanSupport extends ServicesConfiguration {

  val baseUpScanUrl: String = baseUrlFor("upscan-proxy")

  val upscanProxyUrl: String =
    s"$baseUpScanUrl/upscan/upload-proxy"

  val successActionRedirectUrlKey: String = "successActionRedirectUrl"

  val successActionRedirect: String          = "success_action_redirect"
  val xAmzCredential: String                 = "x-amz-credential"
  val xAmzMetaUpscanInitiateResponse: String = "x-amz-meta-upscan-initiate-response"
  val xAmzMetaOriginalFilename: String       = "x-amz-meta-original-filename"
  val xAmzAlgorithm: String                  = "x-amz-algorithm"
  val xAmzSignature: String                  = "x-amz-signature"
  val errorActionRedirect: String            = "error_action_redirect"
  val xAmzMetaSessionId: String              = "x-amz-meta-session-id"
  val xAmzMetaCallbackUrl: String            = "x-amz-meta-callback-url"
  val xAmzDate: String                       = "x-amz-date"
  val xAmzMetaUpscanInitiateReceived: String = "x-amz-meta-upscan-initiate-received"
  val xAmzMetaRequestId: String              = "x-amz-meta-request-id"
  val key: String                            = "key"
  val acl: String                            = "acl"
  val xAmzMetaConsumingService: String       = "x-amz-meta-consuming-service"
  val policy: String                         = "policy"

  val upscanParameters: List[String] = List(
    successActionRedirect,
    xAmzCredential,
    xAmzMetaUpscanInitiateResponse,
    xAmzMetaOriginalFilename,
    xAmzAlgorithm,
    xAmzSignature,
    errorActionRedirect,
    xAmzMetaSessionId,
    xAmzMetaCallbackUrl,
    xAmzDate,
    xAmzMetaUpscanInitiateReceived,
    xAmzMetaRequestId,
    key,
    acl,
    xAmzMetaConsumingService,
    policy
  )

  def saveUpscanParams(): Seq[CheckBuilder.Final[CssCheckType, NodeSelector]] =
    upscanParameters.map(param =>
      css(s"input[name=$param]", "value").exists.saveAs(param)
    )

  def saveSuccessActionRedirectUrl(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("input[name=success_action_redirect]", "value")
      .exists
      .saveAs(successActionRedirectUrlKey)

  def successActionRedirectUrlFromSession(session: Session): String =
    session(successActionRedirectUrlKey).as[String]
}