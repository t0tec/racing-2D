#!/usr/bin/perl
use strict;
use warnings;
# let the Perl interpreter know that we intend to use the Perl DBI
# need to have installed following modules: perl-dbi, perl-dbd-mysql and SQL::SplitStatement
use DBI;
use SQL::SplitStatement;

# The database config
my %config;
$config{host} = "localhost";
$config{port} = 3306;
$config{user} = "<MySQL-user>";
$config{password} = "<MySQL-password>";

# The SQL script filename
my $filename = "db_dump.sql";

print "WARNING!\n\n";
print "RESETTING DATABASE 'racing_2d' (DROPPING TABLES!) AND IMPORTING DUMP DATA FROM '$filename'!\n\n";

# Connect to MySQL Server
my $dbh = DBI->connect("DBI:mysql:host=" . $config{host}, $config{user} , $config{password}) || die( $DBI::errstr . "\n" );

print "Connected succesfully to " . $config{host} . " MySQL server! \n\n";

sub execute_sql_file {
	# Read in ../../db-dump/ the db-dump.sql file
	chdir "..";
	chdir "..";
	chdir "db-dump";
    
    my $sql = do {
		open my $fh, '<', $filename or die "Can't open $filename: $!";
        local $/;
        <$fh>
    };
	
	# Split the SQL statements
	my $sql_splitter = SQL::SplitStatement->new; 
	my @statements = $sql_splitter->split($sql);
	
	# Execute SQL statements
	for my $i (0 .. $#statements) {
		print "Executing statement nr. " . ($i + 1) . "\n";
		$dbh->do($statements[$i]);
	}
}

&execute_sql_file;

print "\nFinished executing statements!\n\n";

print "RESETTING DATABASE COMPLETE!";

# Close connection to the MySQL Server
$dbh->disconnect() || die "Disconnect failed: $DBI::errstr\n";
