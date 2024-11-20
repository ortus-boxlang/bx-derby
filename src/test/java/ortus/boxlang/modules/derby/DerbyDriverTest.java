package ortus.boxlang.modules.derby;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.scopes.Key;

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

}
