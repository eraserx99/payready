package payready.aim;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.aim.Result;
import net.authorize.TransactionType;
import net.authorize.aim.Transaction;
import net.authorize.data.Customer;
import net.authorize.data.EmailReceipt;
import net.authorize.data.Order;
import net.authorize.data.OrderItem;
import net.authorize.data.ShippingAddress;
import net.authorize.data.ShippingCharges;
import net.authorize.data.creditcard.CreditCard;

import java.math.BigDecimal;

/**
 * Authorize.net example using their anet-java-sdk directly.
 */
public class AIMClient extends UnitTestData {
	private String authCode;
	private String transactionId;

	private Customer customer;
	private CreditCard creditCard;
	private Order order;
	private OrderItem orderItem;
	private ShippingAddress shippingAddress;
	private ShippingCharges shippingCharges;
	private EmailReceipt emailReceipt;
	private String splitTenderId = null;

	public AIMClient() {
		// create customer
		customer = Customer.createCustomer();
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setAddress(address);
		customer.setCity(city);
		customer.setState(state);
		customer.setZipPostalCode(zipPostalCode);
		customer.setCompany(company);
		customer.setCountry(country);
		customer.setCustomerId(customerId);
		customer.setCustomerIP(customerIP);
		customer.setEmail(email);
		customer.setFax(fax);
		customer.setPhone(phone);

		// create credit card
		creditCard = CreditCard.createCreditCard();
		creditCard.setCreditCardNumber(creditCardNumber);
		creditCard.setExpirationMonth(creditCardExpMonth);
		creditCard.setExpirationYear(creditCardExpYear);

		// create order
		order = Order.createOrder();
		order.setDescription(orderDescription);
		order.setInvoiceNumber(invoiceNumber);

		// create order item
		orderItem = OrderItem.createOrderItem();
		orderItem.setItemDescription(itemDescription);
		orderItem.setItemId(itemId);
		orderItem.setItemName(itemName);
		orderItem.setItemPrice(itemPrice);
		orderItem.setItemQuantity(itemQuantity);
		orderItem.setItemTaxable(true);
		order.addOrderItem(orderItem);

		orderItem = OrderItem.createOrderItem();
		orderItem.setItemDescription(itemDescription2);
		orderItem.setItemId(itemId2);
		orderItem.setItemName(itemName2);
		orderItem.setItemPrice(itemPrice2);
		orderItem.setItemQuantity(itemQuantity2);
		orderItem.setItemTaxable(false);
		order.addOrderItem(orderItem);

		/*
		 * add a bunch of line items (notice that we're trying to add 29 more
		 * which is over the limit of 30. addOrderItem knows the max length and
		 * will only allow 30 in
		 */
		for (int i = 0; i <= 29; i++) {
			order.addOrderItem(orderItem);
		}

		// shipping address
		shippingAddress = ShippingAddress.createShippingAddress();
		shippingAddress.setAddress(address);
		shippingAddress.setCity(city);
		shippingAddress.setCompany(company);
		shippingAddress.setCountry(country);
		shippingAddress.setFirstName(firstName);
		shippingAddress.setLastName(lastName);
		shippingAddress.setState(state);
		shippingAddress.setZipPostalCode(zipPostalCode);

		// shipping charges
		shippingCharges = ShippingCharges.createShippingCharges();
		shippingCharges.setDutyAmount(dutyAmount);
		shippingCharges.setDutyItemDescription(dutyItemDescription);
		shippingCharges.setDutyItemName(dutyItemName);
		shippingCharges.setFreightAmount(freightAmount);
		shippingCharges.setFreightDescription(freightDescription);
		shippingCharges.setFreightItemName(freightItemName);
		shippingCharges.setPurchaseOrderNumber(purchaseOrderNumber);
		shippingCharges.setTaxAmount(taxAmount);
		shippingCharges.setTaxDescription(taxDescription);
		shippingCharges.setTaxExempt(taxExempt);
		shippingCharges.setTaxItemName(taxItemName);

		// email receipt
		emailReceipt = EmailReceipt.createEmailReceipt();
		emailReceipt.setEmail(email);
		emailReceipt.setEmailCustomer(true);
		emailReceipt.setFooterEmailReceipt(footerEmailReceipt);
		emailReceipt.setHeaderEmailReceipt(headerEmailReceipt);
		emailReceipt.setMerchantEmail(merchantEmail);
	}

	public void authCapture() {
		// create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(
				TransactionType.AUTH_ONLY, totalAmount);
		authCaptureTransaction.setCustomer(customer);
		authCaptureTransaction.setOrder(order);
		authCaptureTransaction.setCreditCard(creditCard);
		authCaptureTransaction.setShippingAddress(shippingAddress);
		authCaptureTransaction.setShippingCharges(shippingCharges);
		authCaptureTransaction.setEmailReceipt(emailReceipt);
		authCaptureTransaction.setMerchantDefinedField(mdfKey, mdfValue);

		Result<Transaction> result = (Result<Transaction>) merchant
				.postTransaction(authCaptureTransaction);

		System.out.println(result.getTarget().getTransactionId());
		System.out.println("responseCode = " + result.getResponseCode());
		System.out.println("responseText = " + result.getResponseText());
		System.out.println("reasonResponseCoe = "
				+ result.getReasonResponseCode());
	}

	public static void main(String[] args) {
		AIMClient client = new AIMClient();
		client.authCapture();
	}
}
