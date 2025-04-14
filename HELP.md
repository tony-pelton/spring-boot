# Getting Started

### Purpose

A sample spring-boot application stack, trying to be an
e-commerce bookstore.

Trying to be as clean and best practice as possible,
letting Spring starters do as much as possible.

This isn't meant to act as or adhere to microservice principles.

In fact, it might be over-engineered a little, or a little
sub-optimal in spots etc. , but I wanted to have some things
to play with.

There are also things (Eureka, client side load balancing,
spring-boot admin) I've used here that would probably not be
used in a modern horizontally scalable cloud stack.

But I think it also shows how you might not need all of that
(more complicated discovery, fully stood up load balancers, some
3rd party monitoring tools) with this kind of functionality already
available in the spring-boot space, just to get something off
the ground.

I wanted to make something that could be checked out to source and
run right out of the box with nothing but an IDE, needing little
to no fiddling out of the box.

### Servers

The individual servers provide capabilities for:

* web ui/storefront
* database
* boot admin ui
* h2 console ui
* spring integration
* a warehouse service
* a command line for operating the system

The database is currently in memory. When the servers start,
schemas are created by hibernate, or SQL script etc, and data
currently is loaded from the 'command' application.

### Features

* spring-boot 3
* Eureka discovery
* spring-boot admin
* h2 db server, with h2-console support
* spring integration
* spring command line
* Maven multi-module, to kinda tie the room together.

### How To

First, start the various services.

This can be done a few different ways, by running the 'psvm'
for each server, or by using the maven spring-boot plugin,
and the spring-boot:run target in each module, or by using
the spring-boot:run target from the parent Maven POM for each module.

The 'services' app needs to be started first, since it provides
the discovery server, as well as the H2 database.

Then the 'integration' server.

Then the 'warehouse' server.

Finally, the 'web' server, which is the storefront.

Then the 'command' server. For right now, this only needs to be running
when, well, you need to issue commands. As of this writing, it has a
couple of commands that are used to bootstrap some data into the system.


