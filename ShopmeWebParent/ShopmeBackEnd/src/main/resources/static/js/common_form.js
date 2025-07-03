/* global bootstrap */

$(document).ready(function () {

	$('#buttonCancel').on('click', function () {
		window.location = moduleUrl;
		// window.location = "/ShopmeAdmin/"
	});


	$('#fileImage').change(function () {
		const file = this.files[0];
		fileSize = file.size;

		// alert("File size: " + fileSize);

		if (file) {
			var allowedTypes = ['image/jpeg', 'image/png'];

			if (fileSize > 102400) {

				this.setCustomValidity("You must choose an image less than 100KB!");
				this.reportValidity();
				this.value = '';
			} else if (!allowedTypes.includes(file.type)) {

				this.setCustomValidity('Invalid file type. Please select a JPEG, or PNG image.');
				this.reportValidity();
				// showModalDialog('Warning', 'Invalid file type. Please select a JPEG, or PNG image.');
				this.value = '';
			} else {
				this.setCustomValidity("");
				showImageThumbnail(this);
			}
		}
	});
});

function showImageThumbnail(fileInput) {
	let file = fileInput.files[0];
	let reader = new FileReader();
	reader.onload = function (e) { // Assigns a function to execute when the file has been successfully read
		$("#thumbnail").attr('src', e.target.result); // e.target.result contains the result of the file read operation
	};
	reader.readAsDataURL(file);
}

function showModalDialog(title, message) {

	document.getElementById('modalTitle').textContent = title;
	document.getElementById('modalBody').textContent = message;
	const myModal = new bootstrap.Modal(document.getElementById('modalDialog'));
	myModal.show();

}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}
