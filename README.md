# com.stuartsierra / log.dev

> I read the Logback manual so you don't have to.

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



## Releases and dependency information

Latest stable release is **0.2.0**

Releases are on [Clojars](https://clojars.org/)

[Leiningen] dependency information:

    [com.stuartsierra/log.dev "0.2.0"]

[Maven] dependency information:

    <dependency>
      <groupId>com.stuartsierra</groupId>
      <artifactId>log.dev</artifactId>
      <version>0.2.0</version>
    </dependency>

[Gradle] dependency information:

    compile "com.stuartsierra:log.dev:0.2.0"

[Clojars]: https://clojars.org/
[Leiningen]: http://leiningen.org/
[Maven]: http://maven.apache.org/
[Gradle]: http://www.gradle.org/



## Note on Logback totalSizeCap

log.dev currently depends on Logback 1.1.7, which includes support for
the `totalSizeCap` property to limit the total size of log files.
See [Automatic expiration](#automatic-expiration), below.

However, as described in [LOGBACK-1166], the total size limit is not
applied to the two most recent "periods" of logs. As a result, the
total space occupied by logs can still grow without bound within a
single day.

Logback 1.1.8 drops the "untouchable periods" behavior, but also
introduces [LOGBACK-1236] which causes spurious warning messages to be
printed to the console on startup. There is a fix for this issue
scheduled to be included in Logback 1.1.9.

### What this means for you

If you want a real limit on log file size, configure your project to
depend on:

    [ch.qos.logback/logback-classic "1.1.8"]
    [ch.qos.logback/logback-core "1.1.8"]

But if you do this, you will get spurious INFO and WARN messages
printed to the console when your app starts up, such as
"SizeAndTimeBasedFNATP is deprecated. Use
SizeAndTimeBasedRollingPolicy instead".

**Or** you can wait until Logback 1.1.9 is released with fixes both
issues. See [Logback News] for release announcements.

[LOGBACK-1166]: http://jira.qos.ch/browse/LOGBACK-1166
[LOGBACK-1236]: http://jira.qos.ch/browse/LOGBACK-1236
[Logback News]: http://logback.qos.ch/news.html



## Configuration

Add this library as a dependency to your project.


### Exclusions

Add **exclusions** for all other logging implementations that might be
in the transitive dependencies of your project.

See `:exclusions` in log.dev's [project.clj](project.clj) file for an
example.


### Local config file

Add a file named `log_dev_app.properties` to your project's resources,
at the root of the classpath, containing the following property:

    app_root_logger=com.example.your.application

Replace `com.example.your.application` with the top-level namespace of
your project.



## Usage

In your application code, use whatever logging API you prefer, such as
[clojure.tools.logging] or [SLF4J].

[clojure.tools.logging]: https://github.com/clojure/tools.logging
[SLF4J]: http://slf4j.org/



## Where are my logs?

### Console logging

Only log messages at levels WARN and ERROR will be printed to the
console (STDOUT). These messages may or may not be visible in an
interactive shell or REPL, depending on your development tooling and
configuration.


### Output file: log/app.log

If you set up the properties file as described above in [Configuration](#configuration),
all log messages from **your application code** will be written to the
file `log/app.log` relative to the working directory in which your
program was started.


### Output file: log/all.log

**All** log messages from **all** namespaces and packages (your
application code plus all libraries) will be written out to a file
`log/all.log` relative to the working directory in which your program
was started.

Any Java or Clojure code using any of the following logging APIs will
have their log messages written via Logback:

* [SLF4J](http://slf4j.org/)
* [Log4j 1](http://logging.apache.org/log4j/1.2/)
* [Log4j 2](http://logging.apache.org/log4j/2.x/)
* [Commons Logging](http://commons.apache.org/proper/commons-logging/)
* [OSGI LogService](https://osgi.org/javadoc/r4v42/org/osgi/service/log/LogService.html)
* See note below about java.util.logging


### Automatic log file rotation

Each log file will be rotated daily **and** every time it reaches
64 MB in size.

Rotated log files will be named like `log/all.YYYY-MM-DD.N.log` where
`YYYY-MM-DD` is the date and `N` is a sequence number.

The current log files are always `log/app.log` and `log/all.log` but
they may be replaced by new files due to rotation. So if you are
monitoring those files with another program, for example `tail -f`, you
may need to restart the monitor every time a file is rotated.


### Automatic expiration

The amount of disk space used by log files is limited to:

* 512 MB total for `app*.log` files
* 512 MB total for `all*.log` files

This adds up to an overall maximum of 1 GB for all log files.

When the total size of the current and rotated log files exceeds these
limits, the oldest files will be deleted.

Any log files more than 15 days old will be deleted automatically.



## Additional notes

### All levels enabled

All logging levels are enabled in this configuration.


### Performance

This log configuration is **verbose** and **synchronous**. This is
good for visibility during development but bad for performance.

Do not use log.dev if you are optimizing or benchmarking code which
does any logging.


### java.util.logging

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

[SLF4JBridgeHandler]: http://www.slf4j.org/apidocs/org/slf4j/bridge/SLF4JBridgeHandler.html
[java.util.logging]: http://docs.oracle.com/javase/7/docs/api/java/util/logging/package-summary.html


### Customizing

You will probably want to customize the logging configuration for
production use. To do that, **remove log.dev** from your project's
dependencies and replace it with your custom configuration.

Refer to [project.clj](project.clj) for an example of how to configure
your dependencies to forward all log APIs via SLF4J to Logback.

Refer to [resources/logback.xml](resources/logback.xml) for an example
of how to configure Logback.



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
