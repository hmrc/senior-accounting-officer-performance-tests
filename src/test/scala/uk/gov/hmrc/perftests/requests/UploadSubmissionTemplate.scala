package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.core.Predef.find2Validate
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef._
import io.gatling.http.Predef.{http, status}
import uk.gov.hmrc.perftests.support.GatlingSupport.convertHttpActionToSeq
import uk.gov.hmrc.perftests.support.RequestSupport.{notificationStartPageUrl, registrationPageUrl}

object UploadSubmissionTemplate {

  def getNotificationStartPage: Seq[ActionBuilder] = convertHttpActionToSeq(
    http("Navigate to 'notification start' page")
      .get(notificationStartPageUrl)
      .check(status.is(200))
  )
}
