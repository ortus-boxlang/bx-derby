package ortus.boxlang.modules.derby;

import static com.google.common.truth.Truth.assertThat;

import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.modules.ModuleRecord;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.services.DatasourceService;
import ortus.boxlang.runtime.services.ModuleService;
import ortus.boxlang.runtime.types.Query;
import ortus.boxlang.runtime.types.Struct;

public class DerbyDriverTest {

	@Test
	@DisplayName( "Test getName()" )
	public void testGetName() {
		DerbyDriver	driver			= new DerbyDriver();
		Key			expectedName	= new Key( "Derby" );
		assertThat( driver.getName() ).isEqualTo( expectedName );
	}

	@Test
	@DisplayName( "Test getType()" )
	public void testGetType() {
		DerbyDriver			driver			= new DerbyDriver();
		DatabaseDriverType	expectedType	= DatabaseDriverType.DERBY;
		assertThat( driver.getType() ).isEqualTo( expectedType );
	}

	@Test
	@DisplayName( "Test getClassName()" )
	public void testGetClassName() {
		DerbyDriver	driver				= new DerbyDriver();
		String		expectedClassName	= "org.apache.derby.jdbc.EmbeddedDriver";
		assertThat( driver.getClassName() ).isEqualTo( expectedClassName );
	}

	@Test
	@DisplayName( "Test buildConnectionURL()" )
	public void testBuildConnectionURL() {
		DerbyDriver			driver	= new DerbyDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "derby" );
		config.properties.put( "database", "mydb" );

		String expectedURL = "jdbc:derby:directory:mydb;create=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test connection with a memory protocol" )
	public void testBuildConnectionURLMemoryProtocol() {
		DerbyDriver			driver	= new DerbyDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "derby" );
		config.properties.put( "protocol", "memory" );
		config.properties.put( "database", "mydb" );

		String expectedURL = "jdbc:derby:memory:mydb;create=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@DisplayName( "Throw an exception if the database is not found" )
	@Test
	public void testBuildConnectionURLNoDatabase() {
		DerbyDriver			driver	= new DerbyDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		try {
			driver.buildConnectionURL( config );
		} catch ( IllegalArgumentException e ) {
			assertThat( e.getMessage() ).isEqualTo( "The database property is required for the Apache Derby JDBC Driver" );
		}
	}

	@DisplayName( "Test the module loads in BoxLang" )
	@Test
	public void testModuleLoads() {
		// Given
		Key					moduleName			= new Key( "bx-derby" );
		String				physicalPath		= Paths.get( "./build/module" ).toAbsolutePath().toString();
		ModuleRecord		moduleRecord		= new ModuleRecord( physicalPath );
		IBoxContext			context				= new ScriptingRequestBoxContext();
		BoxRuntime			runtime				= BoxRuntime.getInstance( true );
		ModuleService		moduleService		= runtime.getModuleService();
		DatasourceService	datasourceService	= runtime.getDataSourceService();
		IScope				variables			= context.getScopeNearby( VariablesScope.name );

		// When
		moduleRecord
		    .loadDescriptor( context )
		    .register( context )
		    .activate( context );

		moduleService.getRegistry().put( moduleName, moduleRecord );

		// Then
		assertThat( moduleService.getRegistry().containsKey( moduleName ) ).isTrue();
		assertThat( datasourceService.hasDriver( Key.of( "derby" ) ) ).isTrue();

		// Register a named datasource
		runtime.getConfiguration().runtime.datasources.put(
		    Key.of( "derby" ),
		    DatasourceConfig.fromStruct( Struct.of(
		        "name", "derby",
		        "properties", Struct.of(
		            "driver", "derby",
		            "database", "testDB",
		            "protocol", "memory"
		        )
		    ) )
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
