/*
 * Copyright @ 2018 - present 8x8, Inc.
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
package org.jitsi.xmpp.extensions.jingleinfo;

import org.jitsi.xmpp.extensions.*;

import org.jitsi.xmpp.extensions.colibri2.*;
import org.jivesoftware.smack.provider.*;
import org.jivesoftware.smack.xml.*;

/**
 * Provider for the <tt>JingleInfoQueryIQ</tt>.
 *
 * @author Sebastien Vincent
 */
public class JingleInfoQueryIQProvider
    extends SafeParseIqProvider<JingleInfoQueryIQ>
{
    /**
     * STUN packet extension provider.
     */
    private final StunProvider stunProvider = new StunProvider();

    /**
     * Relay packet extension provider.
     */
    private final RelayProvider relayProvider = new RelayProvider();

    /**
     * Creates a new instance of the <tt>JingleInfoQueryIQProvider</tt> and
     * register all related extension providers. It is the responsibility of the
     * application to register the <tt>JingleInfoQueryIQProvider</tt> itself.
     */
    public JingleInfoQueryIQProvider()
    {
        ProviderManager.addExtensionProvider(
                ServerPacketExtension.ELEMENT,
                ServerPacketExtension.NAMESPACE,
            new DefaultPacketExtensionProvider<>(ServerPacketExtension.class));
    }

    /**
     * Parses a JingleInfoQueryIQ</tt>.
     *
     * @param parser an XML parser.
     * @return a new {@link JingleInfoQueryIQ} instance.
     * @throws Exception if an error occurs parsing the XML.
     */
    @Override
    protected JingleInfoQueryIQ doParse(XmlPullParser parser)
        throws Exception
    {
        boolean done = false;
        JingleInfoQueryIQ iq = new JingleInfoQueryIQ();

        // Now go on and parse the session element's content.
        while (!done)
        {
            XmlPullParser.Event eventType = parser.next();
            String elementName = parser.getName();

            if (eventType == XmlPullParser.Event.START_ELEMENT)
            {
                if (elementName.equals(StunPacketExtension.ELEMENT))
                {
                    iq.addExtension(stunProvider.parse(parser));
                }
                else if (elementName.equals(RelayPacketExtension.ELEMENT))
                {
                    iq.addExtension(relayProvider.parse(parser));
                }
            }
            if (eventType == XmlPullParser.Event.END_ELEMENT)
            {
                if (parser.getName().equals(JingleInfoQueryIQ.ELEMENT))
                {
                    done = true;
                }
            }
        }

        return iq;
    }
}
