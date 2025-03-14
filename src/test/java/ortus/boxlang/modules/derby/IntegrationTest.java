/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.modules.derby;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Query;
import ortus.boxlang.runtime.types.Struct;

/**
 * This loads the module and runs an integration test on the module.
 */
public class IntegrationTest extends BaseIntegrationTest {

	@DisplayName( "Test the module loads in BoxLang" )
	@Test
	public void testModuleLoads() {
		// Given

		// Then
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();

		// Register a named datasource
		runtime.getConfiguration().datasources.put(
		    Key.of( "derby" ),
		    new DatasourceConfig(
		        "derby",
		        Struct.of(
		            "driver", "derby",
		            "database", "testDB",
		            "protocol", "memory"
		        )
		    )
		);

		// @formatter:off
		runtime.executeSource(
		    """
				try{
					queryExecute( "DROP table developers", [], { "datasource": "derby" } );
				}catch( any e ){
					// Ignore
				}
				queryExecute( "CREATE TABLE developers ( id INTEGER, name VARCHAR(155), role VARCHAR(155) )", [], { "datasource": "derby" } );
				queryExecute( "INSERT INTO developers ( id, name, role ) VALUES ( 77, 'Michael Born', 'Developer' )", [], { "datasource": "derby" } );
				result = queryExecute( "SELECT * FROM developers ORDER BY id", [], { "datasource": "derby" } );
			""",
		    context
		);
		// @formatter:on

		// Assert it executes
		Query result = ( Query ) variables.get( Key.result );
		assertThat( result.size() ).isEqualTo( 1 );

	}
}
