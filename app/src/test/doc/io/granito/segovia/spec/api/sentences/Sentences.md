# Sentence

> WARNING: this is **not** the final version of the API, subject to
> change.

### [Common assumptions](- "before")

All examples below assume that the application has
"[Roberto se había levantado de la cama.](- "#sentence")" sentence
stored under **[first.one](- "store(#TEXT, #sentence)")** identifier.

### ~~Common assumptions~~

A client can get a list of sentences by using an HTTP GET request
for the sentence collection URI. Sentences are returned as a HAL
collection.

### [Get sentences](- "get-sentences c:status=ExpectedToFail")

When a client makes a
**[GET](- "#method") [/api/v1/sentences](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [200](- "?=#response.status") HTTP status and
[application/hal+json](- "?=#response.contentType") body containing
JSON with at least following properties:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
  "_links": {
      "self": { "href": "/api/v1/sentences" }
  },
  "_embedded": {
    "sentences": {
      "id": "first.one",
      "text": "Roberto se había levantado de la cama.",
      "_links": {
        "self": { "href": "/api/v1/sentences/first.one" }
      }
    }
  }
}</pre>

### ~~Get sentences~~

A client can get any sentence stored in the application by
using its identifier in the URI. If there is such sentence, it will
be returned as a HAL resource.

### [Get sentence](- "get-sentence c:status=ExpectedToFail")

When a client makes a
**[GET](- "#method") [/api/v1/sentences/first.one](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [200](- "?=#response.status") HTTP status and
[application/hal+json](- "?=#response.contentType") body containing
JSON with at least following properties:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
  "id": "first.one",
  "text": "Roberto se había levantado de la cama.",
  "_links": {
    "self": { "href": "/api/v1/sentences/first.one" }
  }
}</pre>

### ~~Get sentence~~

If no sentence with the provided identifier exists, the API will
respond with `404` HTTP status code.

### [Not found](- "not-found c:status=ExpectedToFail")

When a client makes a
**[GET](- "#method") [/api/v1/sentences/unknown](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [404](- "?=#response.status") HTTP status and
[application/json](- "?=#response.contentType") body containing
JSON with at least following properties:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
    "status": 404,
    "error": "Not Found",
    "message": "sentence identified by 'unknown' is not found",
    "path": "/api/v1/sentences/unknown"
}</pre>

### ~~Not found~~
