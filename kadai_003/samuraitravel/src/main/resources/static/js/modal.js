const deleteModal = document.getElementById('deleteModal')
if (deleteModal) {
	deleteModal.addEventListener('show.bs.modal', event => {
		const link = event.relatedTarget
		const recipient = link.getAttribute('data-bs-whatever')
		const reviewIdInput = deleteModal.querySelector('.modal-footer #reviewId')

		reviewIdInput.value = recipient
	})
}