package preparedstatements;


import org.apache.log4j.Level;
import org.kohsuke.args4j.Option;

import preparedstatements.logging.Log4JLevelOptionHandler;

public class CommandLineOptions {

	@Option(name = "-j", aliases = { "--jdbc-url" }, required = true, usage = "The JDBC database URL to use, format: jdbc:<subprotokoll>:<subname> (e.g., jdbc: mysql://localhost/dbname)")
	public String databaseJdbcUrl = null;

	@Option(name = "-u", aliases = { "--username" }, required = true, usage = "The JDBC database username to use.")
	public String databaseUsername = null;

	@Option(name = "-p", aliases = { "--password" }, required = true, usage = "The JDBC database password to use.")
	public String databasePassword = null;

	@Option(name = "-l", aliases = { "--logLevel" }, usage = "Set logging level (valid values: TRACE, DEBUG, INFO, WARN, ERROR).", required = false, handler = Log4JLevelOptionHandler.class)
	public Level logLevel = null;

	@Option(name = "-v", aliases = { "--verbose" }, usage = "Verbose (DEBUG) logging output (default: INFO).", required = false)
	public boolean verbose = false;

	@Option(name = "-h", aliases = { "--help" }, usage = "This help message.", required = false)
	public boolean help = false;

}
