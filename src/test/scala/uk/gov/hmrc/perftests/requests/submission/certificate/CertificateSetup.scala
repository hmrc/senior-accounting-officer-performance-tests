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
import uk.gov.hmrc.perftests.requests.utils.mappers.CommonMappers.*

trait CertificateSetup {
  self: PerformanceTestRunner =>

  def certificateSetup(): Unit =
    setup("certificate-setup", "Certificate: Setup") withActions (
      getPage("SAO Home", saoLandingPage),

      getPage("Submission Type", submissionType, saveToken = true),
      postPage("Submission Type", submissionType, certificateHome1, radioButtonCertificate),

      getPage("Home", certificateHome1)
    )
}
