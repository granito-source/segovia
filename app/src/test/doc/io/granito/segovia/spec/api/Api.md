# Application Programming Interface

The Application Programming Interface (API) uses
[Hypertext Application Language (HAL)](https://en.wikipedia.org/wiki/Hypertext_Application_Language)
to carry data between the back-end and the front-end of the application.
The JSON encoding is [UTF-8](https://en.wikipedia.org/wiki/UTF-8).

The current major version of the API is designated to be **1**,
as evident from the root API entry point URI. The complete version
of the API is reported to the client in the response from the root
API request. The response also reports back if the server that handled
the request wants drain the load balancer requests. The latter is
indicated by the `status` property. Normally it is `UP`, but it may
change to `DRAIN` if the server needs to exclude itself from the
service going forward.

### [Status](- "status")

When a client makes a **[GET](- "#method") [/api/v1](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [200](- "?=#response.status") HTTP status and
[application/hal+json](- "?=#response.contentType") body containing
JSON with at least following properties:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
  "apiVersion" : "1.0.0-SNAPSHOT",
  "status" : "UP"
}</pre>

### ~~Status~~

The response is a HAL resource, meaning it contains HAL links:

* `self` to request a refreshed copy of the object;
* `sentence` to request a sentence to work with.

### [HAL Links](- "links")

When a client makes a **[GET](- "#method") [/api/v1](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the body
will contain JSON with the following HAL links:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
  "_links" : {
    "self" : {
      "href" : "/api/v1"
    }
  }
}</pre>

### ~~HAL Links~~
