/**
 * Copyright (c) 2012, Dennis Pfisterer, All rights reserved.
 */
package preparedstatements;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.LoggerFactory;

import preparedstatements.logging.LoggingConfiguration;

public class Main {
	static org.slf4j.Logger log;

	static {
		LoggingConfiguration.configureLog4JFromClasspath();
	}

	public static void main(String[] args) throws IOException {
		log = LoggerFactory.getLogger(Main.class);
		final CommandLineOptions options = parseCmdLineOptions(args);
		LoggingConfiguration.setLog4RootLogLevel(options);
		log.debug("Startup");

		try {
			log.debug("Connecting to {} as {}", options.databaseJdbcUrl, options.databaseUsername);

			JdbcAccess mySql = new JdbcAccess(options.databaseJdbcUrl, options.databaseUsername,
					options.databasePassword);

			mySql.connect();

			int repetitions = 100000;
			
			// Normale Queries
			{
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < repetitions; ++i) {
					mySql.executeQueryStatement(
							"SELECT * FROM mitarbeiter WHERE name = '" + System.currentTimeMillis() + "'").close();
				}
				log.info("Standard Statements: {}ms", System.currentTimeMillis() - startTime);
			}

			// Prepared Statements
			{
				PreparedStatement preparedStatement = mySql.getConnection().prepareStatement(
						"SELECT * FROM mitarbeiter WHERE name = ?");
				long startTime2 = System.currentTimeMillis();
				for (int i = 0; i < repetitions; ++i) {
					preparedStatement.setString(1, "" + System.currentTimeMillis());
					preparedStatement.executeQuery().close();
				}

				log.info("Prepared Statements: {}ms", System.currentTimeMillis() - startTime2);
			}

			mySql.disconnect();

		} catch (SQLException ex) {
			log.error("SQLException: " + ex, ex.getMessage());
			log.error("SQLState: " + ex, ex.getSQLState());
			log.error("VendorError: " + ex, ex.getErrorCode());
		}

		log.debug("Shutdown");
	}

	private static CommandLineOptions parseCmdLineOptions(final String[] args) {
		CommandLineOptions options = new CommandLineOptions();
		CmdLineParser parser = new CmdLineParser(options);

		try {
			parser.parseArgument(args);
			if (options.help)
				printHelpAndExit(parser);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printHelpAndExit(parser);
		}

		return options;
	}

	private static void printHelpAndExit(CmdLineParser parser) {
		System.err.print("Usage: java " + Main.class.getCanonicalName());
		parser.printSingleLineUsage(System.err);
		System.err.println();
		parser.printUsage(System.err);
		System.exit(1);
	}

}
