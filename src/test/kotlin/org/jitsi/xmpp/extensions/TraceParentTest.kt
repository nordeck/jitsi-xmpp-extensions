/*
 * Copyright @ 2024 - present 8x8, Inc.
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
package org.jitsi.xmpp.extensions

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.jitsi.xmpp.extensions.colibri2.IqProviderUtils
import org.jivesoftware.smack.parsing.SmackParsingException
import org.jivesoftware.smack.util.PacketParserUtils

class TraceParentTest : ShouldSpec() {
    init {
        IqProviderUtils.registerProviders()
        val provider = TraceParentProvider()

        context("Parsing a valid extension") {
            val traceParent = provider.parse(
                PacketParserUtils.getParserFor(
                    "<traceparent trace_id='1586bb16ccd4475a9f494bf43563ce28' " +
                        "parent_id='6e39899d78bfa0ae' trace_flags='35'/>"
                )
            )

            traceParent.traceId shouldBe "1586bb16ccd4475a9f494bf43563ce28"
            traceParent.parentId shouldBe "6e39899d78bfa0ae"
            traceParent.traceFlags shouldBe "35"
        }

        context("Parsing with missing traceId") {
            shouldThrow<SmackParsingException> {
                provider.parse(
                    PacketParserUtils.getParserFor(
                        "<traceparent parent_id='6e39899d78bfa0ae' trace_flags='35'/>"
                    )
                )
            }
        }

        context("Parsing with missing parentId") {
            shouldThrow<SmackParsingException> {
                provider.parse(
                    PacketParserUtils.getParserFor(
                        "<traceparent trace_id='1586bb16ccd4475a9f494bf43563ce28' trace_flags='35'/>"
                    )
                )
            }
        }

        context("Parsing with missing traceFlags") {
            shouldThrow<SmackParsingException> {
                provider.parse(
                    PacketParserUtils.getParserFor(
                        "<traceparent trace_id='1586bb16ccd4475a9f494bf43563ce28' " +
                            "parent_id='6e39899d78bfa0ae'/>"
                    )
                )
            }
        }
    }
}
