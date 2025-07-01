/* global bootstrap */

function clearFilter() {
	window.location = moduleUrl;
}

function showDeleteConfirmationModal(link, entityName) {

	entityId = link.attr('entityId');
	
	document.getElementById('yesBtn').href = link.attr('href');
	document.getElementById('confirmText').textContent = 'Are you sure you want to delete this ' + entityName + ' ID: ' + entityId + "?";
	const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
	confirmModal.show();

}