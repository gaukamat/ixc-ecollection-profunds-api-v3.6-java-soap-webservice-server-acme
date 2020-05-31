
package com.icici.xpress_connect.ws.ecollection;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transaction_id_type_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="transaction_id_type_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="UTR"/&gt;
 *     &lt;enumeration value="RRN"/&gt;
 *     &lt;enumeration value="TRANID"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "transaction_id_type_type")
@XmlEnum
public enum TransactionIdTypeType {

    UTR,
    RRN,
    TRANID;

    public String value() {
        return name();
    }

    public static TransactionIdTypeType fromValue(String v) {
        return valueOf(v);
    }

}
