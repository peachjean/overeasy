[![Build Status](https://travis-ci.org/peachjean/overeasy.svg)](https://travis-ci.org/peachjean/overeasy)

Current TODO:

Document

Add "injected-command" - method call that accepts parameters auto-parsed
  myCommand(ItscoObject myObj, String arg1, Optional<String> arg2,

  Map method signature to "Options" objects
  Map CommandLine object to method call

Use Autobind for loading modules
Use Auto Post Processor for finding and binding commands

Auto Post Processor
  Bind a "post processor" in a module and it has an opportunity to modify the elements prior to creating the injector
  List<Element> postProcess(List<Element>)
  l: loop over all elements, collect all post-processors (special binding type)
  loop over all post-processors, invoking postProcess, and chaining the resultant elements together
  after each postProcess call, determine if element list was modified
  if any post processor modified the element list, repeat from "l:"

Add simple declarative parsing for Shell arguments
  Options: bind itsco object as "global", shell injects it, or simply bind "Options" arguments?

Simple auto-complete

Maven packaging plugin?

Organize code
