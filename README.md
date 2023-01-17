# Genesys Cloud Platform Connector Example Plugin

This is a demostration project of how to implement a plugin for the Genesys Cloud Platform Connector application.

The code can be checked out and then modified as per the below instructions to implement a new plugin. This should then be checked into its own code-control project and maintained independently of the container and associated applications.

### Refactor the project
* Refactor ```DemoPluginWorker.java``` to ```<YourPluginName>.java```
* Refactor plugin package. This can be any package name you like


### Update the project details in the POM
* ```<groupId> ``` - Set to your usual maven deployment group
* ```<artifactId> ``` - Give the plugin an maven artifact name
* ```<version> ``` - Give the plugin a maven version
* All version control, developer and license tags - set appropriately.
* Within the ```<properties>``` tag, there are 6 plugin.* tags that need to be set
	* ```plugin.id``` - This needs to be unique. No two loaded plugins can be the same name. If you wish to load 2 versions of the same plugin, put the version into the ID - eg demoPlugin-v0.0.1
	* ```plugin.class``` - This should be the fully qualified Java name to the PluginManager java file. So the package name you specified above plus .PluginManager
	* ```plugin.version``` - The version of your plugin, in x.y.z version
	* ```plugin.provider``` - The developer of the plugin
	* ```plugin.description``` - A text description of what the plugin does. Ideally referring to what backend system the plugin talks to
	* ```plugin.dependencies``` - A comma separated list of other plugins that this one depends on. It is likely this will NOT be used by your plugin.


### Implement your plugin business logic in ```<YourPluginName>.java```
* The following functions should be modified to include business logic:
	* ```private CreateResponse doWork(CreateRequest req)``` - The business logic to create a new item. The entire item details will be specified in the CreateRequest class. Object ID is to be returned.
	* ```private UpdateResponse doWork(UpdateRequest req)``` - The business logic to update an existing item. Whether the complete object or just deltas are specified depends how the web request is called from Genesys.
	* ```private ReadResponse doWork(ReadRequest req)``` - The business logic to find and read an item. There are two methods to search. One is by objectId, the other with search parameters. If ReadRequest.getObjectId() is populated, it is a primary key search. If it is null, then use ReadRequest.getSearchParameters()
		* This should return a SINGLE object only. If multiple records are found in the source system, it should return a code of ```MULTIPLE_RECORDS``` and an error message. 
	* ```private DeleteResponse doWork(DeleteRequest req)``` - The business logic to delete an item. This will be specified by ObjectID only.
* The above Create/Read/Update/Delete functions should remain, whether used or not. If a plugin defines a function as not used in its setup routine, then the logic within the function will never be called.
* All requests will come with a request ID. This is copied to the Response object as part of the construction of the response. It should always match the input
* All requests come with a requestDate - the plugin can utilise this as it sees fit.
* All responses are required to supply an objectId (when operation is successful),  a status (```SUCCESS/RECORD_NOT_FOUND/MULTIPLE_RECORDS/FAILURE``` enum), and an errorMessage (except on ```SUCCESS```)
	* The status will determine the HTTP response code used when sending back to Genesys.


### Implement your startup and shutdown logic in ```<YourPluginName>.java```
Two methods control the plugin startup and shutdown. These are called from other class methods that provide a shell with @PostConstruct and @PreDestroy annotations.

* ```private void pluginSetup()``` - This is called when your plugin is started by the container application. It should provide all startup functionality such as connecting to databases, etc
	* pluginSetup configures a List of supported operations. Comment out any operations that your plugin will NOT support.
* ```private void pluginDestroy()``` - This is called when your plugin is stopped by the container application. It should provide all shutdown functionality such as disconnecting to databases, cleanup, etc


### Implement your health checks and metrics in <YourPluginName.java>
Both of these functions should return the entire health status for the plugin, not just deltas. 
* ```public HealthResult getPluginHealth()``` - This returns a HealthResult class with the the plugins status and any sub components, as called from the main container.
* ```private void setHealth()``` - This is an example of how to push health using the ```container.setPluginHealth()``` function. It does not need to remain in your generated plugin. This functionality could be implemented in database connection routines, for example. 

The health subsystem must report a main plugin HealthStatus class inside the HealthResult set with the setOverallStatus() function. 

If the plugin is not reporting HealthState.HEALTHY or HealthState.WARNING it will not be used by the container application.

The following are the definitions for the four HealthState enumerations-

* HEALTHY - Plugin is 100% functional
* WARNING - Plugin has errors but these are not impacting operation. (Connected to a backup server for example)
* FAILURE - Plugin has errors which prevent normal operation.
* UNKNOWN - Plugin has not determined its health yet. A stopped plugin will return UNKNOWN in the container as it is not able to report its own status.

In all cases, a text description can be returned in the HealthStatus class, which will be sent to the monitoring platform.

The plugin can also report status of components, by creating a Map of ComponentName(String) to HealthStatus objects for each component. 
These are not used by the container application to determine whether a plugin can be used (the plugin must decide and update its overall status) but will be returned to the monitoring platform.

The Health subsystem also allows you to return metrics, which is a Map of String/Object pairs. This could include counters on number of requests, processing times, connection pool sizes, etc. There is no depenedency on these metrics by the container and will only be used by the monitoring platform.
