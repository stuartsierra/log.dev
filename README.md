# com.stuartsierra / log.dev

Quick-start logging setup for Java or Clojure development using
[SLF4J] and [Logback].

[SLF4J]: http://slf4j.org/
[Logback]: http://logback.qos.ch/

**This does not provide a logging API.** It just provides the
necessary dependencies and configuration to get all the log messages
going to the same place.

This is intended as a quick way to get logging set up in a new
project, or as an example to follow when configuring logging in your
own project. It is probably not suitable for production use.



## Releases and Dependency Information

No releases yet. Run `lein install` in this directory and then use
version 0.1.0-SNAPSHOT

[Leiningen] dependency information:

    [com.stuartsierra/log.dev "0.1.0-SNAPSHOT"]

[Maven] dependency information:

    <dependency>
      <groupId>com.stuartsierra</groupId>
      <artifactId>log.dev</artifactId>
      <version>0.1.0-SNAPSHOT</version>
    </dependency>

[Gradle] dependency information:

    compile "com.stuartsierra:log.dev:0.1.0-SNAPSHOT"

[Clojars]: https://clojars.org/
[Leiningen]: http://leiningen.org/
[Maven]: http://maven.apache.org/
[Gradle]: http://www.gradle.org/



## Usage

Add this library as a dependency to your project. Use whatever logging
API you prefer, such as [clojure.tools.logging] or [SLF4J].

[clojure.tools.logging]: https://github.com/clojure/tools.logging
[SLF4J]: http://slf4j.org/

Any Java or Clojure code using any of the following logging APIs will
have their log messages redirected to Logback:

* [SLF4J](http://slf4j.org/)
* [Log4j 1](http://logging.apache.org/log4j/1.2/)
* [Log4j 2](http://logging.apache.org/log4j/2.x/)
* [Commons Logging](http://commons.apache.org/proper/commons-logging/)
* See note below about java.util.logging

All log messages will be written out to a file `log/dev.log` relative
to the working directory in which your program was started.


### Automatic Log File Rotation

The log file will be rotated daily **and** every time it reaches 64 MB
in size. 

Rotated log files will be named like `log/dev.YYYY-MM-DD.N.log` where
`YYYY-MM-DD` is the date and `N` is a sequence number.

The current log file is always `log/dev.log`, but it may be replaced
by a new file due to rotation. So if you are monitoring that file with
another program, for example `tail -f`, you will need to restart the
monitor every time the file is rotated.

**Log files older than 30 days will be deleted.**

There is no limit on the total amount of space that log files can take
up. (See [LOGBACK-747] and [LOGBACK-918]).

[LOGBACK-747]: http://jira.qos.ch/browse/LOGBACK-747
[LOGBACK-918]: http://jira.qos.ch/browse/LOGBACK-918


### No Console Logging

No log messages will be printed to the console (STDOUT/STDERR), not
even WARN or ERROR messages. This is because console logging can
interfere with interactive development using a REPL.


### All Levels Enabled

All logging levels are enabled in this configuration.


### Special note for java.util.logging

If any code or library in your project uses the [java.util.logging]
APIs, you will need to initialize the SLF4J bridge by doing the
following when your application starts.

In Java:

```java
import org.slf4j.bridge.SLF4JBridgeHandler;
//...
SLF4JBridgeHandler.removeHandlersForRootLogger();
SLF4JBridgeHandler.install();
```

In Clojure:

```clojure
(import (org.slf4j.bridge SLF4JBridgeHandler))

(SLF4JBridgeHandler/removeHandlersForRootLogger)
(SLF4JBridgeHandler/install)
```

See the [SLF4JBridgeHandler] documentation for details.

[SLF4JBridgeHandler]:http://www.slf4j.org/apidocs/org/slf4j/bridge/SLF4JBridgeHandler.html
[java.util.logging]: http://docs.oracle.com/javase/7/docs/api/java/util/logging/package-summary.html


### Customizing

You will probably want to customize the logging configuration for
production use. To do that, **remove log.dev** from your project's
dependencies and replace it with your custom configuration.

Refer to `project.clj` for an example of how to configure your
dependencies to forward all log messages to Logback.

Refer to `resources/logback.xml` for an example of how to configure
Logback.



## Change Log

* Version 0.1.0-SNAPSHOT (in development)



## Copyright and License

The MIT License (MIT)

Copyright © 2014 Stuart Sierra

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
