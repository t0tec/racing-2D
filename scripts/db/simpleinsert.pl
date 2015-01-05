#!/usr/bin/perl
use strict;
use warnings;
use DBI;

print "Starting databaseconnection\n";

my %config;
$config{host} = "localhost";
$config{port} = 3306;
$config{database}  = "racing_2d";
$config{user} = "<MySQL-login>";
$config{password} = "<MySQL-password>";

my $dbh = DBI->connect("DBI:mysql:database=" . $config{database} . ";host=" . $config{host} . ";port=" . $config{port}, $config{user}, $config{password} ) || die ( $DBI::errstr . "\n" );

print "Preparing statement\n";
my $sth = $dbh->prepare("INSERT INTO users (username, password, email, full_name) VALUES (?, ?, ?, ?)");

my $username = "User123";
my $password = "User123";
my $email = "User123\@racing2d.com";
my $fullName = "User123";

# Uncomment following line if you want to debug the script
#print "${username}\t${password}\t${email}\t${fullName}\n";
$sth->execute ($username, $password, $email, $fullName) || warn "Something went wrong : $DBI::errstr \n";
print "Statements executed\n";

$sth->finish();
print "Statements finished\n";

$dbh->disconnect() || warn "Disconnection failed\n";
