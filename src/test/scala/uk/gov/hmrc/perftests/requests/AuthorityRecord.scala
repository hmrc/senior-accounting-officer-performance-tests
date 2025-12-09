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

package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef._
import uk.gov.hmrc.perftests.support.GatlingSupport._
import uk.gov.hmrc.perftests.support.RequestSupport._
import uk.gov.hmrc.perftests.support.adt._

object AuthorityRecord {

  def getAuthorityWizardPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'Authority Wizard' page")
      .get(authorityWizardPageUrl)
      .check(status.is(200))
      .check(saveCsrfToken())
  )

  def submitNewAuthorityRecord: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Submit form to create a new authority record")
      .post(authorityWizardPageUrl)
      .disableFollowRedirect
      .formParam(csrfTokenKey, session => csrfTokenFromSession(session))
      .formParam("authorityId", "")
      .formParam("redirectionUrl", registrationPageUrl)
      .formParam(CredentialStrength.fieldName, CredentialStrength.Strong.value)
      .formParam(ConfidenceLevel.fieldName, ConfidenceLevel.Cl50.value)
      .formParam(AffinityGroup.fieldName, _ => AffinityGroup.Organisation.value)
      .formParam("email", "user@test.com")
      .formParam(CredentialRole.fieldName, CredentialRole.User.value)
      .check(status.is(303))
      .check(header(HttpHeaderNames.Location).is(registrationPageUrl))
  )
}
