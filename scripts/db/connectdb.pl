#!/usr/bin/perl
use strict;
use warnings;
# let the Perl interpreter know that we intend to use the Perl DBI
# need to have installed following modules: perl-dbi and perl-dbd-mysql
use DBI;

# The database config
my %config;
$config{host} = "localhost";
$config{port} = 3306; # 3306 local, 443 external
$config{database}  = "racing_2d";
$config{user} = "<MySQL-login>";
$config{password} = "<MySQL-password>";

# Connect to MySQL DB
my $dbh = DBI->connect("DBI:mysql:database=" . $config{database} . ";host=" . $config{host} . ";port=" . $config{port}, $config{user}, $config{password} ) || die( $DBI::errstr . "\n" );

print "connected to " . $config{database} . " mysql db\n";

# Execute a select query
my $sql = "select * from users;";
my $sth = $dbh->prepare($sql);
$sth->execute || die "SQL Error: $DBI::errstr\n";

while (my @row = $sth->fetchrow_array) {
	print "$row[0]\n";
	print "$row[1]\n";
	print "$row[2]\n";
	print "$row[3]\n";
	print "$row[4]\n";
	print "$row[5]\n";
	print "\n";
}

# Close connection to the MySQL DB
$dbh->disconnect() || die "Disconnect failed: $DBI::errstr\n";
