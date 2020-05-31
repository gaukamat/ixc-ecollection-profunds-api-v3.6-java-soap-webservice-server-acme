
package com.icici.xpress_connect.ws.ecollection;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.icici.xpress_connect.ws.ecollection package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ApiVersion_QNAME = new QName("http://www.icicibank.com/api/acme", "api_version");
    private final static QName _RequestId_QNAME = new QName("http://www.icicibank.com/api/acme", "request_id");
    private final static QName _RequestDatetime_QNAME = new QName("http://www.icicibank.com/api/acme", "request_datetime");
    private final static QName _RequestPriority_QNAME = new QName("http://www.icicibank.com/api/acme", "request_priority");
    private final static QName _ParentRequestId_QNAME = new QName("http://www.icicibank.com/api/acme", "parent_request_id");
    private final static QName _ParentRequestDatetime_QNAME = new QName("http://www.icicibank.com/api/acme", "parent_request_datetime");
    private final static QName _SenderSystemUserId_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_system_user_id");
    private final static QName _SenderSystemUserIdType_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_system_user_id_type");
    private final static QName _ResentIndicator_QNAME = new QName("http://www.icicibank.com/api/acme", "resent_indicator");
    private final static QName _OriginalRequestId_QNAME = new QName("http://www.icicibank.com/api/acme", "original_request_id");
    private final static QName _OriginalRequestDatetime_QNAME = new QName("http://www.icicibank.com/api/acme", "original_request_datetime");
    private final static QName _OriginalChannelId_QNAME = new QName("http://www.icicibank.com/api/acme", "original_channel_id");
    private final static QName _OriginalSenderSystemId_QNAME = new QName("http://www.icicibank.com/api/acme", "original_sender_system_id");
    private final static QName _OriginalRequestPriority_QNAME = new QName("http://www.icicibank.com/api/acme", "original_request_priority");
    private final static QName _OriginalSenderSystemUserId_QNAME = new QName("http://www.icicibank.com/api/acme", "original_sender_system_user_id");
    private final static QName _OriginalSenderSystemUserIdType_QNAME = new QName("http://www.icicibank.com/api/acme", "original_sender_system_user_id_type");
    private final static QName _SenderSystemId_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_system_id");
    private final static QName _ChannelId_QNAME = new QName("http://www.icicibank.com/api/acme", "channel_id");
    private final static QName _TestIndicator_QNAME = new QName("http://www.icicibank.com/api/acme", "test_indicator");
    private final static QName _ClientCode_QNAME = new QName("http://www.icicibank.com/api/acme", "client_code");
    private final static QName _VirtualAccountNumber_QNAME = new QName("http://www.icicibank.com/api/acme", "virtual_account_number");
    private final static QName _TransactionId_QNAME = new QName("http://www.icicibank.com/api/acme", "transaction_id");
    private final static QName _TransactionIdType_QNAME = new QName("http://www.icicibank.com/api/acme", "transaction_id_type");
    private final static QName _TransactionDatetime_QNAME = new QName("http://www.icicibank.com/api/acme", "transaction_datetime");
    private final static QName _TransactionAmount_QNAME = new QName("http://www.icicibank.com/api/acme", "transaction_amount");
    private final static QName _CurrencyCode_QNAME = new QName("http://www.icicibank.com/api/acme", "currency_code");
    private final static QName _PaymentMode_QNAME = new QName("http://www.icicibank.com/api/acme", "payment_mode");
    private final static QName _SenderName_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_name");
    private final static QName _SenderMobileCommunicationNumber_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_mobile_communication_number");
    private final static QName _SenderAccountNumber_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_account_number");
    private final static QName _SenderBankName_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_bank_name");
    private final static QName _SenderIfscCode_QNAME = new QName("http://www.icicibank.com/api/acme", "sender_ifsc_code");
    private final static QName _ClientAccountName_QNAME = new QName("http://www.icicibank.com/api/acme", "client_account_name");
    private final static QName _ClientMobileCommunicationNumber_QNAME = new QName("http://www.icicibank.com/api/acme", "client_mobile_communication_number");
    private final static QName _ClientAccountNumber_QNAME = new QName("http://www.icicibank.com/api/acme", "client_account_number");
    private final static QName _ClientBankName_QNAME = new QName("http://www.icicibank.com/api/acme", "client_bank_name");
    private final static QName _ClientIfscCode_QNAME = new QName("http://www.icicibank.com/api/acme", "client_ifsc_code");
    private final static QName _Narration1_QNAME = new QName("http://www.icicibank.com/api/acme", "narration_1");
    private final static QName _Narration2_QNAME = new QName("http://www.icicibank.com/api/acme", "narration_2");
    private final static QName _InformationalMessage_QNAME = new QName("http://www.icicibank.com/api/acme", "informational_message");
    private final static QName _ExternalSystemReference1_QNAME = new QName("http://www.icicibank.com/api/acme", "external_system_reference_1");
    private final static QName _ExternalSystemReference2_QNAME = new QName("http://www.icicibank.com/api/acme", "external_system_reference_2");
    private final static QName _ClientRemarks_QNAME = new QName("http://www.icicibank.com/api/acme", "client_remarks");
    private final static QName _ClientDataExt_QNAME = new QName("http://www.icicibank.com/api/acme", "client_data_ext");
    private final static QName _ResponseId_QNAME = new QName("http://www.icicibank.com/api/acme", "response_id");
    private final static QName _ResponseDatetime_QNAME = new QName("http://www.icicibank.com/api/acme", "response_datetime");
    private final static QName _CompletionCode_QNAME = new QName("http://www.icicibank.com/api/acme", "completion_code");
    private final static QName _ReasonCode_QNAME = new QName("http://www.icicibank.com/api/acme", "reason_code");
    private final static QName _Message_QNAME = new QName("http://www.icicibank.com/api/acme", "message");
    private final static QName _RetryIndicator_QNAME = new QName("http://www.icicibank.com/api/acme", "retry_indicator");
    private final static QName _RetryAfterInterval_QNAME = new QName("http://www.icicibank.com/api/acme", "retry_after_interval");
    private final static QName _RetryAfterDatetime_QNAME = new QName("http://www.icicibank.com/api/acme", "retry_after_datetime");
    private final static QName _IntimationRequest_QNAME = new QName("http://www.icicibank.com/api/acme", "intimation_request");
    private final static QName _IntimationResponse_QNAME = new QName("http://www.icicibank.com/api/acme", "intimation_response");
    private final static QName _RemitterAccountNumber_QNAME = new QName("http://www.icicibank.com/api/acme", "remitter_account_number");
    private final static QName _CmsReferenceNumber_QNAME = new QName("http://www.icicibank.com/api/acme", "cms_reference_number");
    private final static QName _RefundPaymentStatus_QNAME = new QName("http://www.icicibank.com/api/acme", "refund_payment_status");
    private final static QName _RefundCode_QNAME = new QName("http://www.icicibank.com/api/acme", "refund_code");
    private final static QName _RefundRejectionCode_QNAME = new QName("http://www.icicibank.com/api/acme", "refund_rejection_code");
    private final static QName _RefundIntimationRequest_QNAME = new QName("http://www.icicibank.com/api/acme", "refund_intimation_request");
    private final static QName _RefundIntimationResponse_QNAME = new QName("http://www.icicibank.com/api/acme", "refund_intimation_response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.icici.xpress_connect.ws.ecollection
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClientDataExtType }
     * 
     */
    public ClientDataExtType createClientDataExtType() {
        return new ClientDataExtType();
    }

    /**
     * Create an instance of {@link IntimationRequestType }
     * 
     */
    public IntimationRequestType createIntimationRequestType() {
        return new IntimationRequestType();
    }

    /**
     * Create an instance of {@link IntimationResponseType }
     * 
     */
    public IntimationResponseType createIntimationResponseType() {
        return new IntimationResponseType();
    }

    /**
     * Create an instance of {@link RefundIntimationRequestType }
     * 
     */
    public RefundIntimationRequestType createRefundIntimationRequestType() {
        return new RefundIntimationRequestType();
    }

    /**
     * Create an instance of {@link RefundIntimationResponseType }
     * 
     */
    public RefundIntimationResponseType createRefundIntimationResponseType() {
        return new RefundIntimationResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "api_version")
    public JAXBElement<String> createApiVersion(String value) {
        return new JAXBElement<String>(_ApiVersion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "request_id")
    public JAXBElement<String> createRequestId(String value) {
        return new JAXBElement<String>(_RequestId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "request_datetime")
    public JAXBElement<XMLGregorianCalendar> createRequestDatetime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RequestDatetime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "request_priority")
    public JAXBElement<BigInteger> createRequestPriority(BigInteger value) {
        return new JAXBElement<BigInteger>(_RequestPriority_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "parent_request_id")
    public JAXBElement<String> createParentRequestId(String value) {
        return new JAXBElement<String>(_ParentRequestId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "parent_request_datetime")
    public JAXBElement<XMLGregorianCalendar> createParentRequestDatetime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ParentRequestDatetime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_system_user_id")
    public JAXBElement<String> createSenderSystemUserId(String value) {
        return new JAXBElement<String>(_SenderSystemUserId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_system_user_id_type")
    public JAXBElement<String> createSenderSystemUserIdType(String value) {
        return new JAXBElement<String>(_SenderSystemUserIdType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "resent_indicator")
    public JAXBElement<Boolean> createResentIndicator(Boolean value) {
        return new JAXBElement<Boolean>(_ResentIndicator_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "original_request_id")
    public JAXBElement<String> createOriginalRequestId(String value) {
        return new JAXBElement<String>(_OriginalRequestId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "original_request_datetime")
    public JAXBElement<XMLGregorianCalendar> createOriginalRequestDatetime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_OriginalRequestDatetime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "original_channel_id")
    public JAXBElement<String> createOriginalChannelId(String value) {
        return new JAXBElement<String>(_OriginalChannelId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "original_sender_system_id")
    public JAXBElement<String> createOriginalSenderSystemId(String value) {
        return new JAXBElement<String>(_OriginalSenderSystemId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "original_request_priority")
    public JAXBElement<BigInteger> createOriginalRequestPriority(BigInteger value) {
        return new JAXBElement<BigInteger>(_OriginalRequestPriority_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "original_sender_system_user_id")
    public JAXBElement<String> createOriginalSenderSystemUserId(String value) {
        return new JAXBElement<String>(_OriginalSenderSystemUserId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "original_sender_system_user_id_type")
    public JAXBElement<String> createOriginalSenderSystemUserIdType(String value) {
        return new JAXBElement<String>(_OriginalSenderSystemUserIdType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_system_id")
    public JAXBElement<String> createSenderSystemId(String value) {
        return new JAXBElement<String>(_SenderSystemId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "channel_id")
    public JAXBElement<String> createChannelId(String value) {
        return new JAXBElement<String>(_ChannelId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "test_indicator")
    public JAXBElement<Boolean> createTestIndicator(Boolean value) {
        return new JAXBElement<Boolean>(_TestIndicator_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_code")
    public JAXBElement<String> createClientCode(String value) {
        return new JAXBElement<String>(_ClientCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "virtual_account_number")
    public JAXBElement<String> createVirtualAccountNumber(String value) {
        return new JAXBElement<String>(_VirtualAccountNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "transaction_id")
    public JAXBElement<String> createTransactionId(String value) {
        return new JAXBElement<String>(_TransactionId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionIdTypeType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransactionIdTypeType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "transaction_id_type")
    public JAXBElement<TransactionIdTypeType> createTransactionIdType(TransactionIdTypeType value) {
        return new JAXBElement<TransactionIdTypeType>(_TransactionIdType_QNAME, TransactionIdTypeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "transaction_datetime")
    public JAXBElement<XMLGregorianCalendar> createTransactionDatetime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TransactionDatetime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "transaction_amount")
    public JAXBElement<BigDecimal> createTransactionAmount(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_TransactionAmount_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "currency_code")
    public JAXBElement<String> createCurrencyCode(String value) {
        return new JAXBElement<String>(_CurrencyCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentModeType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PaymentModeType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "payment_mode")
    public JAXBElement<PaymentModeType> createPaymentMode(PaymentModeType value) {
        return new JAXBElement<PaymentModeType>(_PaymentMode_QNAME, PaymentModeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_name")
    public JAXBElement<String> createSenderName(String value) {
        return new JAXBElement<String>(_SenderName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_mobile_communication_number")
    public JAXBElement<String> createSenderMobileCommunicationNumber(String value) {
        return new JAXBElement<String>(_SenderMobileCommunicationNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_account_number")
    public JAXBElement<String> createSenderAccountNumber(String value) {
        return new JAXBElement<String>(_SenderAccountNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_bank_name")
    public JAXBElement<String> createSenderBankName(String value) {
        return new JAXBElement<String>(_SenderBankName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "sender_ifsc_code")
    public JAXBElement<String> createSenderIfscCode(String value) {
        return new JAXBElement<String>(_SenderIfscCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_account_name")
    public JAXBElement<String> createClientAccountName(String value) {
        return new JAXBElement<String>(_ClientAccountName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_mobile_communication_number")
    public JAXBElement<String> createClientMobileCommunicationNumber(String value) {
        return new JAXBElement<String>(_ClientMobileCommunicationNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_account_number")
    public JAXBElement<String> createClientAccountNumber(String value) {
        return new JAXBElement<String>(_ClientAccountNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_bank_name")
    public JAXBElement<String> createClientBankName(String value) {
        return new JAXBElement<String>(_ClientBankName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_ifsc_code")
    public JAXBElement<String> createClientIfscCode(String value) {
        return new JAXBElement<String>(_ClientIfscCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "narration_1")
    public JAXBElement<String> createNarration1(String value) {
        return new JAXBElement<String>(_Narration1_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "narration_2")
    public JAXBElement<String> createNarration2(String value) {
        return new JAXBElement<String>(_Narration2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "informational_message")
    public JAXBElement<String> createInformationalMessage(String value) {
        return new JAXBElement<String>(_InformationalMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "external_system_reference_1")
    public JAXBElement<String> createExternalSystemReference1(String value) {
        return new JAXBElement<String>(_ExternalSystemReference1_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "external_system_reference_2")
    public JAXBElement<String> createExternalSystemReference2(String value) {
        return new JAXBElement<String>(_ExternalSystemReference2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_remarks")
    public JAXBElement<String> createClientRemarks(String value) {
        return new JAXBElement<String>(_ClientRemarks_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientDataExtType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ClientDataExtType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "client_data_ext")
    public JAXBElement<ClientDataExtType> createClientDataExt(ClientDataExtType value) {
        return new JAXBElement<ClientDataExtType>(_ClientDataExt_QNAME, ClientDataExtType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "response_id")
    public JAXBElement<String> createResponseId(String value) {
        return new JAXBElement<String>(_ResponseId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "response_datetime")
    public JAXBElement<XMLGregorianCalendar> createResponseDatetime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ResponseDatetime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "completion_code")
    public JAXBElement<BigInteger> createCompletionCode(BigInteger value) {
        return new JAXBElement<BigInteger>(_CompletionCode_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "reason_code")
    public JAXBElement<String> createReasonCode(String value) {
        return new JAXBElement<String>(_ReasonCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "message")
    public JAXBElement<String> createMessage(String value) {
        return new JAXBElement<String>(_Message_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "retry_indicator")
    public JAXBElement<Boolean> createRetryIndicator(Boolean value) {
        return new JAXBElement<Boolean>(_RetryIndicator_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "retry_after_interval")
    public JAXBElement<BigInteger> createRetryAfterInterval(BigInteger value) {
        return new JAXBElement<BigInteger>(_RetryAfterInterval_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "retry_after_datetime")
    public JAXBElement<XMLGregorianCalendar> createRetryAfterDatetime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RetryAfterDatetime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntimationRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IntimationRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "intimation_request")
    public JAXBElement<IntimationRequestType> createIntimationRequest(IntimationRequestType value) {
        return new JAXBElement<IntimationRequestType>(_IntimationRequest_QNAME, IntimationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntimationResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IntimationResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "intimation_response")
    public JAXBElement<IntimationResponseType> createIntimationResponse(IntimationResponseType value) {
        return new JAXBElement<IntimationResponseType>(_IntimationResponse_QNAME, IntimationResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "remitter_account_number")
    public JAXBElement<String> createRemitterAccountNumber(String value) {
        return new JAXBElement<String>(_RemitterAccountNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "cms_reference_number")
    public JAXBElement<String> createCmsReferenceNumber(String value) {
        return new JAXBElement<String>(_CmsReferenceNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "refund_payment_status")
    public JAXBElement<String> createRefundPaymentStatus(String value) {
        return new JAXBElement<String>(_RefundPaymentStatus_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "refund_code")
    public JAXBElement<String> createRefundCode(String value) {
        return new JAXBElement<String>(_RefundCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "refund_rejection_code")
    public JAXBElement<String> createRefundRejectionCode(String value) {
        return new JAXBElement<String>(_RefundRejectionCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundIntimationRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RefundIntimationRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "refund_intimation_request")
    public JAXBElement<RefundIntimationRequestType> createRefundIntimationRequest(RefundIntimationRequestType value) {
        return new JAXBElement<RefundIntimationRequestType>(_RefundIntimationRequest_QNAME, RefundIntimationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundIntimationResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RefundIntimationResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.icicibank.com/api/acme", name = "refund_intimation_response")
    public JAXBElement<RefundIntimationResponseType> createRefundIntimationResponse(RefundIntimationResponseType value) {
        return new JAXBElement<RefundIntimationResponseType>(_RefundIntimationResponse_QNAME, RefundIntimationResponseType.class, null, value);
    }

}
