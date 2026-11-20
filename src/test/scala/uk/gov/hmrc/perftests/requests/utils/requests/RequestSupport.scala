/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.requests.utils.requests

import io.gatling.core.Predef.*
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.css.CssCheckType
import jodd.lagarto.dom.NodeSelector
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.requests.utils.helpers.URLHelpers.*

object RequestSupport extends ServicesConfiguration {

  val formAction: String = ""

  def saveCsrfToken(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("input[name=csrfToken]", "value").exists.saveAs(csrfTokenKey)

  def saveFormAction(): CheckBuilder.Final[CssCheckType, NodeSelector] =
    css("form", "action").exists.saveAs(formAction)

  def formActionFromSession(session: Session): String = session(formAction).as[String]

  def redirectUrlFromSession(
      session: Session,
      baseUrl: String = baseRegistrationUrl
  ): String = {
    val redirectUrl = session(redirectUrlKey).as[String]

    if (redirectUrl.startsWith("http")) {
      redirectUrl
    } else {
      s"$baseUrl$redirectUrl"
    }
  }

  def businessMatchWithJourneyIdUrl(session: Session): String =
    s"$registrationBusinessMatchResultUrl${journeyIdFromSession(session)}"

  def journeyIdFromSession(session: Session): String = session(journeyIdKey).as[String]

  def extractRelativeUrl(url: String): String = {
    val uri   = java.net.URI.create(url)
    val query = Option(uri.getRawQuery).fold("")(queryParam => s"?$queryParam")
    s"${uri.getPath}$query"
  }

  val authorityRedirectUrl: String = if (runLocal) registrationStubEnrolmentUrl else registrationUrl

  val requiresStubEnrolment: Boolean = runLocal

  def removeQueryParametersFromUrl(url: String): String =
    url.split("\\?")(0)
}
