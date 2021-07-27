# Selenium JUtils
[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/selenium-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22selenium-jutils%22)
![Build status](https://github.com/fslev/selenium-jutils/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/selenium-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/selenium-jutils?branch=main)

A light Java library designed to tackle the shortcomings of Selenium Page Object Model and Page Factory features.  
It provides the following enhancements:
- WebContext, used for locating web elements relative to a parent element  
- Retry on error mechanism, used when an error or exception occurs while locating a web element, such as StaleElementReferenceException.  
