$.validator.addMethod("FutureDate", function(value, element) {
	return Date.parse(value) > new Date();
}, "Date must be in the future.");

$(document).ready(
		function() {
			// Validate
			// http://bassistance.de/jquery-plugins/jquery-plugin-validation/
			// http://docs.jquery.com/Plugins/Validation/
			// http://docs.jquery.com/Plugins/Validation/validate#toptions
			$('#tournament-form').validate(
					{
						rules : {
							name : {
								minlength : 2,
								required : true
							},
							datepicker : {
								required : true,
								FutureDate : true,
								date : true
							},
							max_players : {
								required : true,
								number : true,
								min : 3
							}
						},
						highlight : function(element) {
							$(element).closest('.control-group').removeClass(
									'success').addClass('error');
						},
						success : function(element) {
							element.text('OK!').addClass('valid').closest(
									'.control-group').removeClass('error')
									.addClass('success');
						}
					});
		});
