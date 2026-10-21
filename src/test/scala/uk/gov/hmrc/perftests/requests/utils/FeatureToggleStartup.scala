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

import java.net.{CookieManager, CookiePolicy, URI}
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

import scala.util.matching.Regex

trait FeatureToggleStartup {

  private val toggleName = "fault-tolerance"
  private val toggleValue = "4"

  def ensureFaultToleranceEnabled(featureToggleUrl: String): Unit = {
    val cookies = new CookieManager()
    cookies.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

    val client = HttpClient
      .newBuilder()
      .cookieHandler(cookies)
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build()

    def getPage(): String = {
      val request = HttpRequest
        .newBuilder(URI.create(featureToggleUrl))
        .GET()
        .build()

      val response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      )

      require(response.statusCode() == 200,
        s"Feature toggle GET failed: ${response.statusCode()}")

      response.body()
    }

    def checkedInputs(html: String): Seq[(String, String)] = {
      val inputPattern: Regex = "(?is)<input\\b[^>]*>".r
      val attributePattern: Regex =
        """(?i)([\w-]+)\s*=\s*["']([^"']*)["']""".r

      inputPattern.findAllIn(html).flatMap { input =>
        val attributes = attributePattern
          .findAllMatchIn(input)
          .map(m => m.group(1).toLowerCase -> m.group(2))
          .toMap

        if (
          attributes.get("type").contains("checkbox") &&
            input.matches("(?is).*\\schecked(?:\\s|=|>).*")
        ) {
          for {
            name <- attributes.get("name")
            value <- attributes.get("value")
          } yield name -> value
        } else None
      }.toSeq
    }

    val page = getPage()
    val enabled = checkedInputs(page).exists(_._1 == toggleName)

    if (enabled) {
      println(s"Feature toggle '$toggleName' already enabled.")
    } else {
      val csrfPattern =
        """<input\b(?=[^>]*name=["']csrfToken["'])(?=[^>]*value=["']([^"']+)["'])[^>]*>""".r

      val csrfToken = csrfPattern
        .findFirstMatchIn(page)
        .map(_.group(1))
        .getOrElse(throw new IllegalStateException("CSRF token not found"))

      val fields =
        checkedInputs(page).filterNot(_._1 == toggleName) ++
          Seq("csrfToken" -> csrfToken, toggleName -> toggleValue)

      val body = fields.map { case (name, value) =>
        s"${URLEncoder.encode(name, UTF_8)}=${URLEncoder.encode(value, UTF_8)}"
      }.mkString("&")

      val request = HttpRequest
        .newBuilder(URI.create(featureToggleUrl))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build()

      val response = client.send(
        request,
        HttpResponse.BodyHandlers.discarding()
      )

      require(
        response.statusCode() >= 200 && response.statusCode() < 400,
        s"Feature toggle POST failed: ${response.statusCode()}"
      )

      val verificationPage = getPage()

      require(
        checkedInputs(verificationPage).exists(_._1 == toggleName),
        s"Feature toggle '$toggleName' was not enabled"
      )

      println(s"Feature toggle '$toggleName' successfully enabled.")
    }
  }
}
