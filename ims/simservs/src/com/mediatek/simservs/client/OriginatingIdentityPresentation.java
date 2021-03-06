
package com.mediatek.simservs.client;

import com.mediatek.simservs.xcap.XcapException;
import com.mediatek.xcap.client.uri.XcapUri;

import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Originating Identity Presentation class.
 */
public class OriginatingIdentityPresentation extends SimservType {

    public static final String NODE_NAME = "originating-identity-presentation";

    /**
     * Constructor.
     *
     * @param documentUri       XCAP document URI
     * @param parentUri         XCAP root directory URI
     * @param intendedId        X-3GPP-Intended-Id
     * @throws XcapException    if XCAP error
     * @throws ParserConfigurationException if parser configuration error
     */
    public OriginatingIdentityPresentation(XcapUri documentUri, String parentUri,
            String intendedId) throws XcapException,
            ParserConfigurationException {
        super(documentUri, parentUri, intendedId);
    }

    @Override
    public void initServiceInstance(Document domDoc) {
        // No content need to be parsed for this service
    }

    @Override
    protected String getNodeName() {
        return NODE_NAME;
    }
}
