
package com.icici.xpress_connect.ws.ecollection;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for intimation_requestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="intimation_requestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}api_version"/&gt;
 *         &lt;group ref="{http://www.icicibank.com/api/acme}requestHeaderType"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_code"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}virtual_account_number"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}transaction_amount"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}currency_code"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}payment_mode"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}transaction_id"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}transaction_id_type"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}transaction_datetime"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}sender_name"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}sender_mobile_communication_number" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}sender_account_number"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}sender_bank_name" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}sender_ifsc_code"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_account_name" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_mobile_communication_number" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_account_number"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_bank_name" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_ifsc_code" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}narration_1" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}narration_2" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}informational_message" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}external_system_reference_1" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}external_system_reference_2" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_remarks" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.icicibank.com/api/acme}client_data_ext" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "intimation_requestType", propOrder = {
    "apiVersion",
    "requestId",
    "requestDatetime",
    "parentRequestId",
    "parentRequestDatetime",
    "channelId",
    "senderSystemId",
    "senderSystemUserId",
    "senderSystemUserIdType",
    "requestPriority",
    "resentIndicator",
    "originalRequestId",
    "originalRequestDatetime",
    "originalChannelId",
    "originalSenderSystemId",
    "originalSenderSystemUserId",
    "originalSenderSystemUserIdType",
    "originalRequestPriority",
    "testIndicator",
    "clientCode",
    "virtualAccountNumber",
    "transactionAmount",
    "currencyCode",
    "paymentMode",
    "transactionId",
    "transactionIdType",
    "transactionDatetime",
    "senderName",
    "senderMobileCommunicationNumber",
    "senderAccountNumber",
    "senderBankName",
    "senderIfscCode",
    "clientAccountName",
    "clientMobileCommunicationNumber",
    "clientAccountNumber",
    "clientBankName",
    "clientIfscCode",
    "narration1",
    "narration2",
    "informationalMessage",
    "externalSystemReference1",
    "externalSystemReference2",
    "clientRemarks",
    "clientDataExt"
})
public class IntimationRequestType {

    @XmlElement(name = "api_version", required = true)
    protected String apiVersion;
    @XmlElement(name = "request_id", required = true)
    protected String requestId;
    @XmlElement(name = "request_datetime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestDatetime;
    @XmlElement(name = "parent_request_id")
    protected String parentRequestId;
    @XmlElement(name = "parent_request_datetime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar parentRequestDatetime;
    @XmlElement(name = "channel_id")
    protected String channelId;
    @XmlElement(name = "sender_system_id")
    protected String senderSystemId;
    @XmlElement(name = "sender_system_user_id")
    protected String senderSystemUserId;
    @XmlElement(name = "sender_system_user_id_type")
    protected String senderSystemUserIdType;
    @XmlElement(name = "request_priority")
    protected BigInteger requestPriority;
    @XmlElement(name = "resent_indicator")
    protected Boolean resentIndicator;
    @XmlElement(name = "original_request_id")
    protected String originalRequestId;
    @XmlElement(name = "original_request_datetime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar originalRequestDatetime;
    @XmlElement(name = "original_channel_id")
    protected String originalChannelId;
    @XmlElement(name = "original_sender_system_id")
    protected String originalSenderSystemId;
    @XmlElement(name = "original_sender_system_user_id")
    protected String originalSenderSystemUserId;
    @XmlElement(name = "original_sender_system_user_id_type")
    protected String originalSenderSystemUserIdType;
    @XmlElement(name = "original_request_priority")
    protected BigInteger originalRequestPriority;
    @XmlElement(name = "test_indicator")
    protected Boolean testIndicator;
    @XmlElement(name = "client_code", required = true)
    protected String clientCode;
    @XmlElement(name = "virtual_account_number", required = true)
    protected String virtualAccountNumber;
    @XmlElement(name = "transaction_amount", required = true)
    protected BigDecimal transactionAmount;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "payment_mode", required = true)
    @XmlSchemaType(name = "string")
    protected PaymentModeType paymentMode;
    @XmlElement(name = "transaction_id", required = true)
    protected String transactionId;
    @XmlElement(name = "transaction_id_type", required = true)
    @XmlSchemaType(name = "string")
    protected TransactionIdTypeType transactionIdType;
    @XmlElement(name = "transaction_datetime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionDatetime;
    @XmlElement(name = "sender_name", required = true)
    protected String senderName;
    @XmlElement(name = "sender_mobile_communication_number")
    protected String senderMobileCommunicationNumber;
    @XmlElement(name = "sender_account_number", required = true)
    protected String senderAccountNumber;
    @XmlElement(name = "sender_bank_name")
    protected String senderBankName;
    @XmlElement(name = "sender_ifsc_code", required = true)
    protected String senderIfscCode;
    @XmlElement(name = "client_account_name")
    protected String clientAccountName;
    @XmlElement(name = "client_mobile_communication_number")
    protected String clientMobileCommunicationNumber;
    @XmlElement(name = "client_account_number", required = true)
    protected String clientAccountNumber;
    @XmlElement(name = "client_bank_name")
    protected String clientBankName;
    @XmlElement(name = "client_ifsc_code")
    protected String clientIfscCode;
    @XmlElement(name = "narration_1")
    protected String narration1;
    @XmlElement(name = "narration_2")
    protected String narration2;
    @XmlElement(name = "informational_message")
    protected String informationalMessage;
    @XmlElement(name = "external_system_reference_1")
    protected String externalSystemReference1;
    @XmlElement(name = "external_system_reference_2")
    protected String externalSystemReference2;
    @XmlElement(name = "client_remarks")
    protected String clientRemarks;
    @XmlElement(name = "client_data_ext")
    protected ClientDataExtType clientDataExt;

    /**
     * Gets the value of the apiVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Sets the value of the apiVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApiVersion(String value) {
        this.apiVersion = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the requestDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRequestDatetime() {
        return requestDatetime;
    }

    /**
     * Sets the value of the requestDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRequestDatetime(XMLGregorianCalendar value) {
        this.requestDatetime = value;
    }

    /**
     * Gets the value of the parentRequestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentRequestId() {
        return parentRequestId;
    }

    /**
     * Sets the value of the parentRequestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentRequestId(String value) {
        this.parentRequestId = value;
    }

    /**
     * Gets the value of the parentRequestDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getParentRequestDatetime() {
        return parentRequestDatetime;
    }

    /**
     * Sets the value of the parentRequestDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setParentRequestDatetime(XMLGregorianCalendar value) {
        this.parentRequestDatetime = value;
    }

    /**
     * Gets the value of the channelId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Sets the value of the channelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelId(String value) {
        this.channelId = value;
    }

    /**
     * Gets the value of the senderSystemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderSystemId() {
        return senderSystemId;
    }

    /**
     * Sets the value of the senderSystemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderSystemId(String value) {
        this.senderSystemId = value;
    }

    /**
     * Gets the value of the senderSystemUserId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderSystemUserId() {
        return senderSystemUserId;
    }

    /**
     * Sets the value of the senderSystemUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderSystemUserId(String value) {
        this.senderSystemUserId = value;
    }

    /**
     * Gets the value of the senderSystemUserIdType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderSystemUserIdType() {
        return senderSystemUserIdType;
    }

    /**
     * Sets the value of the senderSystemUserIdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderSystemUserIdType(String value) {
        this.senderSystemUserIdType = value;
    }

    /**
     * Gets the value of the requestPriority property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRequestPriority() {
        return requestPriority;
    }

    /**
     * Sets the value of the requestPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRequestPriority(BigInteger value) {
        this.requestPriority = value;
    }

    /**
     * Gets the value of the resentIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResentIndicator() {
        return resentIndicator;
    }

    /**
     * Sets the value of the resentIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResentIndicator(Boolean value) {
        this.resentIndicator = value;
    }

    /**
     * Gets the value of the originalRequestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalRequestId() {
        return originalRequestId;
    }

    /**
     * Sets the value of the originalRequestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalRequestId(String value) {
        this.originalRequestId = value;
    }

    /**
     * Gets the value of the originalRequestDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOriginalRequestDatetime() {
        return originalRequestDatetime;
    }

    /**
     * Sets the value of the originalRequestDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOriginalRequestDatetime(XMLGregorianCalendar value) {
        this.originalRequestDatetime = value;
    }

    /**
     * Gets the value of the originalChannelId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalChannelId() {
        return originalChannelId;
    }

    /**
     * Sets the value of the originalChannelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalChannelId(String value) {
        this.originalChannelId = value;
    }

    /**
     * Gets the value of the originalSenderSystemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalSenderSystemId() {
        return originalSenderSystemId;
    }

    /**
     * Sets the value of the originalSenderSystemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalSenderSystemId(String value) {
        this.originalSenderSystemId = value;
    }

    /**
     * Gets the value of the originalSenderSystemUserId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalSenderSystemUserId() {
        return originalSenderSystemUserId;
    }

    /**
     * Sets the value of the originalSenderSystemUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalSenderSystemUserId(String value) {
        this.originalSenderSystemUserId = value;
    }

    /**
     * Gets the value of the originalSenderSystemUserIdType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalSenderSystemUserIdType() {
        return originalSenderSystemUserIdType;
    }

    /**
     * Sets the value of the originalSenderSystemUserIdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalSenderSystemUserIdType(String value) {
        this.originalSenderSystemUserIdType = value;
    }

    /**
     * Gets the value of the originalRequestPriority property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOriginalRequestPriority() {
        return originalRequestPriority;
    }

    /**
     * Sets the value of the originalRequestPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOriginalRequestPriority(BigInteger value) {
        this.originalRequestPriority = value;
    }

    /**
     * Gets the value of the testIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTestIndicator() {
        return testIndicator;
    }

    /**
     * Sets the value of the testIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTestIndicator(Boolean value) {
        this.testIndicator = value;
    }

    /**
     * Gets the value of the clientCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * Sets the value of the clientCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientCode(String value) {
        this.clientCode = value;
    }

    /**
     * Gets the value of the virtualAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualAccountNumber() {
        return virtualAccountNumber;
    }

    /**
     * Sets the value of the virtualAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualAccountNumber(String value) {
        this.virtualAccountNumber = value;
    }

    /**
     * Gets the value of the transactionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the transactionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTransactionAmount(BigDecimal value) {
        this.transactionAmount = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the paymentMode property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentModeType }
     *     
     */
    public PaymentModeType getPaymentMode() {
        return paymentMode;
    }

    /**
     * Sets the value of the paymentMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentModeType }
     *     
     */
    public void setPaymentMode(PaymentModeType value) {
        this.paymentMode = value;
    }

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the transactionIdType property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionIdTypeType }
     *     
     */
    public TransactionIdTypeType getTransactionIdType() {
        return transactionIdType;
    }

    /**
     * Sets the value of the transactionIdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionIdTypeType }
     *     
     */
    public void setTransactionIdType(TransactionIdTypeType value) {
        this.transactionIdType = value;
    }

    /**
     * Gets the value of the transactionDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTransactionDatetime() {
        return transactionDatetime;
    }

    /**
     * Sets the value of the transactionDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTransactionDatetime(XMLGregorianCalendar value) {
        this.transactionDatetime = value;
    }

    /**
     * Gets the value of the senderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Sets the value of the senderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderName(String value) {
        this.senderName = value;
    }

    /**
     * Gets the value of the senderMobileCommunicationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderMobileCommunicationNumber() {
        return senderMobileCommunicationNumber;
    }

    /**
     * Sets the value of the senderMobileCommunicationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderMobileCommunicationNumber(String value) {
        this.senderMobileCommunicationNumber = value;
    }

    /**
     * Gets the value of the senderAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    /**
     * Sets the value of the senderAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderAccountNumber(String value) {
        this.senderAccountNumber = value;
    }

    /**
     * Gets the value of the senderBankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderBankName() {
        return senderBankName;
    }

    /**
     * Sets the value of the senderBankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderBankName(String value) {
        this.senderBankName = value;
    }

    /**
     * Gets the value of the senderIfscCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderIfscCode() {
        return senderIfscCode;
    }

    /**
     * Sets the value of the senderIfscCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderIfscCode(String value) {
        this.senderIfscCode = value;
    }

    /**
     * Gets the value of the clientAccountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientAccountName() {
        return clientAccountName;
    }

    /**
     * Sets the value of the clientAccountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientAccountName(String value) {
        this.clientAccountName = value;
    }

    /**
     * Gets the value of the clientMobileCommunicationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientMobileCommunicationNumber() {
        return clientMobileCommunicationNumber;
    }

    /**
     * Sets the value of the clientMobileCommunicationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientMobileCommunicationNumber(String value) {
        this.clientMobileCommunicationNumber = value;
    }

    /**
     * Gets the value of the clientAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientAccountNumber() {
        return clientAccountNumber;
    }

    /**
     * Sets the value of the clientAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientAccountNumber(String value) {
        this.clientAccountNumber = value;
    }

    /**
     * Gets the value of the clientBankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientBankName() {
        return clientBankName;
    }

    /**
     * Sets the value of the clientBankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientBankName(String value) {
        this.clientBankName = value;
    }

    /**
     * Gets the value of the clientIfscCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientIfscCode() {
        return clientIfscCode;
    }

    /**
     * Sets the value of the clientIfscCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientIfscCode(String value) {
        this.clientIfscCode = value;
    }

    /**
     * Gets the value of the narration1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarration1() {
        return narration1;
    }

    /**
     * Sets the value of the narration1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarration1(String value) {
        this.narration1 = value;
    }

    /**
     * Gets the value of the narration2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarration2() {
        return narration2;
    }

    /**
     * Sets the value of the narration2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarration2(String value) {
        this.narration2 = value;
    }

    /**
     * Gets the value of the informationalMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInformationalMessage() {
        return informationalMessage;
    }

    /**
     * Sets the value of the informationalMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInformationalMessage(String value) {
        this.informationalMessage = value;
    }

    /**
     * Gets the value of the externalSystemReference1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalSystemReference1() {
        return externalSystemReference1;
    }

    /**
     * Sets the value of the externalSystemReference1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalSystemReference1(String value) {
        this.externalSystemReference1 = value;
    }

    /**
     * Gets the value of the externalSystemReference2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalSystemReference2() {
        return externalSystemReference2;
    }

    /**
     * Sets the value of the externalSystemReference2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalSystemReference2(String value) {
        this.externalSystemReference2 = value;
    }

    /**
     * Gets the value of the clientRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientRemarks() {
        return clientRemarks;
    }

    /**
     * Sets the value of the clientRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientRemarks(String value) {
        this.clientRemarks = value;
    }

    /**
     * Gets the value of the clientDataExt property.
     * 
     * @return
     *     possible object is
     *     {@link ClientDataExtType }
     *     
     */
    public ClientDataExtType getClientDataExt() {
        return clientDataExt;
    }

    /**
     * Sets the value of the clientDataExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClientDataExtType }
     *     
     */
    public void setClientDataExt(ClientDataExtType value) {
        this.clientDataExt = value;
    }

}
