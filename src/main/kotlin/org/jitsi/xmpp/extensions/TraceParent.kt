/*
 * Copyright @ 2023 - present 8x8, Inc.
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

import org.jivesoftware.smack.packet.XmlEnvironment
import org.jivesoftware.smack.parsing.SmackParsingException
import org.jivesoftware.smack.provider.ExtensionElementProvider
import org.jivesoftware.smack.xml.XmlPullParser
import org.jivesoftware.smack.xml.XmlPullParserException
import java.io.IOException

class TraceParent(val traceId: String, val parentId: String, val traceFlags: String) :
    AbstractPacketExtension(NAMESPACE, ELEMENT) {
    init {
        setAttribute(TRACE_ID_ATTR_NAME, traceId)
        setAttribute(PARENT_ID_ATTR_NAME, parentId)
        setAttribute(TRACE_FLAGS_ATTR_NAME, traceFlags)
    }

    companion object {
        const val ELEMENT = "traceparent"
        const val NAMESPACE = "jitsi:opentelemetry"
        const val TRACE_ID_ATTR_NAME = "trace_id"
        const val PARENT_ID_ATTR_NAME = "parent_id"
        const val TRACE_FLAGS_ATTR_NAME = "trace_flags"
    }
}

class TraceParentProvider : ExtensionElementProvider<TraceParent>() {
    @Throws(XmlPullParserException::class, IOException::class, SmackParsingException::class)
    override fun parse(parser: XmlPullParser, depth: Int, xml: XmlEnvironment?): TraceParent {
        val traceId = parser.getAttributeValue("", TraceParent.TRACE_ID_ATTR_NAME)
            ?: throw SmackParsingException.RequiredAttributeMissingException(
                "Missing '${TraceParent.TRACE_ID_ATTR_NAME}' attribute"
            )
        val parentId = parser.getAttributeValue("", TraceParent.PARENT_ID_ATTR_NAME)
            ?: throw SmackParsingException.RequiredAttributeMissingException(
                "Missing '${TraceParent.PARENT_ID_ATTR_NAME}' attribute"
            )
        val traceFlags = parser.getAttributeValue("", TraceParent.TRACE_FLAGS_ATTR_NAME)
            ?: throw SmackParsingException.RequiredAttributeMissingException(
                "Missing '${TraceParent.TRACE_FLAGS_ATTR_NAME}' attribute"
            )
        return TraceParent(traceId, parentId, traceFlags)
    }
}
