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

package uk.gov.hmrc.perftests.requests.submission.certificate

import io.gatling.core.action.builder.ActionBuilder
import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.utils.helpers.URLHelpers.*
import uk.gov.hmrc.perftests.requests.utils.mappers.CertificateMappers.*
import uk.gov.hmrc.perftests.requests.utils.mappers.CommonMappers.*
import uk.gov.hmrc.perftests.requests.utils.requests.Requests.*

trait CertificateSubmission {
  self: PerformanceTestRunner =>

  def certificateSubmission(): Unit = {
    val actions: List[ActionBuilder] =
      List(
        getPage("Home", certificateHome),

        getPage("Additional Information", certificateAdditionalInfo),
        postPage("Additional Information", certificateAdditionalInfo, certificateWhoIsSubmitting, inputAdditionalInformation),

        getPage("Who Is Submitting", certificateWhoIsSubmitting),
        postPage("Who Is Submitting", certificateWhoIsSubmitting, certificateConfirmSao, radioButtonSao),

        getPage("Declaration", certificateConfirmSao),
        postPage("Declaration", certificateConfirmSao, certificateCheckYourAnswers, inputNameSao),

        getPage("Check Your Answers", certificateCheckYourAnswers, saveSubmissionToken = true),
        postPageWithQueryRedirect(
          "Check Your Answers",
          certificateCheckYourAnswers,
          certificateSubmitting,
          Map("certificateSubmissionToken" -> "#{certificateSubmissionToken}"),
          alternativeNextPage = Some(certificateHome)
        )
      ) ++
        waitForSubmissionCompletion(certificateHome) ++
        List(
          getPageFromRedirect("Task List Complete", baseSubmissionUrl),
          postTaskList,
          getPageFromRedirect("Certificate Confirmation", baseSubmissionUrl)
        )

    setup("certificate-submission", "Certificate: Submission")
      .withActions(actions *)
  }
}
