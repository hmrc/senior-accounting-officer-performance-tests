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

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.utils.RequestSupport.*
import uk.gov.hmrc.perftests.requests.utils.Requests.*
import uk.gov.hmrc.perftests.requests.utils.UpscanRequests.*

trait CertificateUpload {
  self: PerformanceTestRunner =>

  def certificateUpload(): Unit =
    setup("certificate-upload", "Certificate: Upload") withActions (
      getPage("Home", certificateHome2),

      getUploadPage(certificateUploadFile),
      uploadFile,
      pause(10),
      followUploadSuccessRedirect(certificateReviewQualified),

      getPage("Review Qualified", certificateReviewQualified),
      postPage("Review Qualified", certificateReviewQualified, certificateReviewUnqualified),

      getPage("Review Unqualified", certificateReviewUnqualified),
      postPage("Review Unqualified", certificateReviewUnqualified, certificateHome3)
    )
}
