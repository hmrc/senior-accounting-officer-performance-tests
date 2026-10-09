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

object CommonMappers {

  val textInput: Map[String, String]             = Map("value" -> "AbCdEfGhI")
  val textInputTestUser1: Map[String, String]    = Map("value" -> "Test User 1")
  val textInputTestUser2: Map[String, String]    = Map("value" -> "Test User 2")
  val numberInput: Map[String, String]           = Map("value" -> "456785643")
  val emailAddress: Map[String, String]          = Map("value" -> "test@example.com")
  val emailAddressTestUser1: Map[String, String] = Map("value" -> "test1@example.com")
  val emailAddressTestUser2: Map[String, String] = Map("value" -> "test2@example.com")

  val radioButtonYes: Map[String, String] = Map("value" -> "yes")
  val radioButtonNo: Map[String, String]  = Map("value" -> "no")

  val radioButtonTrue: Map[String, String]  = Map("value" -> "true")
  val radioButtonFalse: Map[String, String] = Map("value" -> "false")

  val radioButtonNotification: Map[String, String] = Map("value" -> "notification")
  val radioButtonCertificate: Map[String, String]  = Map("value" -> "certificate")

  def dateInput(day: String, month: String, year: String): Map[String, String] =
    Map(
      "value.day"   -> day,
      "value.month" -> month,
      "value.year"  -> year
    )

  val currentSaoStartDate: Map[String, String]  = dateInput("1", "9", "2026")
  val previousSaoStartDate: Map[String, String] = dateInput("1", "1", "2026")
  val previousSaoEndDate: Map[String, String]   = dateInput("31", "8", "2026")

}
