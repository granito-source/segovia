# Application Programming Interface

The API uses [Hypertext Application Language (HAL)](https://en.wikipedia.org/wiki/Hypertext_Application_Language)
to carry data between the back-end and the front-end of the application.
The JSON encoding is [UTF-8](https://en.wikipedia.org/wiki/UTF-8).

The current major version of the API is designated to be **1**,
as evident from the root API entry point URI. The complete version
of the API is reported to the client in the response from the root
API request. The response also reports back if the server that handled
the request wants drain the load balancer requests. The latter is
indicated by the `status` property. Normally it is `UP`, but it will
change to `DRAIN` if the server needs to exclude itself from the
service going forward.

### [API Root](- "root c:status=ExpectedToFail")

When a client makes a **[GET](- "#method") [/api/v1](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [200](- "?=#response.status") HTTP status and
[application/hal+json](- "?=#response.contentType") body containing
the following (formatted for readability):

<pre concordion:assert-equals="prettyPrint(#response.body)">{
  "_links" : {
    "self" : {
      "href" : "/api/v1"
    }
  },
  "apiVersion" : "1.0.0-SNAPSHOT",
  "status" : "UP"
}</pre>

### ~~API Root~~
