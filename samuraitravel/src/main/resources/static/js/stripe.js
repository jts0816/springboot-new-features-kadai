const stripe = Stripe('pk_test_51OWwTwAOyfuRks17YejFYXjvJoe8xfVn0oFR5GTI3dagmlZ1FYUdb5jb2Njk9nUdxlkTuZJbpT039HdqBJMoI9v800Egw07CXX');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});