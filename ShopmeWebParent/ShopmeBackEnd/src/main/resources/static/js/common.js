$(document).ready(function () {

	// Add active class in the nav-link
	const navLinks = document.querySelectorAll('a.nav-link');
	for (let i = 0; i < navLinks.length; i++) {
		if (window.location.pathname.includes(navLinks[i].getAttribute("href"))) {
			navLinks[i].classList.add('active');
			break;
		}
	}

	$('#logoutLink').on('click', function (e) {
		e.preventDefault();
		document.logoutForm.submit();
	});

	customizeDropdownMenu();
});

function customizeDropdownMenu() {

	$('.navbar .dropdown').hover(
			function () {
				$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
			},
			function () {
				$(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
			}
	);

	$('.dropdown > a').click(function () {
		location.href = this.href;
	});

}
