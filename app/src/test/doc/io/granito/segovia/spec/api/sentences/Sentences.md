# Sentence

### [Common assumptions](- "before")

All examples below assume that the application has only
"[Roberto se había levantado de la cama.](- "#sentence")"
[sentence](- "store('es', #sentence)").

### ~~Common assumptions~~

A client can get a list of sentences by using an HTTP GET request
for the sentence collection URI. Sentences are returned as a HAL
collection.

### [Get sentences](-)

When a client makes a
**[GET](- "#method") [/api/v1/languages/es/sentences](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [200](- "?=#response.status") HTTP status and
[application/hal+json](- "?=#response.contentType") body containing
JSON with at least following properties:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
  "_links": {
    "self": { "href": "/api/v1/languages/es/sentences" }
  },
  "_embedded": {
    "sentences": [
      {
        "id": "h6kLGAVxboVG",
        "text": "Roberto se había levantado de la cama.",
        "_links": {
          "self": { "href": "/api/v1/languages/es/sentences/h6kLGAVxboVG" }
        }
      }
    ]
  }
}</pre>

### ~~Get sentences~~

A client can get any sentence stored in the application by
using its identifier in the URI. See `self` link in the example
above. If there is a sentence with the provided identifier,
it will be returned as a HAL resource.

### [Get sentence](-)

When a client makes a
**[GET](- "#method")
[/api/v1/languages/es/sentences/h6kLGAVxboVG](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [200](- "?=#response.status") HTTP status and
[application/hal+json](- "?=#response.contentType") body containing
JSON with at least following properties:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
  "id": "h6kLGAVxboVG",
  "text": "Roberto se había levantado de la cama.",
  "_links": {
    "self": { "href": "/api/v1/languages/es/sentences/h6kLGAVxboVG" }
  }
}</pre>

### ~~Get sentence~~

If no sentence with the provided identifier exists, the API will
respond with `404` HTTP status code.

### [Not found](-)

When a client makes a
**[GET](- "#method") [/api/v1/languages/es/sentences/unknown](- "#uri")**
[HTTP request](- "#response=http(#method, #uri)"), then the application
responds with [404](- "?=#response.status") HTTP status and
[application/problem+json](- "?=#response.contentType") body containing
JSON with at least following properties:

<pre concordion:assert-equals="containsJson(#response.body, #TEXT)">{
  "status": 404,
  "type": "https://segovia.granito.io/problem/not-found/sentence",
  "title": "Sentence is not found.",
  "detail": "Sentence identified by 'unknown' is not found.",
  "instance": "/api/v1/languages/es/sentences/unknown"
}</pre>

### ~~Not found~~
