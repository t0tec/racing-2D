#!/usr/bin/perl
use strict;
use warnings;
use HTML::Entities;
use LWP::UserAgent;
# ################################################!!!!################################################
# NOTE: this script will only load 949 records max...
# ################################################!!!!################################################

require "add_user_service.pl";

my $n = 500;
my $url = "http://www.xtra-rant.com/gennames/gennames2.php?fnt=1&f=uscensus1990_male.txt&sf=&lnt=1&l=lastnames.txt&sl=&n=$n";
my @usernames;
my @emails;
my @fullNames;

print "Sending GET request to $url\n";
my $ua = LWP::UserAgent->new();
my $req = new HTTP::Request GET => $url;
my $res = $ua->request($req);
my $content = $res->content;
print "Page loaded\n";

$content = decode_entities($content);	

print "Page decoded\n";

foreach (split ('</A><BR>', $content)) {
	if (/\s(.*)\>(.*)\s<\/A>.(.*)\s(.*)>(.*)/) {
		my $fullName = "$2 $5"; 
		push(@fullNames, $fullName); 
		my $email = "${2}.${5}\@racing11.com";
		push(@emails, $email);
		my $part1 = substr $2, 0, 5;
		my $part2 = substr $5, 0, 6;
		my $username = "${part1}${part2}";
		push(@usernames, $username);
	}
}

my $length = scalar(@fullNames);
print "${length} lines loaded\n";

for (my $i = 0; $i < $length ; $i++) {
		my $username = $usernames[$i];
		my $password = $usernames[$i];
		my $email = $emails[$i];
		my $fullName = $fullNames[$i];
		# Uncomment following line if you want to debug the script
		#print "${username}\t${password}\t${email}\t${fullName}\n";
		
		# Call the subroutine defined in add_user_service.pl to post to the service
		my $result = &addUser($username, $password, $email, $fullName);

		if($result) {
		 	print "Successfully added $username\n";	
		} else {		
			print "Failed to add $username\n";
		}
		# sleep for 0.1 second if you don't want to crash the script
		sleep(0.1); 
}

exit 0;

