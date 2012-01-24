$(document).ready(
		function() {
			$('input').bind('focusin', function() {
				$(this).addClass('selectedInput');
			});

			$('input').bind('focusout', function() {
				$(this).removeClass('selectedInput');
			});

			$('#registrationForm')
					.validate(
							{
								errorClass : "error",
								validClass : "checked",
								highlight : function(element, errorClass,
										validClass) {
									$(element.form).find(
											"label[for=" + element.id + "]")
											.addClass(errorClass).removeClass(
													validClass);
								},
								unhighlight : function(element, errorClass,
										validClass) {
									$(element.form).find(
											"label[for=" + element.id + "]")
											.removeClass(errorClass).addClass(
													validClass).text('ok!');
								},
								errorPlacement : function(error, element) {
									error.appendTo(element.parent().next());
								},
								success : function(label) {
									var parent = label.parent();
									$('label', parent).remove();
									label.html("ok!").addClass("checked");
									parent.append(label);
								},
								submitHandler : function(form) {
									form.submit();
								},
								debug : true,
								rules : {
									name : {
										required : true,
										minlength : 5,
										remote : {
											url : "check-users.htm",
											type : "post",
											data : {
												username : function() {
													return $("input#name")
															.val();
												}
											}
										}
									},
									email : {
										email : true,
										required : true,
										remote : {
											url : "check-email.htm",
											type : "post",
											data : {
												email : function() {
													return $("input#email")
															.val();
												}
											}
										}
									},
									password : {
										minlength : 5,
										required : true
									},
									confirmation : {
										minlength : 5,
										required : true,
										equalTo : "#password"
									}
								},
								messages : {
									name : {
										required : "Required",
										minlength : "Need at least 5 symbols",
										remote : "Username is already in use"
									},
									email : {
										email : "Please enter a valid email",
										required : "Required",
										remote : "Email is already in use"
									},
									password : {
										minlength : "Need at least 5 symbols",
										required : "Required"
									},
									confirmation : {
										minlength : "Need at least 5 symbols",
										required : "Required",
										equalTo : "Not the same password"
									}
								}
							});
		});