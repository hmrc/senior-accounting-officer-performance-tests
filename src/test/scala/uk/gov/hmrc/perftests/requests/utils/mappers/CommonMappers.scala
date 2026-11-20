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

import uk.gov.hmrc.perftests.requests.utils.feeder.FeederDefs

object CommonMappers extends FeederDefs {

  def mapValue(input: String, idName: String = "value"): Map[String, String] = Map(idName -> input)

  def inputNameSao: Map[String, String]               = mapValue(nameSao)
  def inputNameSaoPrevious: Map[String, String]       = mapValue(nameSaoPrevious)
  def inputNameCDFirst: Map[String, String]           = mapValue(nameCDFirst)
  def inputNameCDSecond: Map[String, String]          = mapValue(nameCDSecond)
  def inputEmailSAO: Map[String, String]              = mapValue(emailSAO)
  def inputEmailCDFirst: Map[String, String]          = mapValue(emailCDFirst)
  def inputEmailCDSecond: Map[String, String]         = mapValue(emailCDSecond)
  def inputAdditionalInformation: Map[String, String] = mapValue(additionalInformation)

  val radioButtonYes: Map[String, String] = mapValue("yes")
  val radioButtonNo: Map[String, String]  = mapValue("no")

  val radioButtonTrue: Map[String, String]  = mapValue("true")
  val radioButtonFalse: Map[String, String] = mapValue("false")

  val radioButtonNotification: Map[String, String] = mapValue("notification")
  val radioButtonCertificate: Map[String, String]  = mapValue("certificate")

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
