$(document).ready(
		function() {
			// Validate
			// http://bassistance.de/jquery-plugins/jquery-plugin-validation/
			// http://docs.jquery.com/Plugins/Validation/
			// http://docs.jquery.com/Plugins/Validation/validate#toptions
			$('#update-user-form').validate(
					{
						rules : {
							fullName : {
								minlength : 2,
								required : true
							},
							email : {
								required : true,
								email : true
							},
							password : {
								minlength : 2,
								required : true
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

		}); // end document.ready
