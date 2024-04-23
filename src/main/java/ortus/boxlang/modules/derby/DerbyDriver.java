/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.modules.derby;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.jdbc.drivers.GenericJDBCDriver;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

public class DerbyDriver extends GenericJDBCDriver {

	protected static final String	DEFAULT_PROTOCOL	= "directory";
	protected static final IStruct	AVAILABLE_PROTOCOLS	= Struct.of(
	    "directory", "The database is in a directory",
	    "classpath", "The database is in the classpath",
	    "memory", "The database is in memory",
	    "jar", "The database is in a jar file"
	);

	/**
	 * The protocol in use for the jdbc connection
	 */
	protected String				protocol			= DEFAULT_PROTOCOL;

	/**
	 * Constructor
	 */
	public DerbyDriver() {
		super();
		this.name					= new Key( "Derby" );
		this.type					= DatabaseDriverType.DERBY;
		// org.apache.derby.jdbc.ClientDriver For client connections
		this.driverClassName		= "org.apache.derby.jdbc.EmbeddedDriver";
		this.defaultDelimiter		= ";";
		this.defaultCustomParams	= Struct.of(
		    "create", "true"
		);
		this.defaultProperties		= Struct.of();
	}

	/**
	 * Build the connection URL for the Apache Derby JDBC Driver
	 * <p>
	 *
	 * <pre>
	 * jdbc:derby:[subsubprotocol:][databaseName][;attribute=value]*
	 * </pre>
	 *
	 * @param config The DatasourceConfig object
	 *
	 * @return The connection URL
	 */
	@Override
	public String buildConnectionURL( DatasourceConfig config ) {
		// Validate the database
		String database = ( String ) config.properties.getOrDefault( "database", "" );
		if ( database.isEmpty() ) {
			throw new IllegalArgumentException( "The database property is required for the Apache Derby JDBC Driver" );
		}

		// Do we have a protcol
		this.protocol = ( String ) config.properties.getOrDefault( "protocol", DEFAULT_PROTOCOL );

		// Verify or throw an exception
		if ( !AVAILABLE_PROTOCOLS.containsKey( this.protocol ) ) {
			throw new IllegalArgumentException(
			    String.format(
			        "The protocol '%s' is not valid for the Apache Derby JDBC Driver. Available protocols are %s",
			        this.protocol,
			        AVAILABLE_PROTOCOLS.keySet().toString()
			    )
			);
		}

		// Host Check
		String	host	= ( String ) config.properties.getOrDefault( "host", "" );
		String	port	= StringCaster.cast( config.properties.getOrDefault( "port", "" ) );

		// If we have a host use the Client URL Builder
		if ( !host.isEmpty() ) {
			return String.format(
			    "jdbc:derby://%s:%s/%s;%s",
			    host,
			    port,
			    database,
			    customParamsToQueryString( config )
			);
		}

		// Build the Embedded URL
		return String.format(
		    "jdbc:derby:%s:%s;%s",
		    this.protocol,
		    database,
		    customParamsToQueryString( config )
		);
	}

}
