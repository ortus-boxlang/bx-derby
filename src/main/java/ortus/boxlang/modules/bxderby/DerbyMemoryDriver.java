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
package ortus.boxlang.modules.bxderby;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.util.StructUtil;

public class DerbyMemoryDriver extends DerbyDriver {

	/**
	 * The name of the driver
	 */
	protected static final Key NAME = new Key( "DerbyMemory" );

	@Override
	public Key getName() {
		return NAME;
	}

	@Override
	public DatabaseDriverType getType() {
		return DatabaseDriverType.DERBYMEMORY;
	}

	@Override
	public String buildConnectionURL( DatasourceConfig config ) {
		// Validate the database
		String database = ( String ) config.properties.getOrDefault( "database", "" );
		if ( database.isEmpty() ) {
			throw new IllegalArgumentException( "The database property is required for the Apache Derby JDBC Driver" );
		}

		// Custom parameters
		IStruct params = new Struct( DEFAULT_PARAMS );
		// If the custom parameters are a string, convert them to a struct
		if ( config.properties.get( Key.custom ) instanceof String castedParams ) {
			config.properties.put( Key.custom, StructUtil.fromQueryString( castedParams, DEFAULT_DELIMITER ) );
		}
		// Add the custom parameters
		config.properties.getAsStruct( Key.custom ).forEach( params::put );

		// Build the Generic connection URL
		return String.format(
		    "jdbc:derby:memory:%s;%s",
		    database,
		    StructUtil.toQueryString( params, DEFAULT_DELIMITER )
		);
	}

}
