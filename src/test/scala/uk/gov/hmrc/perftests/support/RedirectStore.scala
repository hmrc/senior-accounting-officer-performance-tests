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

package uk.gov.hmrc.perftests.support

import java.util.concurrent.ConcurrentHashMap

object RedirectStore {
  private val redirects = new ConcurrentHashMap[String, String]()

  def set(userId: String, redirectUrl: String): Unit = redirects.put(userId, redirectUrl)
  def get(userId: String): Option[String]            = Option(redirects.get(userId))
}
