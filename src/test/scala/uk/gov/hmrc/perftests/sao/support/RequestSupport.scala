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

package uk.gov.hmrc.perftests.sao.support

import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.{Configuration, ServicesConfiguration}
import uk.gov.hmrc.performance.simulation.JourneyPart

import java.util.concurrent.ConcurrentHashMap

object RequestSupport extends ServicesConfiguration {

  val authBaseUrl: String    = baseUrlFor("auth-login-stub")
  val companyBaseUrl: String = baseUrlFor("incorporated-entity-identification-frontend")
  val baseUrl: String        = baseUrlFor("senior-accounting-officer-registration-frontend")

  val authCookie: String = "mdtp=${mdtpCookie}"

  object CsrfHelper {
    def saveCsrfToken(sessionToken: String = "csrfToken") =
      regex("""name="csrfToken" value="([^"]+)""").saveAs(sessionToken)

    def logCsrf(sessionToken: String) = exec { session: Session =>
      println(s"$sessionToken = " + session(sessionToken).asOption[String].getOrElse("NOT FOUND"))
      session
    }
  }

  object RedirectStore {
    private val redirects                              = new ConcurrentHashMap[String, String]()
    def set(userId: String, redirectUrl: String): Unit = redirects.put(userId, redirectUrl)
    def get(userId: String): Option[String]            = Option(redirects.get(userId))
  }

  def fullRedirectUrl(user: String): String = RedirectStore.get(user).getOrElse("/")

  val saveRedirect = exec { session =>
    val user     = "testUser"
    val redirect = session("redirectUrl").as[String]
    RedirectStore.set(user, redirect)
    session.set("redirectUrl", redirect)
  }

  val sessionDebugBefore = exec { session =>
    println("Session ID before POST: " + session.userId)
    session
  }

  val sessionDebugAfter = exec { session =>
    println("Session ID after POST: " + session.userId)
    session
  }

  val logSessionInfo = exec { session =>
    println("--- Session Debug ---")
    println("Session: " + session)
    println("Session ID: " + session.userId)
    println("CSRF Token: " + session("crnCsrfToken").asOption[String])
    println("POST URL: " + session("postUrl").asOption[String])
    session
  }

  object requests extends Configuration {

    implicit def convertChainToActions(chain: ChainBuilder): Seq[ActionBuilder] = chain.actionBuilders

    implicit def convertHttpActionToSeq(act: HttpRequestBuilder): Seq[ActionBuilder] = Seq(act)

    implicit def convertActionToSeq(act: ActionBuilder): Seq[ActionBuilder] = Seq(act)

    implicit class AugmentJourneyParts(j: JourneyPart) {
      def withChainedActions(builders: Seq[ActionBuilder]*): JourneyPart = j.withActions(builders.flatten: _*)
    }

  }

}
