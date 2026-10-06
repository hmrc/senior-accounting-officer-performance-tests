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
import RequestSupport.*
import uk.gov.hmrc.perftests.support.adt.*

object AuthRequests {

  def createAuthority(
                       redirectionUrl: String
                     ): HttpRequestBuilder =
    http("Submit form to create a new authority record")
      .post(authorityWizardPageUrl)
      .disableFollowRedirect
      .formParamMap(
        Map(
          "authorityId"                 -> "",
          "redirectionUrl"              -> redirectionUrl,
          CredentialStrength.fieldName -> CredentialStrength.Strong.value,
          ConfidenceLevel.fieldName    -> ConfidenceLevel.Cl50.value,
          AffinityGroup.fieldName      -> AffinityGroup.Organisation.value,
          "email"                       -> "user@test.com",
          CredentialRole.fieldName     -> CredentialRole.User.value,
          csrfTokenKey                 -> "#{csrfToken}"
        )
      )
      .check(status.is(303))
      .check(
        header(HttpHeaderNames.Location).is(redirectionUrl),
        header(HttpHeaderNames.Location).saveAs(redirectUrlKey)
      )
}

