
package com.icici.xpress_connect.ws.ecollection;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for payment_mode_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="payment_mode_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEFT"/&gt;
 *     &lt;enumeration value="RTGS"/&gt;
 *     &lt;enumeration value="IMPS"/&gt;
 *     &lt;enumeration value="FT"/&gt;
 *     &lt;enumeration value="UTR"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "payment_mode_type")
@XmlEnum
public enum PaymentModeType {

    NEFT,
    RTGS,
    IMPS,
    FT,
    UTR;

    public String value() {
        return name();
    }

    public static PaymentModeType fromValue(String v) {
        return valueOf(v);
    }

}
