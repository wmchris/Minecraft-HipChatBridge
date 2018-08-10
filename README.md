# HipChatBridge

## Dependencies
* [bukkit](https://bukkit.org/)/[spigot](https://www.spigotmc.org/)/[paperspigot](https://aquifermc.org/) Server
* [Herochat](https://www.spigotmc.org/resources/34305/) or [Herochat (old)](https://www.spigotmc.org/resources/19264/)
* A server hosting [Atlassian Hipchat](https://www.hipchat.com)

## Installation
* Create one [auth token](https://developer.atlassian.com/hipchat/guide/hipchat-rest-api/api-access-tokens) in Hipchat for every room you want to sync
* Copy the hipchatbridge.jar into the plugin folder of your minecraft server
* Start the server

## Config
### Herochat Syntax
```
Global: <- Name of the Herochat channel
  active: true <- activate this one if you want to sync that channel
  auth_token: a124124123123131231223daaa <- your auth token
  url: https://example/v2/room/5/notification <- your hipchat room url
  color: gray <- use the US spelling. Hipchat doesn't like for example grey ;)
  notify: false <- aktivate sound notifications here
  send: true <- should be active if you want to send the chat from your minecraft server to hipchat
  receive: true <- not yet implemented :(
  ```
### Command Syntax
  ```
/ticket create: <- Command, don't forget the / :)
  active: true <- activate this one if you want to send this command input to hipchat
  auth_token: a124124123123131231223daaa <- your auth token
  url: https://example/v2/room/4/notification <- your hipchat room url
  color: yellow <- use the US spelling. Hipchat doesn't like for example grey ;)
  min_arguments: 1 <- minimum of arguments, so that **/ticket create** doesnt send a message to hipchat
                        , but **/ticket create i need help** does. can be deactivated with **-1**
  prefix: new ticket <- chat prefix in hipchat
  notify: true <- aktivate sound notifications here
  ```
### Example
```
Global:
  active: true
  auth_token: TOKEN
  url: https://ROOM-NOTIFICATION
  color: gray
  notify: false
  send: true
  receive: true
Support:
  active: true
  auth_token: TOKEN
  url: https://ROOM-NOTIFICATION
  color: green
  notify: false
  send: true
  receive: true
/ticket create:
  active: true
  auth_token: TOKEN
  url: https://ROOM-NOTIFICATION
  color: yellow
  min_arguments: 1
  prefix: new ticket
  notify: true
  ```
