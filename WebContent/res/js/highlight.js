$(document).ready(function(){
	var url = window.location.href;

	if (url.search('tasks') > 0) {
		$('#linkContainer a#tasks').addClass('selectedLink');
	} 
	if (url.search('login') > 0) {
		$('#linkContainer a#login').addClass('selectedLink');
	} 
	if (url.search('register') > 0) {
		$('#linkContainer a#register').addClass('selectedLink');
	} 
	if (url.search('about') > 0) {
	    $('#linkContainer a#about').addClass('selectedLink');
	} 
});