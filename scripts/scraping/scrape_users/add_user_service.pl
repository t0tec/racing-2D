#!/usr/bin/perl
use strict;
use warnings;
# Load LWP
use LWP::UserAgent;

my $development =  "localhost:8080";
my $staging = "db.staging.be:80";
my $release = "db.release.be:80";

# URL for service
my $user_add_url = "http://$development/api/users/register";

sub addUser {
	if(scalar(@_) != 4){
		print "Wrong number of arguments!!!\n";
		return 0;
	}
	
	# Populate POST data fields (key => value pairs)
	my (%post_data) = (
		   'username' => shift(@_),
		   'password' => shift(@_),
		   'email' => shift(@_),
		   'fullname' => shift(@_)
		   );
	
	# Create a user agent
	my $ua = LWP::UserAgent->new;
	
	# Perform the request
	my $response = $ua->post($user_add_url, \%post_data);

	# Check for HTTP error codes
	die 'http status: ' . $response->code . ' ' . $response->message unless ($response->is_success); 
	
	# Output the entry
	#print $response->content();
	
	# Return succesfful response
	return ($response->is_success);	
}

