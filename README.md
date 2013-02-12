# Project

This is a small Java-based web application that performs interactive
_attribute exploration_ using
[FCAlib](http://code.google.com/p/fcalib/) and the
[Bing search API](http://datamarket.azure.com/dataset/bing/search).

## Project Setup

This is a Maven2 project, so downloading it and compiling it with
Maven should be straightforward. The resulting WAR file can be
deployed on an application server. For testing within Eclipse, the
class `de.l3s.fca.Server` can be used.

Please note that Bing search works only with a valid API key in the
file `src/main/resources/config.properties` which should look like:

    web.search.bing.accountKey = PLACE_YOUR_KEY_HERE

## Testing

Some JUnit-based tests are provided and can be run by Maven's `mvn
test`.

## License

GNU General Public License 3 (see LICENSE file)
