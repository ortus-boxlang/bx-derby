# ⚡︎ BoxLang Module: Apache Derby JDBC Driver

```
|:------------------------------------------------------:|
| ⚡︎ B o x L a n g ⚡︎
| Dynamic : Modular : Productive
|:------------------------------------------------------:|
```

<blockquote>
	Copyright Since 2023 by Ortus Solutions, Corp
	<br>
	<a href="https://www.boxlang.io">www.boxlang.io</a> |
	<a href="https://www.ortussolutions.com">www.ortussolutions.com</a>
</blockquote>

<p>&nbsp;</p>

This module provides a BoxLang JDBC driver for Apache Derby.  This module is part of the BoxLang project.

## Examples

See [BoxLang's Defining Datasources](https://boxlang.ortusbooks.com/boxlang-language/syntax/queries#defining-datasources) documentation for full examples on where and how to construct a datasource connection pool.

Here's a few examples of some Apache Derby datasources:

### Connecting to an In-Memory Database

You can specify an in-memory database using this connection string: `jdbc:derby:memory:{MyDBName};create=true` where `{MyDBName}` is replaced with your database name of choice:

```js
this.datasources[ "testDB" ] = {
	"driver"  : "derby",
	"protocol": "memory",
	"database": "testDB"
};
```

### Connecting to a Database Directory On Disk

You can also work with an on-disk database:

```js
this.datasources[ "AutoDB" ] = {
	"driver"  : "derby",
	"protocol": "directory",
	"database": "/home/michael/myApp/resources/AutoDB"
};
```

### Connecting to a .Zip or .Jar File As a Read-Only Database

Apache Derby also supports [compressing databases into a `.zip` file or `.jar` file](https://db.apache.org/derby/docs/10.17/devguide/tdevdeploy33704.html), then [accessing those as a readonly database](https://db.apache.org/derby/docs/10.17/devguide/cdevdeploy11201.html):

Doing this as a BoxLang datasource would look like:

```js
this.datasources[ "myJarDB" ] = {
	"driver"  : "derby",
	"protocol": "jar",
	"database": expandPath( "./libs/myDB.jar" ) & "products/boiledfood"
};
```

Note that even though `protocol` is set to `"jar"`, a `.zip` file is fully supported. 😁

Note it is important to specify an absolute path to the jar or zip file, and the database file path within the jar must be specified correctly. You may need to use a leading slash. For more notes, see the Apache Derby documentatiion on [Transferring read-only databases to archive (jar or zip) files](https://db.apache.org/derby/docs/10.17/devguide/tdevdeploy33704.html) and [Accessing a read-only database in a zip/jar file](https://db.apache.org/derby/docs/10.17/devguide/cdevdeploy11201.html).


## Ortus Sponsors

BoxLang is a professional open-source project and it is completely funded by the [community](https://patreon.com/ortussolutions) and [Ortus Solutions, Corp](https://www.ortussolutions.com).  Ortus Patreons get many benefits like a cfcasts account, a FORGEBOX Pro account and so much more.  If you are interested in becoming a sponsor, please visit our patronage page: [https://patreon.com/ortussolutions](https://patreon.com/ortussolutions)

### THE DAILY BREAD

 > "I am the way, and the truth, and the life; no one comes to the Father, but by me (JESUS)" Jn 14:1-12
