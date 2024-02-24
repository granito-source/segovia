# User interface

The application provides a web-based user interface.

### [Home page](-)

When a user accesses **[/](- "load(#TEXT)")** URI at the application's
site, then:

* the browser navigates to [/home](- "?=uri") URI;
* the browser window has [Segovia: Home](- "?=title") title;
* the [page](- "#page=body") has
  [Welcome to Segovia](- "?=text(pageHeader(#page))") header.

Below is the screenshot of the main page.

<span cx:screenshot=""></span>

### ~~Home page~~

The application has _Study_ page that allows student learn the
language using a text in the target language.

### [Study](-)

Given that the application has
"[Roberto se había levantado de la cama.](- "#sentence")"
[sentence](- "store('es', #sentence)"), when a user accesses
**[/study](- "load(#TEXT)")** URI, then:

* the browser navigates to [/study](- "?=uri") URI;
* the browser window has [Segovia: Study](- "?=title") title;
* the [page](- "#page=body") has [Study](- "?=text(pageHeader(#page))")
  header;
* it also presents the following
  [sentence](- "#sentence=text(activeSentence(#page))") for study:
  "[Roberto se había levantado de la cama.](- "?=contains(#sentence, #TEXT)")"

Below is the screenshot of the study page.

<span cx:screenshot=""></span>

### ~~Study~~
