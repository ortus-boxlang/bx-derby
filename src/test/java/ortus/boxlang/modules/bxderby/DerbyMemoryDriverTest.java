package ortus.boxlang.modules.bxderby;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.scopes.Key;

public class DerbyMemoryDriverTest {

	@Test
	@DisplayName( "Test getName()" )
	public void testGetName() {
		DerbyMemoryDriver	driver			= new DerbyMemoryDriver();
		Key					expectedName	= new Key( "DerbyMemory" );
		assertThat( driver.getName() ).isEqualTo( expectedName );
	}

	@Test
	@DisplayName( "Test getType()" )
	public void testGetType() {
		DerbyMemoryDriver	driver			= new DerbyMemoryDriver();
		DatabaseDriverType	expectedType	= DatabaseDriverType.DERBYMEMORY;
		assertThat( driver.getType() ).isEqualTo( expectedType );
	}

	@Test
	@DisplayName( "Test getClassName()" )
	public void testGetClassName() {
		DerbyMemoryDriver	driver				= new DerbyMemoryDriver();
		String				expectedClassName	= "org.apache.derby.jdbc.EmbeddedDriver";
		assertThat( driver.getClassName() ).isEqualTo( expectedClassName );
	}

	@Test
	@DisplayName( "Test buildConnectionURL()" )
	public void testBuildConnectionURL() {
		DerbyMemoryDriver	driver	= new DerbyMemoryDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "derby" );
		config.properties.put( "database", "mydb" );

		String expectedURL = "jdbc:derby:memory:mydb;create=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@DisplayName( "Throw an exception if the database is not found" )
	@Test
	public void testBuildConnectionURLNoDatabase() {
		DerbyMemoryDriver	driver	= new DerbyMemoryDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		try {
			driver.buildConnectionURL( config );
		} catch ( IllegalArgumentException e ) {
			assertThat( e.getMessage() ).isEqualTo( "The database property is required for the Apache Derby JDBC Driver" );
		}
	}

}
