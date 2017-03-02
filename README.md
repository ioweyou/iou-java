<!--
The MIT License (MIT)

Copyright (c) 2016 Ely Deckers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->

# IOU Java

[![Travis CI](https://travis-ci.org/ioweyou/iou-java.svg)](https://travis-ci.org/ioweyou/iou-java)
[![License MIT](https://img.shields.io/:license-mit-blue.svg)](http://badges.mit-license.org)

<a href="https://promisesaplus.com/">
    <img src="https://promisesaplus.com/assets/logo-small.png" alt="Promises/A+ logo"
         title="Promises/A+ 1.0 compliant" align="right" />
</a>
IOU Java is a [Promises/A+](https://github.com/promises-aplus/promises-spec) compliant promise library that extends [IOU Core](https://github.com/ioweyou/iou-core).

## Maven
-----
```xml
<dependency>
    <groupId>nl.brusque.iou</groupId>
    <artifactId>iou-java</artifactId>
    <version>1.0.0-beta-01</version>
</dependency>
```

## Gradle
-----
```
compile 'nl.brusque.iou:iou-java:1.0.0-beta-01' { transitive = true }
```

## Example
-----
### Call with single then

```java
IOU<Integer> iou = new IOU<>();

iou.getPromise()
        .then(new IThenCallable<Integer, Void>() {
            @Override
            public Void apply(Integer input) throws Exception {
                System.out.println(input.toString());

                return null;
            }
        });

iou.resolve(42); // prints "42"
```

### Chained or piped promise
```java
IOU<Integer> iou = new IOU<>();

iou.getPromise()
        .then(new IThenCallable<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) throws Exception {
                return input * 10;
            }
        })
        .then(new IThenCallable<Integer, String>() {
            @Override
            public String apply(Integer input) throws Exception {
                return String.format("The result: %d", input);
            }
        })
        .then(new IThenCallable<String, Void>() {
            @Override
            public Void apply(String input) throws Exception {
                System.out.println(input);

                return null;
            }
        });

iou.resolve(42); // prints "The result: 420"
```
### Rejecting a promise
```java
IOU<Integer> iou = new IOU<>();

iou.getPromise()
        .then(new IThenCallable<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * 42;
            }
        })
        .fail(new IThenCallable<Object, Void>() {
            @Override
            public Void apply(Object input) throws Exception {
                System.out.println(String.format("%s I can't do that.", input));

                return null;
            }
        });

iou.reject("I'm sorry, Dave."); // prints "I'm sorry, Dave. I can't do that."
```
Or if you like the A+ way better
```java
IOU<Integer> iou = new IOU<>();

iou.getPromise()
        .then(new IThenCallable<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) throws Exception {
                return input * 42;
            }
        }, new IThenCallable<Object, Integer>() {
            @Override
            public Integer apply(Object input) throws Exception {
                System.out.println(String.format("%s I can't do that.", input));

                return null;
            }
        });

iou.reject("I'm sorry, A+."); // prints "I'm sorry, A+. I can't do that."
```
### Failing a promise
```java
IOU<Integer> iou = new IOU<>();

iou.getPromise()
        .then(new IThenCallable<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) throws Exception {
                throw new Exception("I just don't care.");
            }
        })
        .then(new IThenCallable<Integer, Void>() {
            @Override
            public Void apply(Integer input) throws Exception {
                System.out.println("What would you say you do here?");

                return null;
            }
        })
        .fail(new IThenCallable<Object, Void>() {
            @Override
            public Void apply(Object reason) throws Exception {
                System.out.println(
                    String.format("It's not that I'm lazy, it's that %s",
                            ((Exception)reason).getMessage()));

                return null;
            }
        });

iou.resolve(42); // prints "It's not that I'm lazy, it's that I just don't care."
```
