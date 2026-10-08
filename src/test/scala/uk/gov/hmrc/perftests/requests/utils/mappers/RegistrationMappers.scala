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

package uk.gov.hmrc.perftests.requests.utils.mappers

object RegistrationMappers {

  val contactDetails: Map[String, String] =
    Map(
      "contacts[0].name"  -> "Test User 1",
      "contacts[0].email" -> "test1@example.com",
      "contacts[1].name"  -> "Test User 2",
      "contacts[1].email" -> "test2@example.com"
    )
}
