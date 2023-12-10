# User interface

The application provides a web-based user interface.

### [Home page](-)

When a user accesses **[/](- "load(#TEXT)")** URI at the application's
web site, then:

* the browser navigates to [/](- "?=uri") URI;
* the browser window has [Segovia](- "?=title") title;
* the [page](- "#body=text(body)") contains
  [Segovia](- "?=contains(#body, #TEXT)").

Below is the screenshot of the main page.

<span cx:screenshot=""></span>

### ~~Home page~~

### [Study](- "study c:status=ExpectedToFail")

Given that the application has
"[Roberto se había levantado de la cama.](- "#sentence")" sentence
as **[default](- "store(#TEXT, #sentence)")**, when a user accesses
**[/study](- "load(#TEXT)")** URI, then:

* the browser navigates to [/study](- "?=uri") URI;
* the browser window has [Segovia](- "?=title") title;
* the [page](- "#body=text(body)") contains
  [Roberto se había levantado de la cama.](- "?=contains(#body, #TEXT)")
  text.

Below is the screenshot of the main page.

<span cx:screenshot=""></span>

### ~~Study~~
