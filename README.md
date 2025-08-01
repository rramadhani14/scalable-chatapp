# Scalable Chatapp
This is a learning project to explore many complexities introduced by scaling. A chat app(like discord or whatsapp) is a good example to explore the pitfalls of scaling. Unlike other examples like scaling CRUD or Batch processes where the services can be made stateless. A chat app combines statelessness(CRUD endpoint) and statefullness(websocket connection) that makes it quite tricky to scale safely.

An example of a tricky situation on scaling is how to inform a user that is connected to an instance that he received a message from another user that is connected to another instance. In this case, there need to be a routing mechanism that tells the instance of the sender where the receiver instance is. An easy fix for this is to have a global table that map the users and the instances. This will only works for a while until it breaks because of the high amount of users and messages.

For this project, Quarkus with Kotlin will be used for the backend. Kafka will used as message broker. For frontend, good old angular will be used.
